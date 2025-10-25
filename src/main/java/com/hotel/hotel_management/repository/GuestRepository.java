package com.hotel.hotel_management.repository;

import com.hotel.hotel_management.domain.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GuestRepository extends JpaRepository<Guest, Long> {
    interface TypeCount { String getType(); long getCnt(); }

    @Query("""
           select r.type as type, count(g) as cnt
           from Guest g join g.room r
           group by r.type
           """)
    List<TypeCount> countGuestsPerRoomType();

    List<Guest> findByRoom_Id(Long roomId);

    Optional<Guest> findByEmail(String email);
}
