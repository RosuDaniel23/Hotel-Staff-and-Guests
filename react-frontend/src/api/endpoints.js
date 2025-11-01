import http from './http';

export const authApi = {
    employeeLogin: (credentials) => http.post('auth/employee/login', credentials),
    guestLogin: (credentials) => http.post('auth/guest/login', credentials)
};

export const guestPortalApi = {
    getGuestRoom: (guestId) => http.get(`guest-portal/${guestId}/room`),
    getAvailableUpgrades: (guestId) => http.get(`guest-portal/available-upgrades?guestId=${guestId}`),
    requestUpgrade: (guestId, requestedRoomId) => http.post('guest-portal/upgrade-request', { guestId, requestedRoomId }),
    getUpgradeRequests: (guestId) => http.get(`guest-portal/${guestId}/upgrade-requests`),
    createServiceRequest: (guestId, body) => http.post(`guest-portal/${guestId}/service-requests`, body),
    listServiceRequests: (guestId) => http.get(`guest-portal/${guestId}/service-requests`)
};

export const roomApi = {
    getAll: () => http.get('rooms'),
    getById: (id) => http.get(`rooms/${id}`),
    create: (room) => http.post('rooms', room),
    update: (id, room) => http.put(`rooms/${id}`, room),
    delete: (id) => http.delete(`rooms/${id}`),
    updateStatus: (id, status) => http.patch(`rooms/${id}/status?status=${status}`)
};

export const employeeApi = {
    getAll: () => http.get('employees'),
    getById: (id) => http.get(`employees/${id}`),
    create: (employee) => http.post('employees', {
        name: employee.name,
        email: employee.email,
        role: employee.role
    }),
    update: (id, employee) => http.put(`employees/${id}`, {
        name: employee.name,
        email: employee.email,
        role: employee.role
    }),
    delete: (id) => http.delete(`employees/${id}`),
    getAnalytics: () => http.get('employees/analytics')
};

export const guestApi = {
    getAll: () => http.get('guests'),
    getById: (id) => http.get(`guests/${id}`),
    create: (guest) => http.post('guests', guest),
    update: (id, guest) => http.put(`guests/${id}`, guest),
    delete: (id) => http.delete(`guests/${id}`),
    assignRoom: (guestId, roomId) => http.put(`guests/${guestId}/assign/${roomId}`),
    getByRoom: (roomId) => http.get(`guests/by-room/${roomId}`),
    getAnalytics: () => http.get('guests/analytics')
};