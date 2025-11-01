package com.hotel.hotel_management.web;

import com.hotel.hotel_management.domain.Guest;
import com.hotel.hotel_management.domain.Room;
import com.hotel.hotel_management.domain.RoomUpgradeRequest;
import com.hotel.hotel_management.domain.GuestServiceRequest;
import com.hotel.hotel_management.repository.GuestRepository;
import com.hotel.hotel_management.repository.RoomRepository;
import com.hotel.hotel_management.repository.RoomUpgradeRequestRepository;
import com.hotel.hotel_management.repository.GuestServiceRequestRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/guest-portal")
@CrossOrigin(origins = "*")
public class GuestPortalController {

    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;
    private final RoomUpgradeRequestRepository upgradeRequestRepository;
    private final GuestServiceRequestRepository serviceRequestRepository;

    public GuestPortalController(GuestRepository guestRepository,
                                 RoomRepository roomRepository,
                                 RoomUpgradeRequestRepository upgradeRequestRepository,
                                 GuestServiceRequestRepository serviceRequestRepository) {
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
        this.upgradeRequestRepository = upgradeRequestRepository;
        this.serviceRequestRepository = serviceRequestRepository;
    }

    @GetMapping("/{guestId}/room")
    public ResponseEntity<?> getGuestRoom(@PathVariable Long guestId) {
        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new RuntimeException("Guest not found"));

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> guestMap = new HashMap<>();
        guestMap.put("id", guest.getId());
        guestMap.put("name", guest.getName());
        guestMap.put("email", guest.getEmail());
        response.put("guest", guestMap);

        if (guest.getRoom() != null) {
            Room room = guest.getRoom();
            Map<String, Object> roomMap = new HashMap<>();
            roomMap.put("id", room.getId());
            roomMap.put("number", room.getNumber());
            roomMap.put("type", room.getType());
            roomMap.put("price", room.getPrice().toString());
            roomMap.put("status", room.getStatus());
            response.put("room", roomMap);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/available-upgrades")
    public ResponseEntity<?> getAvailableUpgrades(@RequestParam Long guestId) {
        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new RuntimeException("Guest not found"));

        if (guest.getRoom() == null) {
            return ResponseEntity.ok(List.of());
        }

        List<Room> availableRooms = roomRepository.findAll().stream()
                .filter(r -> r.getStatus().equals("AVAILABLE"))
                .filter(r -> !r.getId().equals(guest.getRoom().getId()))
                .filter(r -> r.getPrice().compareTo(guest.getRoom().getPrice()) > 0)
                .map(r -> {
                    Room room = new Room();
                    room.setId(r.getId());
                    room.setNumber(r.getNumber());
                    room.setType(r.getType());
                    room.setPrice(r.getPrice());
                    room.setStatus(r.getStatus());
                    return room;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(availableRooms);
    }

    @PostMapping("/upgrade-request")
    public ResponseEntity<?> requestUpgrade(@RequestBody Map<String, Long> request) {
        Long guestId = request.get("guestId");
        Long requestedRoomId = request.get("requestedRoomId");

        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new RuntimeException("Guest not found"));

        if (guest.getRoom() == null) {
            throw new RuntimeException("Guest does not have a current room");
        }

        Room requestedRoom = roomRepository.findById(requestedRoomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        RoomUpgradeRequest upgradeRequest = new RoomUpgradeRequest();
        upgradeRequest.setGuest(guest);
        upgradeRequest.setCurrentRoom(guest.getRoom());
        upgradeRequest.setRequestedRoom(requestedRoom);

        upgradeRequestRepository.save(upgradeRequest);

        Map<String, Object> resp = new HashMap<>();
        resp.put("message", "Upgrade request submitted successfully");
        resp.put("requestId", upgradeRequest.getId());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{guestId}/upgrade-requests")
    public ResponseEntity<?> getUpgradeRequests(@PathVariable Long guestId) {
        List<RoomUpgradeRequest> requests = upgradeRequestRepository.findByGuestId(guestId);

        List<Map<String, Object>> response = requests.stream()
                .map(req -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", req.getId());
                    Map<String, Object> current = new HashMap<>();
                    current.put("number", req.getCurrentRoom().getNumber());
                    current.put("type", req.getCurrentRoom().getType());
                    current.put("price", req.getCurrentRoom().getPrice().toString());
                    m.put("currentRoom", current);
                    Map<String, Object> requested = new HashMap<>();
                    requested.put("number", req.getRequestedRoom().getNumber());
                    requested.put("type", req.getRequestedRoom().getType());
                    requested.put("price", req.getRequestedRoom().getPrice().toString());
                    m.put("requestedRoom", requested);
                    m.put("status", req.getStatus());
                    m.put("requestedAt", req.getRequestedAt().toString());
                    return m;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{guestId}/service-requests")
    public ResponseEntity<?> createServiceRequest(@PathVariable Long guestId, @RequestBody Map<String, String> body) {
        String type = body.getOrDefault("type", "OTHER");
        String note = body.getOrDefault("note", "");

        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new RuntimeException("Guest not found"));

        GuestServiceRequest req = new GuestServiceRequest();
        req.setGuest(guest);
        req.setType(type);
        req.setNote(note);
        serviceRequestRepository.save(req);

        Map<String, Object> resp = new HashMap<>();
        resp.put("id", req.getId());
        resp.put("message", "Service request submitted");
        resp.put("status", req.getStatus());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{guestId}/service-requests")
    public ResponseEntity<?> listServiceRequests(@PathVariable Long guestId) {
        List<GuestServiceRequest> list = serviceRequestRepository.findByGuestId(guestId);
        List<Map<String, Object>> response = list.stream()
                .map(r -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", r.getId());
                    m.put("type", r.getType());
                    m.put("note", r.getNote());
                    m.put("status", r.getStatus());
                    m.put("requestedAt", r.getRequestedAt().toString());
                    return m;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}
