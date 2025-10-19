import React, { useState, useEffect } from 'react';
import { guestApi, roomApi } from '../../api/endpoints';

const GuestList = () => {
    const [guests, setGuests] = useState([]);
    const [rooms, setRooms] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [currentGuest, setCurrentGuest] = useState(null);
    const [analytics, setAnalytics] = useState([]);

    useEffect(() => {
        loadData();
    }, []);

    const loadData = async () => {
        try {
            setLoading(true);
            const [guestsResponse, roomsResponse, analyticsResponse] = await Promise.all([
                guestApi.getAll(),
                roomApi.getAll(),
                guestApi.getAnalytics()
            ]);
            // Ensure we're setting arrays, even if the responses are empty
            const guestsData = Array.isArray(guestsResponse.data) ? guestsResponse.data : [];
            setGuests(guestsData);
            
            // Handle paginated room response
            const roomData = roomsResponse.data?.content || roomsResponse.data || [];
            setRooms(Array.isArray(roomData) ? roomData : []);
            
            // Map analytics to handle backend property names (cnt -> count)
            const analyticsData = (analyticsResponse.data || []).map(stat => ({
                type: stat.type,
                count: stat.cnt || stat.count || 0
            }));
            setAnalytics(analyticsData);
            setError(null);
        } catch (err) {
            setError(err.message);
            setGuests([]);
            setRooms([]);
            setAnalytics([]);
        } finally {
            setLoading(false);
        }
    };

    // Helper function to check if a room is available for assignment
    const isRoomAvailableForAssignment = (room, currentGuestRoomId) => {
        // Room is available if:
        // 1. Status is AVAILABLE, OR
        // 2. It's the current guest's room (for editing)
        // 3. AND not assigned to another guest
        const isNotAssignedToOther = !guests.some(guest => guest.roomId === room.id && guest.roomId !== currentGuestRoomId);
        return (room.status === 'AVAILABLE' && isNotAssignedToOther) || room.id === currentGuestRoomId;
    };

    const handleDelete = async (id) => {
        if (!window.confirm('Are you sure you want to delete this guest?')) return;
        try {
            await guestApi.delete(id);
            await loadData();
        } catch (err) {
            setError(err.message);
        }
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            if (currentGuest.id) {
                await guestApi.update(currentGuest.id, currentGuest);
            } else {
                await guestApi.create(currentGuest);
            }
            setCurrentGuest(null);
            await loadData();
        } catch (err) {
            setError(err.message);
        }
    };

    const handleAssignRoom = async (guestId, roomId) => {
        try {
            await guestApi.assignRoom(guestId, roomId);
            await loadData();
        } catch (err) {
            setError(err.message);
        }
    };

    if (loading) return <div className="text-center">Loading...</div>;
    if (error) return <div className="text-red-600">{error}</div>;

    return (
        <div>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
                {analytics && analytics.map(stat => (
                    <div key={stat.type} className="bg-white p-4 rounded shadow">
                        <h3 className="text-lg font-semibold text-gray-700">{stat.type}</h3>
                        <p className="text-3xl font-bold text-blue-600">{stat.count}</p>
                        <p className="text-sm text-gray-500">Guests</p>
                    </div>
                ))}
            </div>

            <div className="flex justify-between mb-4">
                <h2 className="text-2xl font-bold">Guests</h2>
                <button 
                    onClick={() => setCurrentGuest({ name: '', email: '', roomId: null })}
                    className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
                >
                    Add Guest
                </button>
            </div>

            {currentGuest && (
                <form onSubmit={handleSubmit} className="mb-6 bg-white p-4 rounded shadow">
                    <div className="grid gap-4">
                        <div>
                            <label className="block mb-1">Name</label>
                            <input
                                type="text"
                                value={currentGuest.name}
                                onChange={e => setCurrentGuest({...currentGuest, name: e.target.value})}
                                className="border p-2 rounded w-full"
                                required
                            />
                        </div>
                        <div>
                            <label className="block mb-1">Email</label>
                            <input
                                type="email"
                                value={currentGuest.email}
                                onChange={e => setCurrentGuest({...currentGuest, email: e.target.value})}
                                className="border p-2 rounded w-full"
                                required
                            />
                        </div>
                        <div>
                            <label className="block mb-1">Assign Room (Optional)</label>
                            <select
                                value={currentGuest.roomId || ''}
                                onChange={e => setCurrentGuest({...currentGuest, roomId: e.target.value ? Number(e.target.value) : null})}
                                className="border p-2 rounded w-full"
                            >
                                <option value="">Not Assigned</option>
                                {rooms
                                    .filter(room => isRoomAvailableForAssignment(room, currentGuest.roomId))
                                    .map(room => (
                                        <option key={room.id} value={room.id}>
                                            {room.number} - {room.type} (${room.price})
                                        </option>
                                    ))
                                }
                            </select>
                        </div>
                        <div className="flex gap-2">
                            <button type="submit" className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600">
                                {currentGuest.id ? 'Update' : 'Create'}
                            </button>
                            <button 
                                type="button" 
                                onClick={() => setCurrentGuest(null)}
                                className="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600"
                            >
                                Cancel
                            </button>
                        </div>
                    </div>
                </form>
            )}

            <div className="bg-white shadow rounded">
                <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                        <tr>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Name</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Email</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Assigned Room</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Actions</th>
                        </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                        {guests.map(guest => (
                            <tr key={guest.id}>
                                <td className="px-6 py-4 whitespace-nowrap">{guest.name}</td>
                                <td className="px-6 py-4 whitespace-nowrap">{guest.email}</td>
                                <td className="px-6 py-4 whitespace-nowrap">
                                    {guest.roomNumber && guest.roomType ? (
                                        <div className="flex flex-col">
                                            <span className="font-medium">Room {guest.roomNumber}</span>
                                            <span className="text-sm text-gray-500">{guest.roomType}</span>
                                        </div>
                                    ) : (
                                        <select
                                            value={guest.roomId || ''}
                                            onChange={(e) => handleAssignRoom(guest.id, e.target.value)}
                                            className="border rounded p-1 bg-yellow-50"
                                        >
                                            <option value="">Not Assigned</option>
                                            {rooms
                                                .filter(room => isRoomAvailableForAssignment(room, null))
                                                .map(room => (
                                                    <option key={room.id} value={room.id}>
                                                        {room.number} - {room.type} (${room.price})
                                                    </option>
                                                ))
                                            }
                                        </select>
                                    )}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                                    <button
                                        onClick={() => setCurrentGuest(guest)}
                                        className="text-indigo-600 hover:text-indigo-900 mr-4"
                                    >
                                        Edit
                                    </button>
                                    <button
                                        onClick={() => handleDelete(guest.id)}
                                        className="text-red-600 hover:text-red-900"
                                    >
                                        Delete
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default GuestList;