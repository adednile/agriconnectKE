// src/components/DriverApp.jsx
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import DriverMap from './DriverMap';
import './DriverApp.css';

const DriverApp = ({ driverId }) => {
  const [deliveries, setDeliveries] = useState([]);
  const [currentDelivery, setCurrentDelivery] = useState(null);
  const [currentLocation, setCurrentLocation] = useState(null);
  const [locationError, setLocationError] = useState(null);
  const [isUpdatingLocation, setIsUpdatingLocation] = useState(false);

  useEffect(() => {
    if (driverId) {
      fetchDriverDeliveries();
      startLocationTracking();
    }
  }, [driverId]);

  const fetchDriverDeliveries = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get(`/api/deliveries/driver/${driverId}`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      setDeliveries(response.data);
      
      // Set current delivery if there's an active one
      const activeDelivery = response.data.find(d => 
        ['ASSIGNED', 'PICKED_UP', 'IN_TRANSIT'].includes(d.status)
      );
      if (activeDelivery) {
        setCurrentDelivery(activeDelivery);
      }
    } catch (error) {
      console.error('Error fetching deliveries:', error);
    }
  };

  const startLocationTracking = () => {
    if (navigator.geolocation) {
      // Get initial position
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const { latitude, longitude } = position.coords;
          setCurrentLocation({ latitude, longitude });
          updateDriverLocation(latitude, longitude);
        },
        (error) => {
          setLocationError('Unable to access your location: ' + error.message);
        }
      );

      // Set up continuous tracking
      const watchId = navigator.geolocation.watchPosition(
        (position) => {
          const { latitude, longitude } = position.coords;
          setCurrentLocation({ latitude, longitude });
          updateDriverLocation(latitude, longitude);
        },
        (error) => {
          console.error('Location tracking error:', error);
        },
        {
          enableHighAccuracy: true,
          timeout: 10000,
          maximumAge: 30000
        }
      );

      return () => navigator.geolocation.clearWatch(watchId);
    } else {
      setLocationError('Geolocation is not supported by this browser.');
    }
  };

  const updateDriverLocation = async (latitude, longitude) => {
    if (isUpdatingLocation) return;
    
    setIsUpdatingLocation(true);
    try {
      const token = localStorage.getItem('token');
      await axios.post(`/api/driver-locations/${driverId}`, 
        { latitude, longitude },
        { headers: { Authorization: `Bearer ${token}` } }
      );
    } catch (error) {
      console.error('Error updating location:', error);
    } finally {
      setIsUpdatingLocation(false);
    }
  };

  const updateDeliveryStatus = async (deliveryId, newStatus) => {
    try {
      const token = localStorage.getItem('token');
      await axios.put(
        `/api/deliveries/${deliveryId}/status?status=${newStatus}`,
        {},
        { headers: { Authorization: `Bearer ${token}` } }
      );
      
      // Refresh deliveries
      fetchDriverDeliveries();
      
      // If this is the current delivery, update it
      if (currentDelivery?.deliveryId === deliveryId) {
        setCurrentDelivery(prev => ({ ...prev, status: newStatus }));
      }
    } catch (error) {
      console.error('Error updating delivery status:', error);
      alert('Failed to update status: ' + error.response?.data?.message || error.message);
    }
  };

  const acceptDelivery = async (deliveryId) => {
    try {
      const token = localStorage.getItem('token');
      await axios.put(
        `/api/deliveries/${deliveryId}/assign?driverId=${driverId}`,
        {},
        { headers: { Authorization: `Bearer ${token}` } }
      );
      
      fetchDriverDeliveries();
      alert('Delivery accepted successfully!');
    } catch (error) {
      console.error('Error accepting delivery:', error);
      alert('Failed to accept delivery: ' + error.response?.data?.message || error.message);
    }
  };

  const getNextAction = (delivery) => {
    switch (delivery.status) {
      case 'ASSIGNED':
        return {
          label: 'Pick Up Order',
          action: () => updateDeliveryStatus(delivery.deliveryId, 'PICKED_UP'),
          variant: 'primary'
        };
      case 'PICKED_UP':
        return {
          label: 'Start Delivery',
          action: () => updateDeliveryStatus(delivery.deliveryId, 'IN_TRANSIT'),
          variant: 'primary'
        };
      case 'IN_TRANSIT':
        return {
          label: 'Mark as Delivered',
          action: () => updateDeliveryStatus(delivery.deliveryId, 'DELIVERED'),
          variant: 'success'
        };
      default:
        return null;
    }
  };

  return (
    <div className="driver-app">
      <div className="driver-header">
        <h1>Driver Dashboard</h1>
        <div className="driver-info">
          <p><strong>Driver ID:</strong> {driverId}</p>
          {currentLocation && (
            <p>
              <strong>Current Location:</strong> 
              {currentLocation.latitude.toFixed(4)}, {currentLocation.longitude.toFixed(4)}
            </p>
          )}
          {isUpdatingLocation && <span className="updating-indicator">Updating location...</span>}
        </div>
      </div>

      {locationError && (
        <div className="location-error">
          <p>{locationError}</p>
          <button onClick={startLocationTracking}>Retry Location Access</button>
        </div>
      )}

      <div className="driver-content">
        {/* Deliveries List */}
        <div className="deliveries-section">
          <h2>Your Deliveries</h2>
          <div className="deliveries-list">
            {deliveries.map(delivery => (
              <div key={delivery.deliveryId} className="delivery-card">
                <div className="delivery-header">
                  <h3>Delivery #{delivery.deliveryId}</h3>
                  <span className={`status-badge status-${delivery.status.toLowerCase()}`}>
                    {delivery.status.replace('_', ' ')}
                  </span>
                </div>
                
                <div className="delivery-details">
                  <p><strong>Order ID:</strong> {delivery.orderId}</p>
                  <p><strong>Distance:</strong> {delivery.distanceKm} km</p>
                  <p><strong>Delivery Fee:</strong> KSh {delivery.deliveryFee?.toLocaleString()}</p>
                </div>

                <div className="delivery-actions">
                  {delivery.status === 'PENDING' && (
                    <button 
                      className="btn-primary"
                      onClick={() => acceptDelivery(delivery.deliveryId)}
                    >
                      Accept Delivery
                    </button>
                  )}
                  
                  {getNextAction(delivery) && (
                    <button 
                      className={`btn-${getNextAction(delivery).variant}`}
                      onClick={getNextAction(delivery).action}
                    >
                      {getNextAction(delivery).label}
                    </button>
                  )}

                  <button 
                    className="btn-secondary"
                    onClick={() => setCurrentDelivery(
                      currentDelivery?.deliveryId === delivery.deliveryId ? null : delivery
                    )}
                  >
                    {currentDelivery?.deliveryId === delivery.deliveryId ? 'Hide Map' : 'Show Map'}
                  </button>
                </div>
              </div>
            ))}
            
            {deliveries.length === 0 && (
              <div className="no-deliveries">
                <p>No deliveries assigned to you yet.</p>
              </div>
            )}
          </div>
        </div>

        {/* Map Section */}
        <div className="map-section">
          {currentDelivery ? (
            <>
              <h3>Navigating to Delivery #{currentDelivery.deliveryId}</h3>
              <DriverMap 
                deliveryId={currentDelivery.deliveryId}
                driverId={driverId}
                showRoute={true}
                currentLocation={currentLocation}
              />
              
              <div className="navigation-actions">
                <button 
                  className="btn-primary"
                  onClick={() => window.open(
                    `https://www.google.com/maps/dir/${currentLocation?.latitude},${currentLocation?.longitude}/${currentDelivery.dropoffLat},${currentDelivery.dropoffLon}`,
                    '_blank'
                  )}
                >
                  Open in Google Maps
                </button>
              </div>
            </>
          ) : (
            <div className="no-active-delivery">
              <h3>No Active Delivery</h3>
              <p>Select a delivery from the list to start navigation.</p>
              {currentLocation && (
                <DriverMap 
                  driverId={driverId}
                  currentLocation={currentLocation}
                  showRoute={false}
                />
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default DriverApp;