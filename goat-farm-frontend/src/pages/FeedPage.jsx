import { useMemo, useState } from 'react';
import { Button, ButtonGroup } from 'react-bootstrap';
import * as yup from 'yup';
import DataTable from '../components/tables/DataTable';
import EntityFormModal from '../components/modals/EntityFormModal';
import { useFetch } from '../hooks/useFetch';
import { apiClient } from '../services/apiClient';

const feedSchema = yup.object().shape({
  goatId: yup.string().required('Goat ID is required'),
  feedName: yup.string().required('Feed name required'),
  quantityKg: yup.number().positive().required('Quantity is required'),
  scheduledDate: yup.string().required('Date required')
});

function FeedPage() {
  const { data: schedules, loading, refetch } = useFetch(() => apiClient.get('/feed'));
  const [modalState, setModalState] = useState({ show: false, schedule: null });

  const columns = useMemo(() => [
    { header: 'Goat ID', accessor: 'goatId' },
    { header: 'Feed Name', accessor: 'feedName' },
    { header: 'Quantity (kg)', accessor: 'quantityKg' },
    { header: 'Date', accessor: 'scheduledDate' },
    { header: 'Status', accessor: 'status', render: (value) => <span className={`badge badge-status bg-${value === 'COMPLETED' ? 'success' : 'warning'}-subtle text-${value === 'COMPLETED' ? 'success' : 'warning'}-emphasis`}>{value}</span> },
    {
      header: 'Actions', accessor: 'id', render: (_value, row) => (
        <ButtonGroup size="sm">
          <Button variant="outline-primary" onClick={() => setModalState({ show: true, schedule: row })}>
            Edit
          </Button>
          <Button variant="outline-danger" onClick={() => handleDelete(row.id)}>
            Delete
          </Button>
        </ButtonGroup>
      )
    }
  ], []);

  const handleDelete = async (id) => {
    if (window.confirm('Delete feed schedule?')) {
      await apiClient.delete(`/feed/${id}`);
      refetch();
    }
  };

  const handleSubmit = async (values) => {
    if (modalState.schedule) {
      await apiClient.put(`/feed/${modalState.schedule.id}`, values);
    } else {
      await apiClient.post('/feed', values);
    }
    setModalState({ show: false, schedule: null });
    refetch();
  };

  const fields = [
    { name: 'goatId', label: 'Goat ID' },
    { name: 'feedName', label: 'Feed Name' },
    { name: 'quantityKg', label: 'Quantity (kg)', type: 'number', step: '0.1' },
    { name: 'scheduledDate', label: 'Scheduled Date', type: 'date' },
    { name: 'scheduledTime', label: 'Scheduled Time', type: 'time' },
    {
      name: 'status', label: 'Status', type: 'select', options: [
        { label: 'Planned', value: 'PLANNED' },
        { label: 'Completed', value: 'COMPLETED' },
        { label: 'Missed', value: 'MISSED' }
      ]
    }
  ];

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-3">
        <div>
          <h4>Feed & Nutrition</h4>
          <p className="text-muted mb-0">Manage feed schedules, conversion ratios, and nutrient intake.</p>
        </div>
        <Button onClick={() => setModalState({ show: true, schedule: null })}>Schedule Feed</Button>
      </div>
      <DataTable columns={columns} data={schedules} isLoading={loading} />
      <EntityFormModal
        title={modalState.schedule ? 'Update Feed Schedule' : 'New Feed Schedule'}
        fields={fields}
        show={modalState.show}
        onHide={() => setModalState({ show: false, schedule: null })}
        validationSchema={feedSchema}
        initialValues={modalState.schedule || { status: 'PLANNED' }}
        onSubmit={handleSubmit}
      />
    </div>
  );
}

export default FeedPage;
