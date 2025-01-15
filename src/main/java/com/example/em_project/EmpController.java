package com.example.em_project;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.RequestEntity.put;
import static org.springframework.http.ResponseEntity.status;

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
    @GetMapping("/employees/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {

        Employee employee = employeeRepository.findById(id).orElse(null);

        if (employee == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
        }

        System.out.println("Employee returned: " + employee); // Debugging
        return employee;
    }


    @PostMapping("/employees")
    public String createEmployee(@RequestBody Employee employee) {
        employeeRepository.save(employee);
        return "Employee saved successfully";
    }
    @PutMapping("/employees/{id}")
    public String updateEmployee(@PathVariable Long id, @RequestBody Employee updatedEmployee) {
        return employeeRepository.findById(id)
                .map(employee -> {
                    employee.setFirstname(updatedEmployee.getFirstname());
                    employee.setLastname(updatedEmployee.getLastname());
                    employeeRepository.save(employee);
                    return "Employee updated successfully";
                })
                .orElse("Employee not found");
    }
    @DeleteMapping("/employees/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return "Employee deleted successfully";
        } else {
            return "Employee not found";
        }
    }



}