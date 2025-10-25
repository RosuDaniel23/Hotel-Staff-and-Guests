import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

const ProtectedRoute = ({ children, allowedTypes }) => {
  const { user, userType } = useAuth();

  if (!user) {
    return <Navigate to="/login" replace />;
  }

  if (allowedTypes && !allowedTypes.includes(userType)) {
    return <Navigate to={userType === 'employee' ? '/dashboard' : '/guest-portal'} replace />;
  }

  return children;
};

export default ProtectedRoute;
