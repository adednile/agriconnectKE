// src/components/BuyerDashboard.jsx
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import RealTimeTracking from './RealTimeTracking';
import './BuyerDashboard.css';

const BuyerDashboard = () => {
  const [orders, setOrders] = useState([]);
  const [selectedOrder, setSelectedOrder] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchBuyerOrders();
  }, []);

  const fetchBuyerOrders = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get('/api/orders/buyer/my-orders', {
        headers: { Authorization: `Bearer ${token}` }
      });
      setOrders(response.data);
    } catch (error) {
      console.error('Error fetching orders:', error);
    } finally {
      setLoading(false);
    }
  };

  const getStatusBadge = (status) => {
    const statusColors = {
      'PENDING': 'bg-yellow-100 text-yellow-800',
      'PAID': 'bg-blue-100 text-blue-800',
      'ASSIGNED': 'bg-purple-100 text-purple-800',
      'IN_TRANSIT': 'bg-orange-100 text-orange-800',
      'DELIVERED': 'bg-green-100 text-green-800',
      'CANCELLED': 'bg-red-100 text-red-800'
    };
    
    return (
      <span className={`px-2 py-1 rounded-full text-xs font-medium ${statusColors[status] || 'bg-gray-100'}`}>
        {status.replace('_', ' ')}
      </span>
    );
  };

  if (loading) {
    return <div className="loading">Loading your orders...</div>;
  }

  return (
    <div className="buyer-dashboard">
      <div className="dashboard-header">
        <h1>My Orders & Delivery Tracking</h1>
        <p>Track your orders in real-time</p>
      </div>

      <div className="dashboard-content">
        {/* Orders List */}
        <div className="orders-section">
          <h2>Recent Orders</h2>
          {orders.length === 0 ? (
            <div className="no-orders">
              <p>You haven't placed any orders yet.</p>
            </div>
          ) : (
            <div className="orders-grid">
              {orders.map(order => (
                <div 
                  key={order.orderId} 
                  className={`order-card ${selectedOrder?.orderId === order.orderId ? 'selected' : ''}`}
                  onClick={() => setSelectedOrder(order)}
                >
                  <div className="order-header">
                    <h3>Order #{order.orderId}</h3>
                    {getStatusBadge(order.orderStatus)}
                  </div>
                  
                  <div className="order-details">
                    <p><strong>Crop:</strong> {order.listing?.cropName}</p>
                    <p><strong>Quantity:</strong> {order.quantity} kg</p>
                    <p><strong>Total:</strong> KSh {order.totalPrice?.toLocaleString()}</p>
                    <p><strong>Order Date:</strong> {new Date(order.createdAt).toLocaleDateString()}</p>
                  </div>

                  {order.delivery && (
                    <div className="delivery-info">
                      <p><strong>Delivery Status:</strong> {order.delivery.status}</p>
                      {order.delivery.driverId && (
                        <p><strong>Driver Assigned:</strong> Yes</p>
                      )}
                    </div>
                  )}
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Tracking Section */}
        {selectedOrder && (
          <div className="tracking-section">
            <div className="tracking-header">
              <h2>Tracking Order #{selectedOrder.orderId}</h2>
              <button 
                className="close-btn"
                onClick={() => setSelectedOrder(null)}
              >
                ×
              </button>
            </div>
            
            <RealTimeTracking 
              orderId={selectedOrder.orderId} 
              orderDetails={selectedOrder}
            />
          </div>
        )}
      </div>
    </div>
  );
};

export default BuyerDashboard;