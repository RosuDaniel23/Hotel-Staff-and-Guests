import React, { useState, useEffect } from 'react';
import { roomApi } from '../../api/endpoints';

const RoomList = () => {
    const [rooms, setRooms] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [currentRoom, setCurrentRoom] = useState(null);
    const [analytics, setAnalytics] = useState([]);

    useEffect(() => {
        loadRooms();
    }, []);

    const loadRooms = async () => {
        try {
            setLoading(true);
            const response = await roomApi.getAll();
            // Handle paginated response - backend returns a Page object with content property
            const content = response.data?.content || response.data || [];
            // Ensure content is an array or fallback to an empty array
            setRooms(Array.isArray(content) ? content : []);
            setError(null);
        } catch (err) {
            setError(err.message);
            setRooms([]);
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm('Are you sure you want to delete this room?')) return;
        try {
            await roomApi.delete(id);
            await loadRooms();
        } catch (err) {
            setError(err.message);
        }
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            if (currentRoom.id) {
                await roomApi.update(currentRoom.id, currentRoom);
            } else {
                await roomApi.create(currentRoom);
            }
            setCurrentRoom(null);
            await loadRooms();
        } catch (err) {
            setError(err.message);
        }
    };

    const handleStatusChange = async (roomId, newStatus) => {
        try {
            await roomApi.updateStatus(roomId, newStatus);
            await loadRooms();
        } catch (err) {
            setError(err.message);
        }
    };

    const getStatusBadgeColor = (status) => {
        switch(status) {
            case 'AVAILABLE': return 'bg-green-100 text-green-800';
            case 'BOOKED': return 'bg-red-100 text-red-800';
            case 'MAINTENANCE': return 'bg-yellow-100 text-yellow-800';
            default: return 'bg-gray-100 text-gray-800';
        }
    };

    if (loading) return <div className="text-center">Loading...</div>;
    if (error) return <div className="text-red-600">{error}</div>;

    return (
        <div>
            <div className="flex justify-between mb-4">
                <h2 className="text-2xl font-bold">Rooms</h2>
                <button 
                    onClick={() => setCurrentRoom({ number: '', type: 'SINGLE', price: 0, status: 'AVAILABLE' })}
                    className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
                >
                    Add Room
                </button>
            </div>

            {currentRoom && (
                <form onSubmit={handleSubmit} className="mb-6 bg-white p-4 rounded shadow">
                    <div className="grid gap-4">
                        <div>
                            <label className="block mb-1">Room Number</label>
                            <input
                                type="text"
                                value={currentRoom.number}
                                onChange={e => setCurrentRoom({...currentRoom, number: e.target.value})}
                                className="border p-2 rounded w-full"
                                required
                            />
                        </div>
                        <div>
                            <label className="block mb-1">Type</label>
                            <select
                                value={currentRoom.type}
                                onChange={e => setCurrentRoom({...currentRoom, type: e.target.value})}
                                className="border p-2 rounded w-full"
                                required
                            >
                                <option value="SINGLE">Single</option>
                                <option value="DOUBLE">Double</option>
                                <option value="SUITE">Suite</option>
                            </select>
                        </div>
                        <div>
                            <label className="block mb-1">Price</label>
                            <input
                                type="number"
                                value={currentRoom.price}
                                onChange={e => setCurrentRoom({...currentRoom, price: Number(e.target.value)})}
                                className="border p-2 rounded w-full"
                                required
                            />
                        </div>
                        <div>
                            <label className="block mb-1">Status</label>
                            <select
                                value={currentRoom.status || 'AVAILABLE'}
                                onChange={e => setCurrentRoom({...currentRoom, status: e.target.value})}
                                className="border p-2 rounded w-full"
                                required
                            >
                                <option value="AVAILABLE">Available</option>
                                <option value="BOOKED">Booked</option>
                                <option value="MAINTENANCE">Maintenance</option>
                            </select>
                        </div>
                        <div className="flex gap-2">
                            <button type="submit" className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600">
                                {currentRoom.id ? 'Update' : 'Create'}
                            </button>
                            <button 
                                type="button" 
                                onClick={() => setCurrentRoom(null)}
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
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Number</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Type</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Price</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Actions</th>
                        </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                        {Array.isArray(rooms) && rooms.map(room => (
                            <tr key={room.id}>
                                <td className="px-6 py-4 whitespace-nowrap">{room.number}</td>
                                <td className="px-6 py-4 whitespace-nowrap">{room.type}</td>
                                <td className="px-6 py-4 whitespace-nowrap">${room.price}</td>
                                <td className="px-6 py-4 whitespace-nowrap">
                                    <select
                                        value={room.status || 'AVAILABLE'}
                                        onChange={(e) => handleStatusChange(room.id, e.target.value)}
                                        className={`px-2 py-1 rounded text-xs font-semibold ${getStatusBadgeColor(room.status)}`}
                                    >
                                        <option value="AVAILABLE">Available</option>
                                        <option value="BOOKED">Booked</option>
                                        <option value="MAINTENANCE">Maintenance</option>
                                    </select>
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                                    <button
                                        onClick={() => setCurrentRoom(room)}
                                        className="text-indigo-600 hover:text-indigo-900 mr-4"
                                    >
                                        Edit
                                    </button>
                                    <button
                                        onClick={() => handleDelete(room.id)}
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

export default RoomList;