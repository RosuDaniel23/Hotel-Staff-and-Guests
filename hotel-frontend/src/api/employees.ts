import http from './http'
import type { Employee } from '@/types'
export const listEmployees = () => http.get<Employee[]>('/employees')
export const getEmployee = (id: number) => http.get<Employee>(`/employees/${id}`)
export const createEmployee = (d: Employee) => http.post<Employee>('/employees', d)
export const updateEmployee = (id: number, d: Employee) => http.put<Employee>(`/employees/${id}`, d)
export const deleteEmployee = (id: number) => http.delete<void>(`/employees/${id}`)
