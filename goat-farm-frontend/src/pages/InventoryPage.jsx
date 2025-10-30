import { useMemo, useState } from 'react';
import { Button, ButtonGroup } from 'react-bootstrap';
import * as yup from 'yup';
import DataTable from '../components/tables/DataTable';
import EntityFormModal from '../components/modals/EntityFormModal';
import { useFetch } from '../hooks/useFetch';
import { apiClient } from '../services/apiClient';
import { exportToCsv } from '../utils/exportUtils';

const inventorySchema = yup.object().shape({
  itemName: yup.string().required('Item name required'),
  category: yup.string().required('Category required'),
  quantity: yup.number().integer().required('Quantity required')
});

function InventoryPage() {
  const { data: items, loading, refetch } = useFetch(() => apiClient.get('/inventory'));
  const [modalState, setModalState] = useState({ show: false, item: null });

  const columns = useMemo(() => [
    { header: 'Item', accessor: 'itemName' },
    { header: 'Category', accessor: 'category' },
    { header: 'Quantity', accessor: 'quantity' },
    { header: 'Unit', accessor: 'unit' },
    { header: 'Supplier', accessor: 'supplier' },
    {
      header: 'Actions', accessor: 'id', render: (_value, row) => (
        <ButtonGroup size="sm">
          <Button variant="outline-primary" onClick={() => setModalState({ show: true, item: row })}>Edit</Button>
          <Button variant="outline-danger" onClick={() => handleDelete(row.id)}>Delete</Button>
        </ButtonGroup>
      )
    }
  ], []);

  const handleDelete = async (id) => {
    if (window.confirm('Delete inventory item?')) {
      await apiClient.delete(`/inventory/${id}`);
      refetch();
    }
  };

  const handleSubmit = async (values) => {
    if (modalState.item) {
      await apiClient.put(`/inventory/${modalState.item.id}`, values);
    } else {
      await apiClient.post('/inventory', values);
    }
    setModalState({ show: false, item: null });
    refetch();
  };

  const fields = [
    { name: 'itemName', label: 'Item Name' },
    { name: 'category', label: 'Category' },
    { name: 'quantity', label: 'Quantity', type: 'number' },
    { name: 'unit', label: 'Unit' },
    { name: 'purchaseDate', label: 'Purchase Date', type: 'date' },
    { name: 'expiryDate', label: 'Expiry Date', type: 'date' },
    { name: 'unitCost', label: 'Unit Cost', type: 'number', step: '0.01' },
    { name: 'supplier', label: 'Supplier' }
  ];

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-3">
        <div>
          <h4>Inventory & Assets</h4>
          <p className="text-muted mb-0">Monitor feed stock, equipment, and consumable inventory.</p>
        </div>
        <ButtonGroup>
          <Button variant="outline-secondary" onClick={() => exportToCsv('Inventory Report', items)}>Export CSV</Button>
          <Button onClick={() => setModalState({ show: true, item: null })}>Add Item</Button>
        </ButtonGroup>
      </div>
      <DataTable columns={columns} data={items} isLoading={loading} />
      <EntityFormModal
        title={modalState.item ? 'Update Inventory Item' : 'Add Inventory Item'}
        fields={fields}
        show={modalState.show}
        onHide={() => setModalState({ show: false, item: null })}
        validationSchema={inventorySchema}
        initialValues={modalState.item || {}}
        onSubmit={handleSubmit}
      />
    </div>
  );
}

export default InventoryPage;
