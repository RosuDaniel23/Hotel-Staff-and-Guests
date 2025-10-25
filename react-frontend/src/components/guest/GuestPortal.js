import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import axios from 'axios';

const GuestPortal = () => {
  const { user } = useAuth();
  const [roomInfo, setRoomInfo] = useState(null);
  const [availableUpgrades, setAvailableUpgrades] = useState([]);
  const [upgradeRequests, setUpgradeRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showUpgradeModal, setShowUpgradeModal] = useState(false);

  useEffect(() => {
    fetchGuestData();
    fetchUpgradeRequests();
  }, [user]);

  const fetchGuestData = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/api/guest-portal/${user.id}/room`);
      setRoomInfo(response.data);

      if (response.data.room) {
        const upgradesResponse = await axios.get(
          `http://localhost:8080/api/guest-portal/available-upgrades?guestId=${user.id}`
        );
        setAvailableUpgrades(upgradesResponse.data);
      }
    } catch (error) {
      console.error('Error fetching guest data:', error);
    } finally {
      setLoading(false);
    }
  };

  const fetchUpgradeRequests = async () => {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/guest-portal/${user.id}/upgrade-requests`
      );
      setUpgradeRequests(response.data);
    } catch (error) {
      console.error('Error fetching upgrade requests:', error);
    }
  };

  const handleUpgradeRequest = async (roomId) => {
    try {
      await axios.post('http://localhost:8080/api/guest-portal/upgrade-request', {
        guestId: user.id,
        requestedRoomId: roomId
      });

      alert('Upgrade request submitted successfully!');
      setShowUpgradeModal(false);
      fetchUpgradeRequests();
    } catch (error) {
      alert('Error submitting upgrade request: ' + (error.response?.data?.message || error.message));
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="text-xl text-gray-600">Loading...</div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="max-w-6xl mx-auto px-4">
        <div className="bg-white rounded-lg shadow-md p-6 mb-6">
          <h1 className="text-3xl font-bold text-gray-900 mb-2">
            Welcome, {roomInfo?.guest?.name}!
          </h1>
          <p className="text-gray-600">{roomInfo?.guest?.email}</p>
        </div>

        <div className="grid md:grid-cols-2 gap-6">
          <div className="bg-white rounded-lg shadow-md p-6">
            <h2 className="text-2xl font-bold text-gray-900 mb-4">Your Room</h2>

            {roomInfo?.room ? (
              <div className="space-y-4">
                <div className="bg-indigo-50 rounded-lg p-4">
                  <div className="text-4xl font-bold text-indigo-600 mb-2">
                    Room {roomInfo.room.number}
                  </div>
                  <div className="text-lg text-gray-700">{roomInfo.room.type}</div>
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div className="bg-gray-50 rounded-lg p-4">
                    <div className="text-sm text-gray-600">Price per Night</div>
                    <div className="text-2xl font-bold text-gray-900">${roomInfo.room.price}</div>
                  </div>
                  <div className="bg-gray-50 rounded-lg p-4">
                    <div className="text-sm text-gray-600">Status</div>
                    <div className="text-lg font-semibold">
                      <span className={`inline-block px-3 py-1 rounded-full text-sm ${
                        roomInfo.room.status === 'BOOKED' 
                          ? 'bg-green-100 text-green-800' 
                          : 'bg-blue-100 text-blue-800'
                      }`}>
                        {roomInfo.room.status}
                      </span>
                    </div>
                  </div>
                </div>

                <button
                  onClick={() => setShowUpgradeModal(true)}
                  className="w-full bg-indigo-600 text-white py-3 px-4 rounded-lg font-medium hover:bg-indigo-700 transition"
                >
                  Request Room Upgrade
                </button>
              </div>
            ) : (
              <div className="text-center py-8 text-gray-600">
                <p>You don't have a room assigned yet.</p>
                <p className="mt-2">Please contact the front desk.</p>
              </div>
            )}
          </div>

          <div className="bg-white rounded-lg shadow-md p-6">
            <h2 className="text-2xl font-bold text-gray-900 mb-4">Upgrade Requests</h2>

            {upgradeRequests.length > 0 ? (
              <div className="space-y-3 max-h-96 overflow-y-auto">
                {upgradeRequests.map((request) => (
                  <div key={request.id} className="border border-gray-200 rounded-lg p-4">
                    <div className="flex justify-between items-start mb-2">
                      <div className="text-sm font-medium text-gray-900">
                        Room {request.currentRoom.number} → Room {request.requestedRoom.number}
                      </div>
                      <span className={`px-2 py-1 rounded-full text-xs font-semibold ${
                        request.status === 'PENDING' 
                          ? 'bg-yellow-100 text-yellow-800'
                          : request.status === 'APPROVED'
                          ? 'bg-green-100 text-green-800'
                          : 'bg-red-100 text-red-800'
                      }`}>
                        {request.status}
                      </span>
                    </div>
                    <div className="text-xs text-gray-600">
                      {request.currentRoom.type} (${request.currentRoom.price}) → {request.requestedRoom.type} (${request.requestedRoom.price})
                    </div>
                    <div className="text-xs text-gray-500 mt-1">
                      Requested: {new Date(request.requestedAt).toLocaleString()}
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              <div className="text-center py-8 text-gray-600">
                No upgrade requests yet.
              </div>
            )}
          </div>
        </div>
      </div>

      {showUpgradeModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
          <div className="bg-white rounded-lg max-w-3xl w-full max-h-[80vh] overflow-y-auto p-6">
            <div className="flex justify-between items-center mb-6">
              <h2 className="text-2xl font-bold text-gray-900">Available Room Upgrades</h2>
              <button
                onClick={() => setShowUpgradeModal(false)}
                className="text-gray-400 hover:text-gray-600"
              >
                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                </svg>
              </button>
            </div>

            {availableUpgrades.length > 0 ? (
              <div className="grid md:grid-cols-2 gap-4">
                {availableUpgrades.map((room) => (
                  <div key={room.id} className="border border-gray-200 rounded-lg p-4 hover:border-indigo-500 transition">
                    <div className="text-xl font-bold text-gray-900 mb-2">
                      Room {room.number}
                    </div>
                    <div className="text-gray-700 mb-1">{room.type}</div>
                    <div className="text-2xl font-bold text-indigo-600 mb-3">
                      ${room.price} <span className="text-sm text-gray-600">per night</span>
                    </div>
                    <div className="mb-3">
                      <span className="inline-block px-3 py-1 bg-green-100 text-green-800 rounded-full text-sm">
                        {room.status}
                      </span>
                    </div>
                    <button
                      onClick={() => handleUpgradeRequest(room.id)}
                      className="w-full bg-indigo-600 text-white py-2 px-4 rounded-lg font-medium hover:bg-indigo-700 transition"
                    >
                      Request This Room
                    </button>
                  </div>
                ))}
              </div>
            ) : (
              <div className="text-center py-8 text-gray-600">
                No upgrade options available at the moment.
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
};

export default GuestPortal;
