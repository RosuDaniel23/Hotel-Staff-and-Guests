package com.hotel.hotel_management.service;

import com.hotel.hotel_management.domain.Room;
import com.hotel.hotel_management.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {
    private final RoomRepository repo;
    public RoomService(RoomRepository repo) { this.repo = repo; }

    public Room create(Room r) { return repo.save(r); }
    public List<Room> all() { return repo.findAll(); }
    public Room get(Long id) { return repo.findById(id).orElseThrow(); }
    public Room update(Long id, Room in) {
        Room r = get(id);
        r.setNumber(in.getNumber());
        r.setType(in.getType());
        r.setPrice(in.getPrice());
        return repo.save(r);
    }
    public void delete(Long id) { repo.deleteById(id); }
}
