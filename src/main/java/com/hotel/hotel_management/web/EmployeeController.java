package com.hotel.hotel_management.web;

import com.hotel.hotel_management.domain.Employee;
import com.hotel.hotel_management.repository.EmployeeRepository;
import com.hotel.hotel_management.service.EmployeeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService service;
    private final EmployeeRepository repo;

    public EmployeeController(EmployeeService service, EmployeeRepository repo) {
        this.service = service; this.repo = repo;
    }

    @PostMapping public Employee create(@RequestBody Employee e){ return service.create(e); }
    @GetMapping public List<Employee> all(){ return service.all(); }
    @GetMapping("/{id}") public Employee get(@PathVariable Long id){ return service.get(id); }
    @PutMapping("/{id}") public Employee update(@PathVariable Long id, @RequestBody Employee e){ return service.update(id, e); }
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id){ service.delete(id); }

    @GetMapping("/analytics/by-role")
    public List<EmployeeRepository.RoleCount> countByRole() { return repo.countByRole(); }
}
