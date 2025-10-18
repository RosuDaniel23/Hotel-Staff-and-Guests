package com.hotel.hotel_management.web;

import com.hotel.hotel_management.domain.Guest;
import com.hotel.hotel_management.repository.GuestRepository;
import com.hotel.hotel_management.service.GuestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guests")
public class GuestController {
    private final GuestService service;
    private final GuestRepository repo;

    public GuestController(GuestService service, GuestRepository repo) {
        this.service = service; this.repo = repo;
    }

    @PostMapping public Guest create(@RequestBody Guest g){ return service.create(g); }
    @GetMapping public List<Guest> all(){ return service.all(); }
    @GetMapping("/{id}") public Guest get(@PathVariable Long id){ return service.get(id); }
    @PutMapping("/{id}") public Guest update(@PathVariable Long id, @RequestBody Guest g){ return service.update(id, g); }
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id){ service.delete(id); }

    @PutMapping("/{guestId}/assign/{roomId}")
    public Guest assign(@PathVariable Long guestId, @PathVariable Long roomId){
        return service.assignToRoom(guestId, roomId);
    }

    @GetMapping("/by-room/{roomId}")
    public List<Guest> byRoom(@PathVariable Long roomId){ return service.byRoom(roomId); }

    @GetMapping("/analytics/by-room-type")
    public List<GuestRepository.TypeCount> guestsPerRoomType() {
        return repo.countGuestsPerRoomType();
    }
}
