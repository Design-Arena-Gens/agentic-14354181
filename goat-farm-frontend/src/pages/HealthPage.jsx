import { useMemo, useState } from 'react';
import { Button, ButtonGroup } from 'react-bootstrap';
import * as yup from 'yup';
import DataTable from '../components/tables/DataTable';
import EntityFormModal from '../components/modals/EntityFormModal';
import { useFetch } from '../hooks/useFetch';
import { apiClient } from '../services/apiClient';

const healthSchema = yup.object().shape({
  goatId: yup.string().required('Goat ID required'),
  recordType: yup.string().required('Record type required'),
  recordDate: yup.string().required('Record date required')
});

function HealthPage() {
  const { data: records, loading, refetch } = useFetch(() => apiClient.get('/health'));
  const [modalState, setModalState] = useState({ show: false, record: null });

  const columns = useMemo(() => [
    { header: 'Goat ID', accessor: 'goatId' },
    { header: 'Type', accessor: 'recordType' },
    { header: 'Date', accessor: 'recordDate' },
    { header: 'Veterinarian', accessor: 'veterinarian' },
    { header: 'Notes', accessor: 'notes' },
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
    if (window.confirm('Delete health record?')) {
      await apiClient.delete(`/health/${id}`);
      refetch();
    }
  };

  const handleSubmit = async (values) => {
    if (modalState.record) {
      await apiClient.put(`/health/${modalState.record.id}`, values);
    } else {
      await apiClient.post('/health', values);
    }
    setModalState({ show: false, record: null });
    refetch();
  };

  const fields = [
    { name: 'goatId', label: 'Goat ID' },
    {
      name: 'recordType',
      label: 'Record Type',
      type: 'select',
      options: [
        { label: 'Vaccination', value: 'VACCINATION' },
        { label: 'Deworming', value: 'DEWORMING' },
        { label: 'Treatment', value: 'TREATMENT' },
        { label: 'Checkup', value: 'CHECKUP' },
        { label: 'Quarantine', value: 'QUARANTINE' }
      ]
    },
    { name: 'recordDate', label: 'Record Date', type: 'date' },
    { name: 'vaccineOrMedicine', label: 'Vaccine / Medicine' },
    { name: 'dosage', label: 'Dosage' },
    { name: 'veterinarian', label: 'Veterinarian' },
    { name: 'notes', label: 'Notes', type: 'textarea', rows: 3, col: 'col-12' }
  ];

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-3">
        <div>
          <h4>Health & Vaccination</h4>
          <p className="text-muted mb-0">Monitor preventive care, treatments, and veterinary interventions.</p>
        </div>
        <Button onClick={() => setModalState({ show: true, record: null })}>Add Health Record</Button>
      </div>
      <DataTable columns={columns} data={records} isLoading={loading} />
      <EntityFormModal
        title={modalState.record ? 'Update Health Record' : 'New Health Record'}
        fields={fields}
        show={modalState.show}
        onHide={() => setModalState({ show: false, record: null })}
        validationSchema={healthSchema}
        initialValues={modalState.record || { recordType: 'VACCINATION' }}
        onSubmit={handleSubmit}
      />
    </div>
  );
}

export default HealthPage;
