package com.hotel.hotel_management.web;

import com.hotel.hotel_management.domain.Guest;
import com.hotel.hotel_management.domain.Room;
import com.hotel.hotel_management.domain.RoomUpgradeRequest;
import com.hotel.hotel_management.repository.GuestRepository;
import com.hotel.hotel_management.repository.RoomRepository;
import com.hotel.hotel_management.repository.RoomUpgradeRequestRepository;
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

    public GuestPortalController(GuestRepository guestRepository,
                                RoomRepository roomRepository,
                                RoomUpgradeRequestRepository upgradeRequestRepository) {
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
        this.upgradeRequestRepository = upgradeRequestRepository;
    }

    @GetMapping("/{guestId}/room")
    public ResponseEntity<?> getGuestRoom(@PathVariable Long guestId) {
        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new RuntimeException("Guest not found"));

        Map<String, Object> response = new HashMap<>();
        response.put("guest", Map.of(
                "id", guest.getId(),
                "name", guest.getName(),
                "email", guest.getEmail()
        ));

        if (guest.getRoom() != null) {
            Room room = guest.getRoom();
            response.put("room", Map.of(
                    "id", room.getId(),
                    "number", room.getNumber(),
                    "type", room.getType(),
                    "price", room.getPrice().toString(),
                    "status", room.getStatus()
            ));
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

        return ResponseEntity.ok(Map.of(
                "message", "Upgrade request submitted successfully",
                "requestId", upgradeRequest.getId()
        ));
    }

    @GetMapping("/{guestId}/upgrade-requests")
    public ResponseEntity<?> getUpgradeRequests(@PathVariable Long guestId) {
        List<RoomUpgradeRequest> requests = upgradeRequestRepository.findByGuestId(guestId);

        List<Map<String, Object>> response = requests.stream()
                .map(req -> Map.of(
                        "id", req.getId(),
                        "currentRoom", Map.of(
                                "number", req.getCurrentRoom().getNumber(),
                                "type", req.getCurrentRoom().getType(),
                                "price", req.getCurrentRoom().getPrice().toString()
                        ),
                        "requestedRoom", Map.of(
                                "number", req.getRequestedRoom().getNumber(),
                                "type", req.getRequestedRoom().getType(),
                                "price", req.getRequestedRoom().getPrice().toString()
                        ),
                        "status", req.getStatus(),
                        "requestedAt", req.getRequestedAt().toString()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
