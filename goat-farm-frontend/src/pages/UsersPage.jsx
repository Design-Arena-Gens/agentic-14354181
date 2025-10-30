import { useMemo, useState } from 'react';
import { Badge, Button, ButtonGroup } from 'react-bootstrap';
import * as yup from 'yup';
import DataTable from '../components/tables/DataTable';
import EntityFormModal from '../components/modals/EntityFormModal';
import { useFetch } from '../hooks/useFetch';
import { apiClient } from '../services/apiClient';

const userSchema = yup.object().shape({
  fullName: yup.string().required('Full name required'),
  email: yup.string().email().required('Email required'),
  username: yup.string().required('Username required'),
  password: yup.string().min(6, 'At least 6 characters').nullable()
});

function UsersPage() {
  const { data: users, loading, refetch } = useFetch(() => apiClient.get('/users'));
  const [modalState, setModalState] = useState({ show: false, user: null });

  const columns = useMemo(() => [
    { header: 'Name', accessor: 'fullName' },
    { header: 'Email', accessor: 'email' },
    { header: 'Username', accessor: 'username' },
    {
      header: 'Roles', accessor: 'roles', render: (roles) => roles?.map((role) => (
        <Badge bg="secondary" key={role} className="me-1">{role}</Badge>
      ))
    },
    { header: 'Status', accessor: 'enabled', render: (enabled) => enabled ? <Badge bg="success">Active</Badge> : <Badge bg="danger">Disabled</Badge> },
    {
      header: 'Actions', accessor: 'id', render: (_value, row) => (
        <ButtonGroup size="sm">
          <Button variant="outline-primary" onClick={() => setModalState({ show: true, user: row })}>Edit</Button>
          <Button variant={row.enabled ? 'outline-warning' : 'outline-success'} onClick={() => toggleStatus(row)}>
            {row.enabled ? 'Disable' : 'Enable'}
          </Button>
        </ButtonGroup>
      )
    }
  ], []);

  const toggleStatus = async (user) => {
    await apiClient.patch(`/users/${user.id}/status`, null, { params: { enabled: !user.enabled } });
    refetch();
  };

  const handleSubmit = async (values) => {
    const payload = {
      ...values,
      roles: values.roles ? values.roles.split(',').map((role) => role.trim()).filter(Boolean) : []
    };
    if (modalState.user) {
      await apiClient.put(`/users/${modalState.user.id}`, payload);
    } else {
      await apiClient.post('/users', payload);
    }
    setModalState({ show: false, user: null });
    refetch();
  };

  const fields = [
    { name: 'fullName', label: 'Full Name' },
    { name: 'email', label: 'Email', type: 'email' },
    { name: 'username', label: 'Username' },
    { name: 'password', label: 'Password', type: 'password' },
    { name: 'phone', label: 'Phone' },
    { name: 'roles', label: 'Roles (comma separated)', col: 'col-12' }
  ];

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-3">
        <div>
          <h4>User & Role Management</h4>
          <p className="text-muted mb-0">Manage access levels across administrators, veterinarians, and workers.</p>
        </div>
        <Button onClick={() => setModalState({ show: true, user: null })}>Invite User</Button>
      </div>
      <DataTable columns={columns} data={users} isLoading={loading} />
      <EntityFormModal
        title={modalState.user ? 'Update User' : 'Invite User'}
        fields={fields}
        show={modalState.show}
        onHide={() => setModalState({ show: false, user: null })}
        validationSchema={userSchema}
        initialValues={modalState.user ? { ...modalState.user, roles: modalState.user.roles?.join(', ') } : { roles: 'VIEWER' }}
        onSubmit={handleSubmit}
        submitLabel={modalState.user ? 'Update' : 'Create'}
      />
    </div>
  );
}

export default UsersPage;
