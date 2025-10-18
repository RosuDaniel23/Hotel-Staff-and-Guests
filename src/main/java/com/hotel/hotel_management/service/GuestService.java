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
    public void delete(Long id) { guests.deleteById(id); }

    public Guest assignToRoom(Long guestId, Long roomId) {
        Guest g = get(guestId);
        Room r = rooms.findById(roomId).orElseThrow();
        g.setRoom(r);
        return guests.save(g);
    }

    public List<Guest> byRoom(Long roomId) { return guests.findByRoom_Id(roomId); }
}
