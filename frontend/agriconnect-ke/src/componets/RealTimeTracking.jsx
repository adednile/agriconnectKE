// src/components/RealTimeTracking.jsx
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import DriverMap from './DriverMap';
import './RealTimeTracking.css';

const RealTimeTracking = ({ orderId, orderDetails }) => {
  const [delivery, setDelivery] = useState(null);
  const [driverLocation, setDriverLocation] = useState(null);
  const [routeInfo, setRouteInfo] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (orderId) {
      fetchDeliveryData();
      // Set up polling for real-time updates every 10 seconds
      const interval = setInterval(fetchDeliveryData, 10000);
      return () => clearInterval(interval);
    }
  }, [orderId]);

  const fetchDeliveryData = async () => {
    try {
      setError(null);
      
      // Fetch delivery details
      const deliveryResponse = await axios.get(`/api/deliveries/order/${orderId}`);
      const deliveryData = deliveryResponse.data;
      setDelivery(deliveryData);

      // Fetch route information
      if (deliveryData.deliveryId) {
        const routeResponse = await axios.get(`/api/deliveries/${deliveryData.deliveryId}/route`);
        setRouteInfo(routeResponse.data);
      }

      // Fetch driver location if driver is assigned
      if (deliveryData.driverId) {
        const locationResponse = await axios.get(`/api/driver-locations/${deliveryData.driverId}`);
        setDriverLocation(locationResponse.data);
        
        // Also fetch driver progress
        const progressResponse = await axios.get(`/api/deliveries/${deliveryData.deliveryId}/driver-progress`);
        setRouteInfo(prev => ({ ...prev, ...progressResponse.data }));
      }
    } catch (error) {
      console.error('Error fetching delivery data:', error);
      setError('Unable to load tracking information');
    } finally {
      setLoading(false);
    }
  };

  const getStatusDescription = (status) => {
    const statusMap = {
      'PENDING': 'Waiting for driver assignment',
      'ASSIGNED': 'Driver assigned and preparing for pickup',
      'PICKED_UP': 'Driver has picked up your order',
      'IN_TRANSIT': 'Your order is on the way',
      'DELIVERED': 'Order delivered successfully',
      'CANCELLED': 'Order was cancelled'
    };
    return statusMap[status] || status;
  };

  if (loading) {
    return (
      <div className="tracking-loading">
        <div className="spinner"></div>
        <p>Loading tracking information...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="tracking-error">
        <p>{error}</p>
        <button onClick={fetchDeliveryData} className="retry-btn">
          Retry
        </button>
      </div>
    );
  }

  if (!delivery) {
    return (
      <div className="no-delivery">
        <p>No delivery information found for this order.</p>
      </div>
    );
  }

  return (
    <div className="real-time-tracking">
      {/* Delivery Information Card */}
      <div className="delivery-info-card">
        <div className="info-section">
          <h4>Delivery Status</h4>
          <div className="status-indicator">
            <span className={`status-badge status-${delivery.status?.toLowerCase()}`}>
              {delivery.status?.replace('_', ' ')}
            </span>
            <p className="status-description">
              {getStatusDescription(delivery.status)}
            </p>
          </div>
        </div>

        {routeInfo && (
          <div className="info-section">
            <h4>Route Information</h4>
            <div className="route-details">
              {routeInfo.distanceKm && (
                <p><strong>Distance:</strong> {routeInfo.distanceKm.toFixed(1)} km</p>
              )}
              {routeInfo.estimatedTimeMinutes && (
                <p><strong>Estimated Time:</strong> {Math.round(routeInfo.estimatedTimeMinutes)} min</p>
              )}
              {routeInfo.progressPercentage && (
                <div className="progress-section">
                  <p><strong>Progress:</strong> {Math.round(routeInfo.progressPercentage)}%</p>
                  <div className="progress-bar">
                    <div 
                      className="progress-fill"
                      style={{ width: `${routeInfo.progressPercentage}%` }}
                    ></div>
                  </div>
                </div>
              )}
            </div>
          </div>
        )}

        {driverLocation && (
          <div className="info-section">
            <h4>Driver Information</h4>
            <div className="driver-details">
              <p><strong>Driver ID:</strong> {delivery.driverId}</p>
              <p><strong>Last Updated:</strong> {new Date(driverLocation.lastUpdated).toLocaleString()}</p>
            </div>
          </div>
        )}
      </div>

      {/* Map Section */}
      <div className="map-section">
        {delivery.driverId ? (
          <DriverMap 
            deliveryId={delivery.deliveryId}
            driverId={delivery.driverId}
            showRoute={true}
            orderDetails={orderDetails}
          />
        ) : (
          <div className="no-driver-assigned">
            <div className="waiting-icon">⏳</div>
            <h3>Waiting for Driver Assignment</h3>
            <p>Your order is confirmed and waiting for a driver to be assigned.</p>
            <p>You'll be able to track the delivery once a driver is assigned.</p>
          </div>
        )}
      </div>

      {/* Action Buttons */}
      <div className="tracking-actions">
        <button onClick={fetchDeliveryData} className="refresh-btn">
          Refresh
        </button>
        {delivery.driverId && (
          <button className="contact-driver-btn">
            Contact Driver
          </button>
        )}
      </div>
    </div>
  );
};

export default RealTimeTracking;