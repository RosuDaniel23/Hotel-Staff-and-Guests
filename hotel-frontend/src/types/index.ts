export interface Room {
    id?: number
    number: string
    type: string
    price: number
    status: 'AVAILABLE' | 'BOOKED' | 'MAINTENANCE'
}
export interface Employee {
    id?: number
    name: string
    role: 'MANAGER' | 'RECEPTIONIST' | 'HOUSEKEEPING' | 'OTHER'
    email: string
}
export interface Guest {
    id?: number
    name: string
    email: string
    phone?: string
}
