package com.example.em_project;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class EmpControllerTest {

    @InjectMocks
    private EmpController empController; // Controller being tested

    @Mock
    private EmployeeRepository employeeRepository; // Mocked repository

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(empController).build();
    }
    @Test
    public void testGetAllEmployees() throws Exception {
        List<Employee> employees = Arrays.asList(
                new Employee(1L, "John", "Doe", "john.doe@example.com"),
                new Employee(2L, "Jane", "Smith", "jane.smith@example.com")
        );

        when(employeeRepository.findAll()).thenReturn(employees);

        mockMvc.perform(get("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstname").value("John"))
                .andExpect(jsonPath("$[1].lastname").value("Smith"));
    }



    @Test
    public void testCreateEmployee() throws Exception {
        // Mock employee to create
        Employee employee = new Employee(3L, "Alice", "Brown", "alice.brown@example.com");

        // Mock repository save behavior
        when(employeeRepository.save(employee)).thenReturn(employee);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(employee)))
                .andExpect(status().isOk());
    }
}
