package com.hotel.hotel_management.repository;

import com.hotel.hotel_management.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    interface RoleCount { String getRole(); long getCnt(); }

    @Query("select e.role as role, count(e) as cnt from Employee e group by e.role")
    List<RoleCount> countByRole();

    Optional<Employee> findByEmail(String email);
}
