package com.example.em_project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")  // Base path for all endpoints
public class EmpController {

    private final EmployeeRepository employeeRepository;

    // Inject EmployeeRepository
    @Autowired
    public EmpController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        System.out.println("Employees returned: " + employees); // Debugging
        return employees;
    }

    @PostMapping("/employees")
    public String createEmployee(@RequestBody Employee employee) {
        employeeRepository.save(employee);
        return "Employee saved successfully";
    }
}