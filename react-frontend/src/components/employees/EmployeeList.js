import React, { useState, useEffect } from 'react';
import { employeeApi } from '../../api/endpoints';

const EmployeeList = () => {
    const [employees, setEmployees] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [currentEmployee, setCurrentEmployee] = useState(null);
    const [analytics, setAnalytics] = useState(null);

    useEffect(() => {
        loadEmployees();
        loadAnalytics();
    }, []);

    const loadEmployees = async () => {
        try {
            setLoading(true);
            const response = await employeeApi.getAll();
            setEmployees(response.data);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    const loadAnalytics = async () => {
        try {
            const response = await employeeApi.getAnalytics();
            // Map analytics to handle backend property names (cnt -> count)
            const analyticsData = (response.data || []).map(stat => ({
                role: stat.role,
                count: stat.cnt || stat.count || 0
            }));
            setAnalytics(analyticsData);
        } catch (err) {
            console.error('Failed to load analytics:', err);
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm('Are you sure you want to delete this employee?')) return;
        try {
            await employeeApi.delete(id);
            await loadEmployees();
            await loadAnalytics();
        } catch (err) {
            setError(err.message);
        }
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            if (currentEmployee.id) {
                await employeeApi.update(currentEmployee.id, {
                    name: currentEmployee.name,
                    email: currentEmployee.email,
                    role: currentEmployee.role
                });
            } else {
                await employeeApi.create({
                    name: currentEmployee.name,
                    email: currentEmployee.email,
                    role: currentEmployee.role
                });
            }
            setCurrentEmployee(null);
            await loadEmployees();
            await loadAnalytics();
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
                    <div key={stat.role} className="bg-white p-4 rounded shadow">
                        <h3 className="text-lg font-semibold text-gray-700">{stat.role}</h3>
                        <p className="text-3xl font-bold text-blue-600">{stat.count}</p>
                        <p className="text-sm text-gray-500">Employees</p>
                    </div>
                ))}
            </div>

            <div className="flex justify-between mb-4">
                <h2 className="text-2xl font-bold">Employees</h2>
                <button 
                    onClick={() => setCurrentEmployee({ name: '', email: '', role: 'RECEPTIONIST' })}
                    className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
                >
                    Add Employee
                </button>
            </div>

            {currentEmployee && (
                <form onSubmit={handleSubmit} className="mb-6 bg-white p-4 rounded shadow">
                    <div className="grid gap-4">
                        <div>
                            <label className="block mb-1">Name</label>
                            <input
                                type="text"
                                value={currentEmployee.name}
                                onChange={e => setCurrentEmployee({...currentEmployee, name: e.target.value})}
                                className="border p-2 rounded w-full"
                                required
                            />
                        </div>
                        <div>
                            <label className="block mb-1">Email</label>
                            <input
                                type="email"
                                value={currentEmployee.email}
                                onChange={e => setCurrentEmployee({...currentEmployee, email: e.target.value})}
                                className="border p-2 rounded w-full"
                                required
                            />
                        </div>
                        <div>
                            <label className="block mb-1">Role</label>
                            <select
                                value={currentEmployee.role}
                                onChange={e => setCurrentEmployee({...currentEmployee, role: e.target.value})}
                                className="border p-2 rounded w-full"
                            >
                                <option>RECEPTIONIST</option>
                                <option>HOUSEKEEPER</option>
                                <option>MANAGER</option>
                            </select>
                        </div>
                        <div className="flex gap-2">
                            <button type="submit" className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600">
                                {currentEmployee.id ? 'Update' : 'Create'}
                            </button>
                            <button 
                                type="button" 
                                onClick={() => setCurrentEmployee(null)}
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
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Role</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Actions</th>
                        </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                        {employees.map(employee => (
                            <tr key={employee.id}>
                                <td className="px-6 py-4 whitespace-nowrap">{employee.name}</td>
                                <td className="px-6 py-4 whitespace-nowrap">{employee.email}</td>
                                <td className="px-6 py-4 whitespace-nowrap">
                                    <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full 
                                        ${employee.role === 'MANAGER' ? 'bg-purple-100 text-purple-800' : 
                                          employee.role === 'RECEPTIONIST' ? 'bg-blue-100 text-blue-800' : 
                                          'bg-green-100 text-green-800'}`}>
                                        {employee.role}
                                    </span>
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                                    <button
                                        onClick={() => setCurrentEmployee(employee)}
                                        className="text-indigo-600 hover:text-indigo-900 mr-4"
                                    >
                                        Edit
                                    </button>
                                    <button
                                        onClick={() => handleDelete(employee.id)}
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

export default EmployeeList;