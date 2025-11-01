package com.hotel.hotel_management.repository;

import com.hotel.hotel_management.domain.GuestServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GuestServiceRequestRepository extends JpaRepository<GuestServiceRequest, Long> {
    List<GuestServiceRequest> findByGuestId(Long guestId);
}

