package com.hotel.hotel_management.web;

import com.hotel.hotel_management.domain.Employee;
import com.hotel.hotel_management.dto.EmployeeDtos;
import com.hotel.hotel_management.repository.EmployeeRepository;
import com.hotel.hotel_management.service.EmployeeService;
import com.hotel.hotel_management.util.Mappers;
import jakarta.validation.Valid;
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

    @PostMapping
    public EmployeeDtos.Response create(@Valid @RequestBody EmployeeDtos.Create in){
        Employee e = new Employee();
        e.setName(in.getName());
        e.setRole(in.getRole());
        e.setEmail(in.getEmail());
        return Mappers.toResponse(service.create(e));
    }

    @GetMapping
    public List<EmployeeDtos.Response> all(){
        return service.all().stream().map(Mappers::toResponse).toList();
    }

    @GetMapping("/{id}")
    public EmployeeDtos.Response get(@PathVariable Long id){
        return Mappers.toResponse(service.get(id));
    }

    @PutMapping("/{id}")
    public EmployeeDtos.Response update(@PathVariable Long id, @Valid @RequestBody EmployeeDtos.Update in){
        Employee e = new Employee();
        e.setName(in.getName());
        e.setRole(in.getRole());
        e.setEmail(in.getEmail());
        return Mappers.toResponse(service.update(id, e));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){ service.delete(id); }

    @GetMapping("/analytics/by-role")
    public List<EmployeeRepository.RoleCount> countByRole() { return repo.countByRole(); }
}
