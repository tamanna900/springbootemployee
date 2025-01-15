package com.example.em_project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EmpControllerTest {
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new EmpController(employeeRepository))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Test
    public void testUpdateEmployee() throws Exception {
        Long employeeId = 1L;


        Employee existingEmployee = new Employee(employeeId, "John", "Doe", "john.doe@example.com");


        String updatePayload = """
            {
                "firstname": "Johnathan",
                "lastname": "Doe",
                "email": "john.doe@example.com"
            }
        """;

        Employee updatedEmployee = new Employee(employeeId, "Johnathan", "Doe", "john.doe@example.com");


        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);

        // Perform PUT request
        mockMvc.perform(put("/api/employees/{id}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatePayload))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee updated successfully"));

        // Verify repository interactions
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    public void testDeleteEmployee() throws Exception {
        Long employeeId = 1L;

        when(employeeRepository.existsById(employeeId)).thenReturn(true);

        mockMvc.perform(delete("/api/employees/{id}", employeeId))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee deleted successfully"));

        verify(employeeRepository, times(1)).existsById(employeeId);
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }

    @Test
    public void testGetAllEmployees() throws Exception {
        Employee employee1 = new Employee(1L, "John", "Doe", "john.doe@example.com");
        Employee employee2 = new Employee(2L, "Jane", "Doe", "jane.doe@example.com");

        when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee1, employee2));

        mockMvc.perform(get("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstname").value("John"))
                .andExpect(jsonPath("$[1].firstname").value("Jane"));

        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    public void testGetEmployeeById_Success() throws Exception {
        Long employeeId = 1L;

        Employee employee = new Employee(employeeId, "John", "Doe", "john.doe@example.com");

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));


        mockMvc.perform(get("/api/employees/{id}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Expect HTTP status 200 OK
                .andExpect(jsonPath("$.id").value(employeeId)) // Validate employee ID
                .andExpect(jsonPath("$.firstname").value("John")) // Validate employee first name
                .andExpect(jsonPath("$.lastname").value("Doe")) // Validate employee last name
                .andExpect(jsonPath("$.email").value("john.doe@example.com")); // Validate employee email


        verify(employeeRepository, times(1)).findById(employeeId);
    }

    @RestControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(ResponseStatusException.class)
        public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
            HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value()); // Convert HttpStatusCode to HttpStatus
            if (status == null) {
                status = HttpStatus.INTERNAL_SERVER_ERROR; // Fallback to 500 if status is null
            }
            return new ResponseEntity<>(ex.getReason(), status);
        }
    }
    @Test
    public void testGetEmployeeById_NotFound() throws Exception {
        Long employeeId = 1L;


        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());


        mockMvc.perform(get("/api/employees/{id}", employeeId)
                        .contentType("application/json"))
                .andExpect(status().isNotFound());

        verify(employeeRepository, times(1)).findById(employeeId);

    }


    @Test
    public void testPutEmployee() throws Exception {
        Long employeeId = 1L;

        Employee existingEmployee = new Employee(employeeId, "John", "Doe", "john.doe@example.com");

        String updatePayload = """
            {
                "firstname": "Johnathan",
                "lastname": "Doe",
                "email": "john.doe@example.com"
            }
        """;

        Employee updatedEmployee = new Employee(employeeId, "Johnathan", "Doe", "john.doe@example.com");

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);

        mockMvc.perform(put("/api/employees/{id}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatePayload))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee updated successfully"));

        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

}



