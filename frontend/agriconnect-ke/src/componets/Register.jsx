// src/components/Register.jsx
import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import axios from 'axios';
import './Auth.css';

const Register = () => {
  const [formData, setFormData] = useState({
    fullName: '',
    phone: '',
    password: '',
    confirmPassword: '',
    role: 'BUYER',
    county: '',
    businessType: ''
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    // Validate passwords match
    if (formData.password !== formData.confirmPassword) {
      setError('Passwords do not match');
      setLoading(false);
      return;
    }

    // Validate password length
    if (formData.password.length < 6) {
      setError('Password must be at least 6 characters long');
      setLoading(false);
      return;
    }

    try {
      const { confirmPassword, ...submitData } = formData;
      const response = await axios.post('/api/auth/register', submitData);
      
      // Store user data and token
      localStorage.setItem('token', response.data.token);
      localStorage.setItem('user', JSON.stringify(response.data.user));
      
      // Redirect based on role
      const user = response.data.user;
      switch (user.role) {
        case 'BUYER':
          navigate('/buyer/dashboard');
          break;
        case 'FARMER':
          navigate('/farmer/dashboard');
          break;
        case 'DRIVER':
          navigate('/driver/app');
          break;
        case 'ADMIN':
          navigate('/admin/dashboard');
          break;
        default:
          navigate('/');
      }
    } catch (error) {
      setError(error.response?.data?.message || 'Registration failed. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <div className="auth-header">
          <h1>Create Account</h1>
          <p>Join AgriConnectKE today</p>
        </div>

        {error && <div className="error-message">{error}</div>}

        <form onSubmit={handleSubmit} className="auth-form">
          <div className="form-group">
            <label htmlFor="fullName" className="form-label">
              Full Name
            </label>
            <input
              type="text"
              id="fullName"
              name="fullName"
              value={formData.fullName}
              onChange={handleChange}
              className="form-input"
              placeholder="Enter your full name"
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="phone" className="form-label">
              Phone Number
            </label>
            <input
              type="tel"
              id="phone"
              name="phone"
              value={formData.phone}
              onChange={handleChange}
              className="form-input"
              placeholder="0712345678"
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="role" className="form-label">
              I am a
            </label>
            <select
              id="role"
              name="role"
              value={formData.role}
              onChange={handleChange}
              className="form-select"
              required
            >
              <option value="BUYER">Buyer</option>
              <option value="FARMER">Farmer</option>
              <option value="DRIVER">Driver</option>
            </select>
          </div>

          <div className="form-group">
            <label htmlFor="county" className="form-label">
              County
            </label>
            <input
              type="text"
              id="county"
              name="county"
              value={formData.county}
              onChange={handleChange}
              className="form-input"
              placeholder="Enter your county"
              required
            />
          </div>

          {formData.role === 'BUYER' && (
            <div className="form-group">
              <label htmlFor="businessType" className="form-label">
                Business Type
              </label>
              <input
                type="text"
                id="businessType"
                name="businessType"
                value={formData.businessType}
                onChange={handleChange}
                className="form-input"
                placeholder="e.g., Supermarket, Restaurant"
              />
            </div>
          )}

          <div className="form-group">
            <label htmlFor="password" className="form-label">
              Password
            </label>
            <input
              type="password"
              id="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              className="form-input"
              placeholder="At least 6 characters"
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="confirmPassword" className="form-label">
              Confirm Password
            </label>
            <input
              type="password"
              id="confirmPassword"
              name="confirmPassword"
              value={formData.confirmPassword}
              onChange={handleChange}
              className="form-input"
              placeholder="Confirm your password"
              required
            />
          </div>

          <button 
            type="submit" 
            className="btn-primary auth-btn"
            disabled={loading}
          >
            {loading ? (
              <>
                <div className="loading-spinner"></div>
                Creating Account...
              </>
            ) : (
              'Create Account'
            )}
          </button>
        </form>

        <div className="auth-footer">
          <p>
            Already have an account?{' '}
            <Link to="/login" className="auth-link">
              Sign in here
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
};

export default Register;