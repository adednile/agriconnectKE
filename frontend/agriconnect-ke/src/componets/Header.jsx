// src/components/Header.jsx
import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import './Header.css';

const Header = ({ user }) => {
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = () => {
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    navigate('/login');
  };

  const getNavItems = () => {
    switch (user.role) {
      case 'BUYER':
        return [
          { path: '/buyer/dashboard', label: 'Dashboard' },
          { path: '/buyer/orders', label: 'My Orders' },
          { path: '/buyer/market', label: 'Marketplace' }
        ];
      case 'FARMER':
        return [
          { path: '/farmer/dashboard', label: 'Dashboard' },
          { path: '/farmer/listings', label: 'My Listings' },
          { path: '/farmer/sales', label: 'Sales' }
        ];
      case 'DRIVER':
        return [
          { path: '/driver/app', label: 'Driver App' },
          { path: '/driver/deliveries', label: 'My Deliveries' },
          { path: '/driver/earnings', label: 'Earnings' }
        ];
      case 'ADMIN':
        return [
          { path: '/admin/dashboard', label: 'Dashboard' },
          { path: '/admin/users', label: 'Users' },
          { path: '/admin/deliveries', label: 'Deliveries' },
          { path: '/admin/analytics', label: 'Analytics' }
        ];
      default:
        return [];
    }
  };

  const navItems = getNavItems();

  return (
    <header className="app-header">
      <div className="header-content">
        <div className="logo" onClick={() => navigate('/')}>
          <h2>🌱 AgriConnectKE</h2>
        </div>
        
        <nav className="navigation">
          {navItems.map(item => (
            <button
              key={item.path}
              className={`nav-btn ${location.pathname === item.path ? 'active' : ''}`}
              onClick={() => navigate(item.path)}
            >
              {item.label}
            </button>
          ))}
        </nav>
        
        <div className="user-section">
          <span className="user-info">
            Welcome, {user.fullName} ({user.role.toLowerCase()})
          </span>
          <button className="logout-btn" onClick={handleLogout}>
            Logout
          </button>
        </div>
      </div>
    </header>
  );
};

export default Header;