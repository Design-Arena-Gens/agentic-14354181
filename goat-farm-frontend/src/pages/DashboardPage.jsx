import { useEffect, useState } from 'react';
import { Card, Col, Row } from 'react-bootstrap';
import { Line, Pie, Bar } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  ArcElement,
  BarElement,
  Tooltip,
  Legend
} from 'chart.js';
import { apiClient } from '../services/apiClient';

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, ArcElement, BarElement, Tooltip, Legend);

function DashboardPage() {
  const [herdSummary, setHerdSummary] = useState({});
  const [financialSummary, setFinancialSummary] = useState({});
  const [healthSummary, setHealthSummary] = useState({});

  useEffect(() => {
    async function fetchSummary() {
      const [herdRes, financialRes, healthRes] = await Promise.all([
        apiClient.get('/reports/herd-summary'),
        apiClient.get('/reports/financial-summary'),
        apiClient.get('/reports/health-summary')
      ]);
      setHerdSummary(herdRes.data);
      setFinancialSummary(financialRes.data);
      setHealthSummary(healthRes.data);
    }
    fetchSummary().catch(() => {
      setHerdSummary({});
      setFinancialSummary({});
      setHealthSummary({});
    });
  }, []);

  const herdDistributionData = {
    labels: ['Does', 'Bucks'],
    datasets: [
      {
        data: [herdSummary.totalDoes || 0, herdSummary.totalBucks || 0],
        backgroundColor: ['#4caf50', '#2196f3']
      }
    ]
  };

  const financialTrendData = {
    labels: ['Sales', 'Income', 'Expenses'],
    datasets: [
      {
        label: 'Amount (INR)',
        data: [Number(financialSummary.totalSales || 0), Number(financialSummary.totalIncome || 0), Number(financialSummary.totalExpenses || 0)],
        backgroundColor: ['#1e88e5', '#43a047', '#e53935']
      }
    ]
  };

  const healthTrendData = {
    labels: ['Vaccinations this month', 'Total health records'],
    datasets: [
      {
        label: 'Records',
        data: [healthSummary.vaccinationsThisMonth || 0, healthSummary.totalHealthRecords || 0],
        borderColor: '#7b1fa2',
        backgroundColor: 'rgba(123, 31, 162, 0.3)'
      }
    ]
  };

  return (
    <div className="dashboard">
      <Row className="g-4 mb-4">
        <Col md={4}>
          <Card className="card-shadow">
            <Card.Body>
              <Card.Title>Total Herd</Card.Title>
              <h2>{herdSummary.totalGoats ?? 0}</h2>
              <small className="text-muted">Average weight: {herdSummary.averageWeight?.toFixed?.(1) || 0} kg</small>
            </Card.Body>
          </Card>
        </Col>
        <Col md={4}>
          <Card className="card-shadow">
            <Card.Body>
              <Card.Title>Net Profit</Card.Title>
              <h2>₹{Number(financialSummary.netProfit || 0).toLocaleString()}</h2>
              <small className="text-muted">Sales ₹{Number(financialSummary.totalSales || 0).toLocaleString()}</small>
            </Card.Body>
          </Card>
        </Col>
        <Col md={4}>
          <Card className="card-shadow">
            <Card.Body>
              <Card.Title>Health Performance</Card.Title>
              <h2>{healthSummary.vaccinationsThisMonth ?? 0}</h2>
              <small className="text-muted">Vaccinations completed this month</small>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      <Row className="g-4">
        <Col md={4}>
          <Card className="card-shadow">
            <Card.Body>
              <Card.Title>Herd Distribution</Card.Title>
              <Pie data={herdDistributionData} />
            </Card.Body>
          </Card>
        </Col>
        <Col md={4}>
          <Card className="card-shadow">
            <Card.Body>
              <Card.Title>Financial Overview</Card.Title>
              <Bar data={financialTrendData} options={{ responsive: true, plugins: { legend: { display: false } } }} />
            </Card.Body>
          </Card>
        </Col>
        <Col md={4}>
          <Card className="card-shadow">
            <Card.Body>
              <Card.Title>Health Activity</Card.Title>
              <Line data={healthTrendData} options={{ responsive: true, plugins: { legend: { display: false } } }} />
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </div>
  );
}

export default DashboardPage;
