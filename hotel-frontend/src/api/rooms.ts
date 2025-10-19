import http from './http'
import type { Room } from '@/types'
export const listRooms = () => http.get<Room[]>('/rooms')
export const getRoom = (id: number) => http.get<Room>(`/rooms/${id}`)
export const createRoom = (data: Room) => http.post<Room>('/rooms', data)
export const updateRoom = (id: number, data: Room) => http.put<Room>(`/rooms/${id}`, data)
export const deleteRoom = (id: number) => http.delete<void>(`/rooms/${id}`)
