import { Outlet, NavLink, useNavigate } from 'react-router-dom';
import { Button, Dropdown, Nav } from 'react-bootstrap';
import { Leaf, Home, Activity, Pill, Wheat, ShoppingBag, Warehouse, BarChart3, Users } from 'lucide-react';
import { useAuth } from '../../hooks/useAuth';

const navItems = [
  { to: '/dashboard', icon: Home, label: 'Dashboard' },
  { to: '/goats', icon: Goat, label: 'Herd' },
  { to: '/breeding', icon: Activity, label: 'Breeding' },
  { to: '/feed', icon: Wheat, label: 'Feed' },
  { to: '/health', icon: Pill, label: 'Health' },
  { to: '/sales', icon: ShoppingBag, label: 'Sales & Finance' },
  { to: '/inventory', icon: Warehouse, label: 'Inventory' },
  { to: '/reports', icon: BarChart3, label: 'Reports' },
  { to: '/users', icon: Users, label: 'Users' }
];

function Layout() {
  const navigate = useNavigate();
  const { user, logout } = useAuth();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div className="layout">
      <aside className="sidebar">
        <div className="d-flex align-items-center mb-4">
          <Leaf className="me-2" />
          <div>
            <h5 className="mb-0">Goat Farm</h5>
            <small>Management Suite</small>
          </div>
        </div>
        <Nav className="flex-column">
          {navItems.map(({ to, icon: Icon, label }) => (
            <NavLink key={to} to={to}
              className={({ isActive }) => `d-flex align-items-center ${isActive ? 'active' : ''}`}>
              <Icon size={18} className="me-2" />
              {label}
            </NavLink>
          ))}
        </Nav>
        <div className="mt-auto pt-4">
          <Dropdown>
            <Dropdown.Toggle variant="light" className="w-100">
              {user?.username}
            </Dropdown.Toggle>
            <Dropdown.Menu>
              <Dropdown.ItemText>
                Roles: {user?.roles?.join(', ') || 'N/A'}
              </Dropdown.ItemText>
              <Dropdown.Divider />
              <Dropdown.Item onClick={handleLogout}>Sign out</Dropdown.Item>
            </Dropdown.Menu>
          </Dropdown>
        </div>
      </aside>
      <main className="content">
        <div className="d-flex justify-content-between align-items-center mb-4">
          <div>
            <h2 className="fw-bold">Farm Operations</h2>
            <p className="text-muted mb-0">Monitor herd performance, health, and finance in real-time.</p>
          </div>
          <Button onClick={() => navigate('/reports')} variant="primary">View Reports</Button>
        </div>
        <Outlet />
      </main>
    </div>
  );
}

export default Layout;
