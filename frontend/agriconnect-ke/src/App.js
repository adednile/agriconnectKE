// src/App.js
import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import BuyerDashboard from './components/BuyerDashboard';
import DriverApp from './components/DriverApp';
import FarmerDashboard from './components/FarmerDashboard';
import AdminDashboard from './components/AdminDashboard';
import Login from './components/Login';
import Register from './components/Register';
import Header from './components/Header';
import './App.css';

function App() {
  const user = JSON.parse(localStorage.getItem('user'));
  
  const getDefaultRoute = () => {
    if (!user) return '/login';
    
    switch (user.role) {
      case 'BUYER':
        return '/buyer/dashboard';
      case 'FARMER':
        return '/farmer/dashboard';
      case 'DRIVER':
        return '/driver/app';
      case 'ADMIN':
        return '/admin/dashboard';
      default:
        return '/login';
    }
  };

  return (
    <Router>
      <div className="App">
        {user && <Header user={user} />}
        
        <main className="main-content">
          <Routes>
            {/* Auth Routes */}
            <Route 
              path="/login" 
              element={!user ? <Login /> : <Navigate to={getDefaultRoute()} />} 
            />
            <Route 
              path="/register" 
              element={!user ? <Register /> : <Navigate to={getDefaultRoute()} />} 
            />
            
            {/* Buyer Routes */}
            <Route 
              path="/buyer/dashboard" 
              element={
                user?.role === 'BUYER' ? <BuyerDashboard /> : <Navigate to="/login" />
              } 
            />
            <Route 
              path="/buyer/orders" 
              element={
                user?.role === 'BUYER' ? <BuyerDashboard /> : <Navigate to="/login" />
              } 
            />
            
            {/* Farmer Routes */}
            <Route 
              path="/farmer/dashboard" 
              element={
                user?.role === 'FARMER' ? <FarmerDashboard /> : <Navigate to="/login" />
              } 
            />
            <Route 
              path="/farmer/listings" 
              element={
                user?.role === 'FARMER' ? <FarmerDashboard /> : <Navigate to="/login" />
              } 
            />
            
            {/* Driver Routes */}
            <Route 
              path="/driver/app" 
              element={
                user?.role === 'DRIVER' ? <DriverApp driverId={user.userId} /> : <Navigate to="/login" />
              } 
            />
            <Route 
              path="/driver/deliveries" 
              element={
                user?.role === 'DRIVER' ? <DriverApp driverId={user.userId} /> : <Navigate to="/login" />
              } 
            />
            
            {/* Admin Routes */}
            <Route 
              path="/admin/dashboard" 
              element={
                user?.role === 'ADMIN' ? <AdminDashboard /> : <Navigate to="/login" />
              } 
            />
            <Route 
              path="/admin/users" 
              element={
                user?.role === 'ADMIN' ? <AdminDashboard /> : <Navigate to="/login" />
              } 
            />
            
            {/* Default Route */}
            <Route path="/" element={<Navigate to={getDefaultRoute()} />} />
            
            {/* 404 Route */}
            <Route path="*" element={<div className="not-found">Page Not Found</div>} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;