import { useMemo, useState } from 'react';
import { Button, ButtonGroup } from 'react-bootstrap';
import * as yup from 'yup';
import DataTable from '../components/tables/DataTable';
import EntityFormModal from '../components/modals/EntityFormModal';
import { useFetch } from '../hooks/useFetch';
import { apiClient } from '../services/apiClient';

const breedingSchema = yup.object().shape({
  doeId: yup.string().required('Doe ID is required'),
  buckId: yup.string().required('Buck ID is required'),
  breedingDate: yup.string().required('Breeding date is required')
});

function BreedingPage() {
  const { data: records, loading, refetch } = useFetch(() => apiClient.get('/breeding'));
  const [modalState, setModalState] = useState({ show: false, record: null });

  const columns = useMemo(() => [
    { header: 'Doe ID', accessor: 'doeId' },
    { header: 'Buck ID', accessor: 'buckId' },
    { header: 'Breeding Date', accessor: 'breedingDate' },
    { header: 'Expected Kidding', accessor: 'expectedKiddingDate' },
    { header: 'Kids Born', accessor: 'kidsBorn' },
    {
      header: 'Actions', accessor: 'id', render: (_value, row) => (
        <ButtonGroup size="sm">
          <Button variant="outline-primary" onClick={() => setModalState({ show: true, record: row })}>
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
    if (window.confirm('Delete breeding record?')) {
      await apiClient.delete(`/breeding/${id}`);
      refetch();
    }
  };

  const handleSubmit = async (values) => {
    if (modalState.record) {
      await apiClient.put(`/breeding/${modalState.record.id}`, values);
    } else {
      await apiClient.post('/breeding', values);
    }
    setModalState({ show: false, record: null });
    refetch();
  };

  const fields = [
    { name: 'doeId', label: 'Doe ID' },
    { name: 'buckId', label: 'Buck ID' },
    { name: 'breedingDate', label: 'Breeding Date', type: 'date' },
    { name: 'expectedKiddingDate', label: 'Expected Kidding Date', type: 'date' },
    { name: 'actualKiddingDate', label: 'Actual Kidding Date', type: 'date' },
    { name: 'kidsBorn', label: 'Kids Born', type: 'number' },
    { name: 'remarks', label: 'Remarks', type: 'textarea', rows: 3, col: 'col-12' }
  ];

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-3">
        <div>
          <h4>Breeding Management</h4>
          <p className="text-muted mb-0">Track breeding cycles, conception, and kidding performance.</p>
        </div>
        <Button onClick={() => setModalState({ show: true, record: null })}>Log Breeding</Button>
      </div>
      <DataTable columns={columns} data={records} isLoading={loading} />
      <EntityFormModal
        title={modalState.record ? 'Update Breeding Record' : 'Add Breeding Record'}
        fields={fields}
        show={modalState.show}
        onHide={() => setModalState({ show: false, record: null })}
        validationSchema={breedingSchema}
        initialValues={modalState.record || {}}
        onSubmit={handleSubmit}
      />
    </div>
  );
}

export default BreedingPage;
