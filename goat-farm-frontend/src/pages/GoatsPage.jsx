import { useMemo, useState } from 'react';
import { Button, ButtonGroup } from 'react-bootstrap';
import * as yup from 'yup';
import { apiClient } from '../services/apiClient';
import DataTable from '../components/tables/DataTable';
import EntityFormModal from '../components/modals/EntityFormModal';
import { useFetch } from '../hooks/useFetch';

const goatSchema = yup.object().shape({
  tagId: yup.string().required('Tag is required'),
  name: yup.string().nullable(),
  gender: yup.string().oneOf(['DOE', 'BUCK']).required('Gender is mandatory'),
  breed: yup.string().required('Breed is required'),
  dateOfBirth: yup.string().nullable(),
  weightKg: yup.number().nullable(),
  bodyConditionScore: yup.number().nullable(),
  status: yup.string().nullable()
});

function GoatsPage() {
  const [modalState, setModalState] = useState({ show: false, goat: null });
  const { data: goats, loading, refetch } = useFetch(() => apiClient.get('/goats'));

  const columns = useMemo(() => [
    { header: 'Tag', accessor: 'tagId' },
    { header: 'Breed', accessor: 'breed' },
    { header: 'Gender', accessor: 'gender' },
    { header: 'Weight (kg)', accessor: 'weightKg' },
    { header: 'Status', accessor: 'status', render: (value) => <span className="badge bg-info-subtle text-info-emphasis">{value || 'Active'}</span> },
    {
      header: 'Actions', accessor: 'id', render: (_value, row) => (
        <ButtonGroup size="sm">
          <Button variant="outline-primary" onClick={() => setModalState({ show: true, goat: row })}>
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
    if (window.confirm('Delete goat record?')) {
      await apiClient.delete(`/goats/${id}`);
      refetch();
    }
  };

  const handleSubmit = async (values) => {
    if (modalState.goat) {
      await apiClient.put(`/goats/${modalState.goat.id}`, values);
    } else {
      await apiClient.post('/goats', values);
    }
    setModalState({ show: false, goat: null });
    refetch();
  };

  const fields = [
    { name: 'tagId', label: 'Tag ID' },
    { name: 'name', label: 'Name' },
    { name: 'gender', label: 'Gender', type: 'select', options: [{ label: 'Doe', value: 'DOE' }, { label: 'Buck', value: 'BUCK' }] },
    { name: 'breed', label: 'Breed' },
    { name: 'dateOfBirth', label: 'Date of Birth', type: 'date' },
    { name: 'weightKg', label: 'Weight (kg)', type: 'number', step: '0.1' },
    { name: 'bodyConditionScore', label: 'Body Condition Score', type: 'number', step: '0.1' },
    { name: 'status', label: 'Status' },
    { name: 'motherId', label: 'Mother ID' },
    { name: 'fatherId', label: 'Father ID' }
  ];

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-3">
        <div>
          <h4>Herd Management</h4>
          <p className="text-muted mb-0">Track herd demographics, lineage, and productivity.</p>
        </div>
        <Button onClick={() => setModalState({ show: true, goat: null })}>Add Goat</Button>
      </div>
      <DataTable columns={columns} data={goats} isLoading={loading} />
      <EntityFormModal
        title={modalState.goat ? 'Update Goat' : 'Register Goat'}
        fields={fields}
        show={modalState.show}
        onHide={() => setModalState({ show: false, goat: null })}
        validationSchema={goatSchema}
        initialValues={modalState.goat || { gender: 'DOE' }}
        onSubmit={handleSubmit}
        submitLabel={modalState.goat ? 'Update' : 'Create'}
      />
    </div>
  );
}

export default GoatsPage;
