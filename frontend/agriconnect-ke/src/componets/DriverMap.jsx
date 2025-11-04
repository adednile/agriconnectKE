// src/components/DriverMap.jsx
import React, { useEffect, useState, useRef } from 'react';
import { MapContainer, TileLayer, Marker, Popup, Polyline, useMap } from 'react-leaflet';
import L from 'leaflet';
import axios from 'axios';
import 'leaflet/dist/leaflet.css';

// Fix for default markers in react-leaflet
delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon-2x.png',
  iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png',
  shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-shadow.png',
});

// Custom icons
const driverIcon = new L.Icon({
  iconUrl: '/driver-marker.png', // You can create custom icons
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
});

const farmerIcon = new L.Icon({
  iconUrl: '/farmer-marker.png',
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
});

const buyerIcon = new L.Icon({
  iconUrl: '/buyer-marker.png',
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
});

const DriverMap = ({ deliveryId, driverId, showRoute = false }) => {
  const [driverLocation, setDriverLocation] = useState(null);
  const [deliveryDetails, setDeliveryDetails] = useState(null);
  const [route, setRoute] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const mapRef = useRef();

  useEffect(() => {
    if (driverId) {
      fetchDriverLocation();
      // Set up real-time updates every 30 seconds
      const interval = setInterval(fetchDriverLocation, 30000);
      return () => clearInterval(interval);
    }
  }, [driverId]);

  useEffect(() => {
    if (deliveryId) {
      fetchDeliveryDetails();
    }
  }, [deliveryId]);

  const fetchDriverLocation = async () => {
    try {
      const response = await axios.get(`/api/driver-locations/${driverId}`);
      setDriverLocation(response.data);
    } catch (error) {
      console.error('Error fetching driver location:', error);
    }
  };

  const fetchDeliveryDetails = async () => {
    try {
      const response = await axios.get(`/api/deliveries/order/${deliveryId}`);
      const delivery = response.data;
      setDeliveryDetails(delivery);
      
      if (showRoute) {
        calculateRoute(delivery);
      }
    } catch (error) {
      console.error('Error fetching delivery details:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const calculateRoute = async (delivery) => {
    try {
      // Use OpenStreetMap Nominatim for route calculation
      const response = await axios.get(`/api/deliveries/${delivery.deliveryId}/route`);
      setRoute(response.data.route || []);
    } catch (error) {
      console.error('Error calculating route:', error);
      // Fallback: create a simple straight line
      if (delivery.pickupLat && delivery.pickupLon && delivery.dropoffLat && delivery.dropoffLon) {
        setRoute([
          [delivery.pickupLat, delivery.pickupLon],
          [delivery.dropoffLat, delivery.dropoffLon]
        ]);
      }
    }
  };

  if (isLoading) {
    return <div className="loading">Loading map...</div>;
  }

  const center = driverLocation 
    ? [driverLocation.latitude, driverLocation.longitude]
    : [ -1.286389, 36.817223]; // Default to Nairobi

  return (
    <div className="driver-map-container" style={{ height: '500px', width: '100%' }}>
      <MapContainer
        center={center}
        zoom={13}
        style={{ height: '100%', width: '100%' }}
        ref={mapRef}
      >
        <TileLayer
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        />
        
        {/* Driver Location Marker */}
        {driverLocation && (
          <Marker
            position={[driverLocation.latitude, driverLocation.longitude]}
            icon={driverIcon}
          >
            <Popup>
              <div>
                <strong>Driver Location</strong><br />
                Last updated: {new Date(driverLocation.lastUpdated).toLocaleString()}
              </div>
            </Popup>
          </Marker>
        )}

        {/* Delivery Route */}
        {showRoute && deliveryDetails && route.length > 0 && (
          <>
            {/* Pickup Location */}
            <Marker
              position={[deliveryDetails.pickupLat, deliveryDetails.pickupLon]}
              icon={farmerIcon}
            >
              <Popup>
                <div>
                  <strong>Pickup Location</strong><br />
                  Farmer's Location
                </div>
              </Popup>
            </Marker>

            {/* Dropoff Location */}
            <Marker
              position={[deliveryDetails.dropoffLat, deliveryDetails.dropoffLon]}
              icon={buyerIcon}
            >
              <Popup>
                <div>
                  <strong>Delivery Location</strong><br />
                  Buyer's Location
                </div>
              </Popup>
            </Marker>

            {/* Route Line */}
            <Polyline
              positions={route}
              color="blue"
              weight={4}
              opacity={0.7}
            />
          </>
        )}

        {/* Auto-center map when driver moves */}
        {driverLocation && <MapUpdater location={driverLocation} />}
      </MapContainer>
    </div>
  );
};

// Component to auto-update map center
const MapUpdater = ({ location }) => {
  const map = useMap();
  
  useEffect(() => {
    if (location) {
      map.setView([location.latitude, location.longitude]);
    }
  }, [location, map]);

  return null;
};

export default DriverMap;