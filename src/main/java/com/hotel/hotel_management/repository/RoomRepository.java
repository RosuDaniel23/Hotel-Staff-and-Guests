package com.hotel.hotel_management.repository;

import com.hotel.hotel_management.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
    boolean existsByNumber(String number);
}
