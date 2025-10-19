import http from './http'
import type { Guest } from '@/types'
export const listGuests = () => http.get<Guest[]>('/guests')
export const getGuest = (id: number) => http.get<Guest>(`/guests/${id}`)
export const createGuest = (d: Guest) => http.post<Guest>('/guests', d)
export const updateGuest = (id: number, d: Guest) => http.put<Guest>(`/guests/${id}`, d)
export const deleteGuest = (id: number) => http.delete<void>(`/guests/${id}`)
