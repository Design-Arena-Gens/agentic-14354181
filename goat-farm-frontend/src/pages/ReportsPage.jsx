import { useMemo, useState } from 'react';
import { Accordion, Badge, Button, Card, Col, Form, Row } from 'react-bootstrap';
import { apiClient } from '../services/apiClient';
import { exportToExcel, exportToPdf } from '../utils/exportUtils';

const reportCatalog = [
  {
    category: 'Herd & Animal Reports',
    items: [
      { key: 'herd-summary', title: 'Goat Master List', description: 'Complete list of all goats with identifiers and demographics.' },
      { key: 'breed-distribution', title: 'Breed-wise Goat Population', description: 'Population statistics segmented by breed.' },
      { key: 'gender-distribution', title: 'Gender Distribution Report', description: 'Breakdown of does and bucks across herd.' },
      { key: 'age-distribution', title: 'Age Group Distribution', description: 'Age segmentation for herd planning.' },
      { key: 'mortality', title: 'Mortality Report', description: 'Mortality trends and causes.' },
      { key: 'growth-trend', title: 'Herd Growth Trend', description: 'Herd expansion and attrition timeline.' }
    ]
  },
  {
    category: 'Breeding & Reproduction Reports',
    items: [
      { key: 'breeding-performance', title: 'Breeding Performance Report', description: 'KPIs for service rates and conception.' },
      { key: 'kidding-outcome', title: 'Kidding Outcome Report', description: 'Live births, stillbirths, and kid survival rates.' },
      { key: 'buck-performance', title: 'Buck Performance Report', description: 'Breeding efficiency per buck.' }
    ]
  },
  {
    category: 'Feed & Nutrition Reports',
    items: [
      { key: 'feed-consumption', title: 'Feed Consumption Summary', description: 'Feed utilization by category and herd.' },
      { key: 'feed-cost', title: 'Feed Cost Analysis', description: 'Cost trends versus feed usage.' }
    ]
  },
  {
    category: 'Health & Veterinary Reports',
    items: [
      { key: 'vaccination-register', title: 'Vaccination Register', description: 'Comprehensive vaccination log.' },
      { key: 'health-status', title: 'Health Status Summary', description: 'Preventive care and treatment statistics.' }
    ]
  },
  {
    category: 'Finance & Sales Reports',
    items: [
      { key: 'sales-register', title: 'Sales Register', description: 'Sales ledger with transaction details.' },
      { key: 'pnl', title: 'Profit & Loss Statement', description: 'Income, expenses, and profitability snapshot.' },
      { key: 'cash-flow', title: 'Cash Flow Report', description: 'Cash inflow and outflow summary.' }
    ]
  }
];

const reportHandlers = {
  'herd-summary': async () => {
    const goats = (await apiClient.get('/goats')).data;
    return { title: 'Goat Master List', rows: goats };
  },
  'breed-distribution': async () => {
    const goats = (await apiClient.get('/goats')).data;
    const grouped = goats.reduce((acc, goat) => {
      acc[goat.breed] = (acc[goat.breed] || 0) + 1;
      return acc;
    }, {});
    const rows = Object.entries(grouped).map(([breed, count]) => ({ breed, count }));
    return { title: 'Breed-wise Goat Population', rows };
  },
  'gender-distribution': async () => {
    const summary = (await apiClient.get('/reports/herd-summary')).data;
    const rows = [
      { gender: 'Does', count: summary.totalDoes || 0 },
      { gender: 'Bucks', count: summary.totalBucks || 0 }
    ];
    return { title: 'Gender Distribution Report', rows };
  },
  'age-distribution': async () => {
    const goats = (await apiClient.get('/goats')).data;
    const rows = goats.map((goat) => ({
      tagId: goat.tagId,
      ageInDays: goat.dateOfBirth ? Math.floor((Date.now() - new Date(goat.dateOfBirth)) / (1000 * 3600 * 24)) : 'NA'
    }));
    return { title: 'Age Group Distribution', rows };
  },
  'sales-register': async () => {
    const sales = (await apiClient.get('/sales')).data;
    return { title: 'Sales Register', rows: sales };
  },
  pnl: async () => {
    const [sales, incomes, expenses] = await Promise.all([
      apiClient.get('/sales'),
      apiClient.get('/income'),
      apiClient.get('/expenses')
    ]);
    const totalSales = sales.data.reduce((sum, record) => sum + (record.salePrice || 0), 0);
    const totalIncome = incomes.data.reduce((sum, record) => sum + (record.amount || 0), 0);
    const totalExpenses = expenses.data.reduce((sum, record) => sum + (record.amount || 0), 0);
    const rows = [
      { metric: 'Sales', amount: totalSales },
      { metric: 'Other Income', amount: totalIncome },
      { metric: 'Expenses', amount: totalExpenses },
      { metric: 'Net Profit', amount: totalSales + totalIncome - totalExpenses }
    ];
    return { title: 'Profit & Loss Statement', rows };
  },
  'cash-flow': async () => {
    const [incomes, expenses] = await Promise.all([
      apiClient.get('/income'),
      apiClient.get('/expenses')
    ]);
    const rows = [
      { type: 'Cash Inflow', amount: incomes.data.reduce((sum, record) => sum + (record.amount || 0), 0) },
      { type: 'Cash Outflow', amount: expenses.data.reduce((sum, record) => sum + (record.amount || 0), 0) }
    ];
    return { title: 'Cash Flow Report', rows };
  }
};

function ReportsPage() {
  const [query, setQuery] = useState('');
  const [loadingKey, setLoadingKey] = useState(null);
  const [result, setResult] = useState(null);
  const [error, setError] = useState(null);

  const filteredCatalog = useMemo(() => {
    if (!query) return reportCatalog;
    return reportCatalog.map((category) => ({
      ...category,
      items: category.items.filter((item) => item.title.toLowerCase().includes(query.toLowerCase()))
    })).filter((category) => category.items.length);
  }, [query]);

  const generateReport = async (key) => {
    setLoadingKey(key);
    setError(null);
    try {
      if (!reportHandlers[key]) {
        setResult({ title: 'Report Pending', rows: [{ message: 'Detailed analytics coming soon in Roadmap Phase 2.' }] });
      } else {
        const data = await reportHandlers[key]();
        setResult(data);
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Unable to generate report.');
    } finally {
      setLoadingKey(null);
    }
  };

  const exportReport = (format) => {
    if (!result) return;
    const columns = Object.keys(result.rows[0] || {}).map((key) => ({ header: key, accessor: key }));
    if (!columns.length) return;
    if (format === 'pdf') {
      exportToPdf(result.title, columns, result.rows);
    } else {
      exportToExcel(result.title, result.rows);
    }
  };

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-3">
        <div>
          <h4>Reports & Analytics</h4>
          <p className="text-muted mb-0">Generate compliance-ready reports and performance dashboards.</p>
        </div>
        <Form.Control
          placeholder="Search reports"
          style={{ maxWidth: 280 }}
          value={query}
          onChange={(event) => setQuery(event.target.value)}
        />
      </div>

      <Accordion alwaysOpen>
        {filteredCatalog.map((category, index) => (
          <Accordion.Item eventKey={String(index)} key={category.category}>
            <Accordion.Header>
              {category.category}
              <Badge bg="primary" className="ms-2">{category.items.length}</Badge>
            </Accordion.Header>
            <Accordion.Body>
              <Row className="g-3">
                {category.items.map((item) => (
                  <Col md={6} key={item.key}>
                    <Card className="card-shadow">
                      <Card.Body>
                        <Card.Title>{item.title}</Card.Title>
                        <Card.Text>{item.description}</Card.Text>
                        <Button
                          disabled={loadingKey === item.key}
                          onClick={() => generateReport(item.key)}
                        >
                          {loadingKey === item.key ? 'Generating...' : 'Generate'}
                        </Button>
                      </Card.Body>
                    </Card>
                  </Col>
                ))}
              </Row>
            </Accordion.Body>
          </Accordion.Item>
        ))}
      </Accordion>

      {error && <div className="alert alert-danger mt-4">{error}</div>}

      {result && (
        <Card className="card-shadow mt-4">
          <Card.Body>
            <div className="d-flex justify-content-between align-items-center mb-3">
              <div>
                <h5 className="mb-1">{result.title}</h5>
                <small className="text-muted">Rows: {result.rows.length}</small>
              </div>
              <div className="d-flex gap-2">
                <Button variant="outline-secondary" onClick={() => exportReport('pdf')}>Export PDF</Button>
                <Button variant="outline-secondary" onClick={() => exportReport('excel')}>Export Excel</Button>
              </div>
            </div>
            <div className="table-responsive">
              <table className="table">
                <thead>
                  <tr>
                    {Object.keys(result.rows[0] || {}).map((column) => (
                      <th key={column}>{column}</th>
                    ))}
                  </tr>
                </thead>
                <tbody>
                  {result.rows.map((row, index) => (
                    <tr key={index}>
                      {Object.values(row).map((value, valueIndex) => (
                        <td key={valueIndex}>{value?.toString?.() ?? ''}</td>
                      ))}
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </Card.Body>
        </Card>
      )}
    </div>
  );
}

export default ReportsPage;
