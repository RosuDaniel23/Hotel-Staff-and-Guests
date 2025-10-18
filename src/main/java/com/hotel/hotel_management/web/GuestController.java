package com.hotel.hotel_management.web;

import com.hotel.hotel_management.dto.GuestDtos;
import com.hotel.hotel_management.repository.GuestRepository;
import com.hotel.hotel_management.service.GuestService;
import com.hotel.hotel_management.util.Mappers;
import jakarta.validation.Valid;
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

    @PostMapping
    public GuestDtos.Response create(@Valid @RequestBody GuestDtos.Create in){
        return Mappers.toResponse(service.createFromDto(in.getName(), in.getEmail(), in.getRoomId()));
    }

    @GetMapping
    public List<GuestDtos.Response> all(){
        return service.all().stream().map(Mappers::toResponse).toList();
    }

    @GetMapping("/{id}")
    public GuestDtos.Response get(@PathVariable Long id){
        return Mappers.toResponse(service.get(id));
    }

    @PutMapping("/{id}")
    public GuestDtos.Response update(@PathVariable Long id, @Valid @RequestBody GuestDtos.Update in){
        return Mappers.toResponse(service.updateFromDto(id, in.getName(), in.getEmail(), in.getRoomId()));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){ service.delete(id); }

    @PutMapping("/{guestId}/assign/{roomId}")
    public GuestDtos.Response assign(@PathVariable Long guestId, @PathVariable Long roomId){
        return Mappers.toResponse(service.assignToRoom(guestId, roomId));
    }

    @GetMapping("/by-room/{roomId}")
    public List<GuestDtos.Response> byRoom(@PathVariable Long roomId){
        return service.byRoom(roomId).stream().map(Mappers::toResponse).toList();
    }

    @GetMapping("/analytics/by-room-type")
    public List<GuestRepository.TypeCount> guestsPerRoomType() {
        return repo.countGuestsPerRoomType();
    }
}
