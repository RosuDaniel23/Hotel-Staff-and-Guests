package com.hotel.hotel_management.service;

import com.hotel.hotel_management.domain.Employee;
import com.hotel.hotel_management.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository repo;
    public EmployeeService(EmployeeRepository repo) { this.repo = repo; }

    public Employee create(Employee e) { return repo.save(e); }
    public List<Employee> all() { return repo.findAll(); }
    public Employee get(Long id) { return repo.findById(id).orElseThrow(); }
    public Employee update(Long id, Employee in) {
        Employee e = get(id);
        e.setName(in.getName());
        e.setRole(in.getRole());
        e.setEmail(in.getEmail());
        return repo.save(e);
    }
    public void delete(Long id) { repo.deleteById(id); }
}
