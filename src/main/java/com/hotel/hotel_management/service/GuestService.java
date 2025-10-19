package com.hotel.hotel_management.service;

import com.hotel.hotel_management.domain.Guest;
import com.hotel.hotel_management.domain.Room;
import com.hotel.hotel_management.repository.GuestRepository;
import com.hotel.hotel_management.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestService {
    private final GuestRepository guests;
    private final RoomRepository rooms;

    public GuestService(GuestRepository guests, RoomRepository rooms) {
        this.guests = guests; this.rooms = rooms;
    }

    public Guest create(Guest g) { return guests.save(g); }
    public List<Guest> all() { return guests.findAll(); }
    public Guest get(Long id) { return guests.findById(id).orElseThrow(); }
    public Guest update(Long id, Guest in) {
        Guest g = get(id);
        g.setName(in.getName());
        g.setEmail(in.getEmail());
        return guests.save(g);
    }
    public void delete(Long id) { 
        Guest g = get(id);
        // If guest had a room, set it back to AVAILABLE
        if (g.getRoom() != null) {
            Room room = g.getRoom();
            room.setStatus("AVAILABLE");
            rooms.save(room);
        }
        guests.deleteById(id); 
    }

    public Guest assignToRoom(Long guestId, Long roomId) {
        Guest g = get(guestId);
        
        // If guest had a previous room, set it back to AVAILABLE
        if (g.getRoom() != null) {
            Room oldRoom = g.getRoom();
            oldRoom.setStatus("AVAILABLE");
            rooms.save(oldRoom);
        }
        
        // Assign new room and set it to BOOKED
        Room r = rooms.findById(roomId).orElseThrow();
        r.setStatus("BOOKED");
        rooms.save(r);
        g.setRoom(r);
        return guests.save(g);
    }

    public Guest createFromDto(String name, String email, Long roomId) {
        Guest g = new Guest();
        g.setName(name);
        g.setEmail(email);
        if (roomId != null) {
            Room r = rooms.findById(roomId).orElseThrow();
            r.setStatus("BOOKED");
            rooms.save(r);
            g.setRoom(r);
        }
        return guests.save(g);
    }

    public Guest updateFromDto(Long id, String name, String email, Long roomId) {
        Guest g = get(id);
        
        // If room is being changed
        if (g.getRoom() != null && (roomId == null || !g.getRoom().getId().equals(roomId))) {
            // Set old room to AVAILABLE
            Room oldRoom = g.getRoom();
            oldRoom.setStatus("AVAILABLE");
            rooms.save(oldRoom);
        }
        
        g.setName(name);
        g.setEmail(email);
        
        if (roomId != null) {
            Room r = rooms.findById(roomId).orElseThrow();
            r.setStatus("BOOKED");
            rooms.save(r);
            g.setRoom(r);
        } else {
            g.setRoom(null);
        }
        
        return guests.save(g);
    }


    public List<Guest> byRoom(Long roomId) { return guests.findByRoom_Id(roomId); }
}
