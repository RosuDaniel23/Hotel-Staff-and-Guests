package com.hotel.hotel_management.web;

import com.hotel.hotel_management.domain.Room;
import com.hotel.hotel_management.service.RoomService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomService service;
    public RoomController(RoomService service) { this.service = service; }

    @PostMapping public Room create(@RequestBody Room r) { return service.create(r); }
    @GetMapping public List<Room> all() { return service.all(); }
    @GetMapping("/{id}") public Room get(@PathVariable Long id) { return service.get(id); }
    @PutMapping("/{id}") public Room update(@PathVariable Long id, @RequestBody Room r){ return service.update(id, r); }
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id){ service.delete(id); }
}
