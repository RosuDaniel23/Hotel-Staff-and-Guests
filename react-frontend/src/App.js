import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link, Navigate } from 'react-router-dom';
import RoomList from './components/rooms/RoomList';
import EmployeeList from './components/employees/EmployeeList';
import GuestList from './components/guests/GuestList';
import Dashboard from './components/Dashboard';
import Login from './components/auth/Login';
import GuestPortal from './components/guest/GuestPortal';
import ProtectedRoute from './components/auth/ProtectedRoute';
import { AuthProvider, useAuth } from './context/AuthContext';

function AppContent() {
  const { user, userType, logout } = useAuth();

  return (
    <Router>
      <div className="min-h-screen bg-gray-50">
        {user && userType === 'employee' && (
          <nav className="bg-white shadow-sm">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
              <div className="flex justify-between h-16">
                <div className="flex">
                  <div className="flex-shrink-0 flex items-center">
                    <h1 className="text-xl font-bold">Hotel Management</h1>
                  </div>
                  <div className="hidden sm:ml-6 sm:flex sm:space-x-8">
                    <Link to="/dashboard" className="text-gray-900 inline-flex items-center px-1 pt-1 border-b-2 border-transparent hover:border-gray-300">
                      Dashboard
                    </Link>
                    <Link to="/rooms" className="text-gray-900 inline-flex items-center px-1 pt-1 border-b-2 border-transparent hover:border-gray-300">
                      Rooms
                    </Link>
                    <Link to="/employees" className="text-gray-900 inline-flex items-center px-1 pt-1 border-b-2 border-transparent hover:border-gray-300">
                      Employees
                    </Link>
                    <Link to="/guests" className="text-gray-900 inline-flex items-center px-1 pt-1 border-b-2 border-transparent hover:border-gray-300">
                      Guests
                    </Link>
                  </div>
                </div>
                <div className="flex items-center">
                  <span className="text-sm text-gray-700 mr-4">
                    {user.name} ({user.role})
                  </span>
                  <button
                    onClick={logout}
                    className="bg-red-600 text-white px-4 py-2 rounded-md hover:bg-red-700 transition"
                  >
                    Logout
                  </button>
                </div>
              </div>
            </div>
          </nav>
        )}

        {user && userType === 'guest' && (
          <nav className="bg-white shadow-sm">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
              <div className="flex justify-between h-16">
                <div className="flex items-center">
                  <h1 className="text-xl font-bold">Guest Portal</h1>
                </div>
                <div className="flex items-center">
                  <span className="text-sm text-gray-700 mr-4">
                    {user.name}
                  </span>
                  <button
                    onClick={logout}
                    className="bg-red-600 text-white px-4 py-2 rounded-md hover:bg-red-700 transition"
                  >
                    Logout
                  </button>
                </div>
              </div>
            </div>
          </nav>
        )}

        <div className={user && userType === 'employee' ? "max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8" : ""}>
          <Routes>
            <Route path="/login" element={!user ? <Login /> : <Navigate to={userType === 'employee' ? '/dashboard' : '/guest-portal'} />} />

            <Route path="/dashboard" element={
              <ProtectedRoute allowedTypes={['employee']}>
                <Dashboard />
              </ProtectedRoute>
            } />
            <Route path="/rooms" element={
              <ProtectedRoute allowedTypes={['employee']}>
                <RoomList />
              </ProtectedRoute>
            } />
            <Route path="/employees" element={
              <ProtectedRoute allowedTypes={['employee']}>
                <EmployeeList />
              </ProtectedRoute>
            } />
            <Route path="/guests" element={
              <ProtectedRoute allowedTypes={['employee']}>
                <GuestList />
              </ProtectedRoute>
            } />

            <Route path="/guest-portal" element={
              <ProtectedRoute allowedTypes={['guest']}>
                <GuestPortal />
              </ProtectedRoute>
            } />

            <Route path="/" element={
              user ? (
                <Navigate to={userType === 'employee' ? '/dashboard' : '/guest-portal'} />
              ) : (
                <Navigate to="/login" />
              )
            } />
          </Routes>
        </div>
      </div>
    </Router>
  );
}

function App() {
  return (
    <AuthProvider>
      <AppContent />
    </AuthProvider>
  );
}

export default App;
