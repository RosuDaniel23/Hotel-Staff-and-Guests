package com.hotel.hotel_management.repository;

import com.hotel.hotel_management.domain.RoomUpgradeRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomUpgradeRequestRepository extends JpaRepository<RoomUpgradeRequest, Long> {
    List<RoomUpgradeRequest> findByGuestId(Long guestId);
    List<RoomUpgradeRequest> findByStatus(String status);
}

