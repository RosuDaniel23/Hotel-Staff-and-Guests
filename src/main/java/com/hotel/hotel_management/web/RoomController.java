package com.hotel.hotel_management.web;

import com.hotel.hotel_management.domain.Room;
import com.hotel.hotel_management.dto.RoomDtos;
import com.hotel.hotel_management.service.RoomService;
import com.hotel.hotel_management.util.Mappers;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomService service;
    public RoomController(RoomService service) { this.service = service; }

    @PostMapping
    public RoomDtos.Response create(@Valid @RequestBody RoomDtos.Create in) {
        Room r = new Room();
        r.setNumber(in.getNumber());
        r.setType(in.getType());
        r.setPrice(in.getPrice());
        r.setStatus(in.getStatus() != null ? in.getStatus() : "AVAILABLE");
        return Mappers.toResponse(service.create(r));
    }

    // paginare + sortare: /api/rooms?page=0&size=10&sort=price,desc
    @GetMapping
    public Page<RoomDtos.Response> all(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       @RequestParam(defaultValue = "id,asc") String[] sort) {
        Sort s = Sort.by(parseSort(sort));
        Pageable p = PageRequest.of(page, size, s);
        List<RoomDtos.Response> content = service.all().stream().map(Mappers::toResponse).toList();
        // simulăm Page din listă (poți muta în repo cu findAll(p))
        return new PageImpl<>(content, p, content.size());
    }

    @GetMapping("/{id}")
    public RoomDtos.Response get(@PathVariable Long id) {
        return Mappers.toResponse(service.get(id));
    }

    @PutMapping("/{id}")
    public RoomDtos.Response update(@PathVariable Long id, @Valid @RequestBody RoomDtos.Update in) {
        Room r = new Room();
        r.setNumber(in.getNumber());
        r.setType(in.getType());
        r.setPrice(in.getPrice());
        r.setStatus(in.getStatus() != null ? in.getStatus() : "AVAILABLE");
        return Mappers.toResponse(service.update(id, r));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){ service.delete(id); }

    @PatchMapping("/{id}/status")
    public RoomDtos.Response updateStatus(@PathVariable Long id, @RequestParam String status) {
        Room room = service.get(id);
        room.setStatus(status);
        return Mappers.toResponse(service.update(id, room));
    }

    private Sort.Order[] parseSort(String[] sort) {
        // ex: ["price,desc","number,asc"]
        return java.util.Arrays.stream(sort).map(s -> {
            String[] p = s.split(",");
            return new Sort.Order(p.length>1 && p[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, p[0]);
        }).toArray(Sort.Order[]::new);
    }
}
