import { useMemo, useState } from 'react';
import { Button, ButtonGroup, Card, Col, Row, Tabs, Tab } from 'react-bootstrap';
import * as yup from 'yup';
import DataTable from '../components/tables/DataTable';
import EntityFormModal from '../components/modals/EntityFormModal';
import { useFetch } from '../hooks/useFetch';
import { apiClient } from '../services/apiClient';
import { exportToExcel, exportToPdf } from '../utils/exportUtils';

const salesSchema = yup.object().shape({
  goatId: yup.string().required('Goat ID required'),
  buyerName: yup.string().required('Buyer name required'),
  saleDate: yup.string().required('Sale date required'),
  salePrice: yup.number().positive().required('Sale price required')
});

const incomeSchema = yup.object().shape({
  incomeDate: yup.string().required('Date required'),
  source: yup.string().required('Source required'),
  amount: yup.number().positive().required('Amount required')
});

const expenseSchema = yup.object().shape({
  expenseDate: yup.string().required('Date required'),
  category: yup.string().required('Category required'),
  amount: yup.number().positive().required('Amount required')
});

function SalesPage() {
  const { data: sales, loading: salesLoading, refetch: refetchSales } = useFetch(() => apiClient.get('/sales'));
  const { data: incomes, loading: incomesLoading, refetch: refetchIncome } = useFetch(() => apiClient.get('/income'));
  const { data: expenses, loading: expensesLoading, refetch: refetchExpenses } = useFetch(() => apiClient.get('/expenses'));

  const [activeTab, setActiveTab] = useState('sales');
  const [modalState, setModalState] = useState({ show: false, record: null, type: 'sales' });

  const salesColumns = useMemo(() => [
    { header: 'Goat ID', accessor: 'goatId' },
    { header: 'Buyer', accessor: 'buyerName' },
    { header: 'Sale Date', accessor: 'saleDate' },
    { header: 'Price', accessor: 'salePrice', render: (value) => `₹${Number(value).toLocaleString()}` },
    { header: 'Payment Status', accessor: 'paymentStatus' },
    {
      header: 'Actions', accessor: 'id', render: (_value, row) => (
        <ButtonGroup size="sm">
          <Button variant="outline-primary" onClick={() => setModalState({ show: true, record: row, type: 'sales' })}>Edit</Button>
          <Button variant="outline-danger" onClick={() => handleDelete('sales', row.id)}>Delete</Button>
        </ButtonGroup>
      )
    }
  ], []);

  const incomeColumns = useMemo(() => [
    { header: 'Date', accessor: 'incomeDate' },
    { header: 'Source', accessor: 'source' },
    { header: 'Amount', accessor: 'amount', render: (value) => `₹${Number(value).toLocaleString()}` },
    { header: 'Description', accessor: 'description' },
    {
      header: 'Actions', accessor: 'id', render: (_value, row) => (
        <ButtonGroup size="sm">
          <Button variant="outline-primary" onClick={() => setModalState({ show: true, record: row, type: 'income' })}>Edit</Button>
          <Button variant="outline-danger" onClick={() => handleDelete('income', row.id)}>Delete</Button>
        </ButtonGroup>
      )
    }
  ], []);

  const expenseColumns = useMemo(() => [
    { header: 'Date', accessor: 'expenseDate' },
    { header: 'Category', accessor: 'category' },
    { header: 'Amount', accessor: 'amount', render: (value) => `₹${Number(value).toLocaleString()}` },
    { header: 'Vendor', accessor: 'vendor' },
    {
      header: 'Actions', accessor: 'id', render: (_value, row) => (
        <ButtonGroup size="sm">
          <Button variant="outline-primary" onClick={() => setModalState({ show: true, record: row, type: 'expense' })}>Edit</Button>
          <Button variant="outline-danger" onClick={() => handleDelete('expense', row.id)}>Delete</Button>
        </ButtonGroup>
      )
    }
  ], []);

  const handleDelete = async (type, id) => {
    if (!window.confirm('Delete record?')) return;
    if (type === 'sales') {
      await apiClient.delete(`/sales/${id}`);
      refetchSales();
    } else if (type === 'income') {
      await apiClient.delete(`/income/${id}`);
      refetchIncome();
    } else {
      await apiClient.delete(`/expenses/${id}`);
      refetchExpenses();
    }
  };

  const handleSubmit = async (values) => {
    const { type, record } = modalState;
    const isUpdate = Boolean(record);
    const endpointMap = {
      sales: '/sales',
      income: '/income',
      expense: '/expenses'
    };
    const endpoint = endpointMap[type];
    if (isUpdate) {
      await apiClient.put(`${endpoint}/${record.id}`, values);
    } else {
      await apiClient.post(endpoint, values);
    }

    setModalState({ show: false, record: null, type });
    refetchSales();
    refetchIncome();
    refetchExpenses();
  };

  const getFields = () => {
    switch (modalState.type) {
      case 'income':
        return [
          { name: 'incomeDate', label: 'Date', type: 'date' },
          { name: 'source', label: 'Source' },
          { name: 'amount', label: 'Amount', type: 'number', step: '0.01' },
          { name: 'description', label: 'Description', type: 'textarea', rows: 3, col: 'col-12' }
        ];
      case 'expense':
        return [
          { name: 'expenseDate', label: 'Date', type: 'date' },
          { name: 'category', label: 'Category' },
          { name: 'amount', label: 'Amount', type: 'number', step: '0.01' },
          { name: 'vendor', label: 'Vendor' },
          { name: 'description', label: 'Description', type: 'textarea', rows: 3, col: 'col-12' }
        ];
      default:
        return [
          { name: 'goatId', label: 'Goat ID' },
          { name: 'buyerName', label: 'Buyer Name' },
          { name: 'saleDate', label: 'Sale Date', type: 'date' },
          { name: 'salePrice', label: 'Sale Price', type: 'number', step: '0.01' },
          { name: 'paymentStatus', label: 'Payment Status' },
          { name: 'remarks', label: 'Remarks', type: 'textarea', rows: 3, col: 'col-12' }
        ];
    }
  };

  const getSchema = () => {
    switch (modalState.type) {
      case 'income':
        return incomeSchema;
      case 'expense':
        return expenseSchema;
      default:
        return salesSchema;
    }
  };

  const getInitialValues = () => modalState.record || {};

  const summaryCards = [
    {
      title: 'Total Sales',
      value: sales.reduce((acc, record) => acc + (record.salePrice || 0), 0)
    },
    {
      title: 'Total Income',
      value: incomes.reduce((acc, record) => acc + (record.amount || 0), 0)
    },
    {
      title: 'Total Expenses',
      value: expenses.reduce((acc, record) => acc + (record.amount || 0), 0)
    }
  ];

  const exportCurrentTab = (format) => {
    const tabMap = {
      sales: { title: 'Sales Records', data: sales, columns: salesColumns },
      income: { title: 'Income Ledger', data: incomes, columns: incomeColumns },
      expense: { title: 'Expense Ledger', data: expenses, columns: expenseColumns }
    };
    const { title, data, columns } = tabMap[activeTab];
    if (format === 'pdf') {
      exportToPdf(title, columns, data);
    } else {
      exportToExcel(title, data);
    }
  };

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-3">
        <div>
          <h4>Sales & Finance</h4>
          <p className="text-muted mb-0">Track revenue streams, operational costs, and profitability.</p>
        </div>
        <ButtonGroup>
          <Button variant="outline-secondary" onClick={() => exportCurrentTab('pdf')}>Export PDF</Button>
          <Button variant="outline-secondary" onClick={() => exportCurrentTab('excel')}>Export Excel</Button>
          <Button onClick={() => setModalState({ show: true, record: null, type: activeTab })}>Add {activeTab}</Button>
        </ButtonGroup>
      </div>

      <Row className="g-4 mb-4">
        {summaryCards.map((card) => (
          <Col key={card.title} md={4}>
            <Card className="card-shadow">
              <Card.Body>
                <Card.Title>{card.title}</Card.Title>
                <h4>₹{card.value.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}</h4>
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>

      <Tabs activeKey={activeTab} onSelect={(key) => setActiveTab(key)} className="mb-3">
        <Tab eventKey="sales" title="Sales">
          <DataTable columns={salesColumns} data={sales} isLoading={salesLoading} />
        </Tab>
        <Tab eventKey="income" title="Income">
          <DataTable columns={incomeColumns} data={incomes} isLoading={incomesLoading} />
        </Tab>
        <Tab eventKey="expense" title="Expenses">
          <DataTable columns={expenseColumns} data={expenses} isLoading={expensesLoading} />
        </Tab>
      </Tabs>

      <EntityFormModal
        title={`Manage ${modalState.type.charAt(0).toUpperCase() + modalState.type.slice(1)} Record`}
        fields={getFields()}
        show={modalState.show}
        onHide={() => setModalState({ show: false, record: null, type: activeTab })}
        validationSchema={getSchema()}
        initialValues={getInitialValues()}
        onSubmit={handleSubmit}
        submitLabel={modalState.record ? 'Update' : 'Create'}
      />
    </div>
  );
}

export default SalesPage;
