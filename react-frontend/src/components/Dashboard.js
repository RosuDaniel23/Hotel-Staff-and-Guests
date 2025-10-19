import React, { useState, useEffect } from 'react';
import { roomApi, employeeApi, guestApi } from '../api/endpoints';

const Dashboard = () => {
    const [stats, setStats] = useState({
        rooms: { total: 0, available: 0, booked: 0, maintenance: 0 },
        employees: [],
        guests: []
    });
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        loadDashboardData();
    }, []);

    const loadDashboardData = async () => {
        try {
            setLoading(true);
            const [rooms, employeeStats, guestStats] = await Promise.all([
                roomApi.getAll(),
                employeeApi.getAnalytics(),
                guestApi.getAnalytics()
            ]);

            // Handle paginated response from backend
            const roomData = rooms.data?.content || rooms.data || [];

            // Calculate room statistics based on status field
            const roomStats = {
                total: roomData.length,
                available: roomData.filter(r => r.status === 'AVAILABLE').length,
                booked: roomData.filter(r => r.status === 'BOOKED').length,
                maintenance: roomData.filter(r => r.status === 'MAINTENANCE').length
            };

            // Map backend property names (cnt) to frontend names (count)
            const employees = (employeeStats.data || []).map(stat => ({
                role: stat.role,
                count: stat.cnt || stat.count || 0
            }));

            const guests = (guestStats.data || []).map(stat => ({
                type: stat.type,
                count: stat.cnt || stat.count || 0
            }));

            setStats({
                rooms: roomStats,
                employees: employees,
                guests: guests
            });
        } catch (err) {
            console.error('Dashboard data fetch error:', err);
            setError(err.message || 'Failed to load dashboard data');
        } finally {
            setLoading(false);
        }
    };

    if (loading) return <div className="text-center">Loading dashboard data...</div>;
    if (error) return <div className="text-red-600">{error}</div>;

    return (
        <div className="space-y-6">
            <h1 className="text-3xl font-bold">Dashboard</h1>
            
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
                <div className="bg-white p-6 rounded-lg shadow">
                    <h3 className="text-lg font-semibold text-gray-700">Total Rooms</h3>
                    <p className="text-3xl font-bold text-blue-600">{stats.rooms.total}</p>
                </div>
                <div className="bg-white p-6 rounded-lg shadow">
                    <h3 className="text-lg font-semibold text-gray-700">Available Rooms</h3>
                    <p className="text-3xl font-bold text-green-600">{stats.rooms.available}</p>
                </div>
                <div className="bg-white p-6 rounded-lg shadow">
                    <h3 className="text-lg font-semibold text-gray-700">Booked Rooms</h3>
                    <p className="text-3xl font-bold text-red-600">{stats.rooms.booked}</p>
                </div>
                <div className="bg-white p-6 rounded-lg shadow">
                    <h3 className="text-lg font-semibold text-gray-700">In Maintenance</h3>
                    <p className="text-3xl font-bold text-yellow-600">{stats.rooms.maintenance}</p>
                </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div className="bg-white p-6 rounded-lg shadow">
                    <h2 className="text-xl font-semibold mb-4">Employee Distribution</h2>
                    <div className="space-y-4">
                        {stats.employees.map(stat => (
                            <div key={stat.role} className="flex justify-between items-center">
                                <span className="text-gray-600">{stat.role}</span>
                                <span className="text-lg font-semibold">{stat.count}</span>
                            </div>
                        ))}
                    </div>
                </div>

                <div className="bg-white p-6 rounded-lg shadow">
                    <h2 className="text-xl font-semibold mb-4">Guest Distribution by Room Type</h2>
                    <div className="space-y-4">
                        {stats.guests.map(stat => (
                            <div key={stat.type} className="flex justify-between items-center">
                                <span className="text-gray-600">{stat.type}</span>
                                <span className="text-lg font-semibold">{stat.count}</span>
                            </div>
                        ))}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Dashboard;