package funpodium.employee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import funpodium.employee.model.Employee;
import funpodium.employee.repository.EmployeeRepository;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getEmployeeById(@PathVariable Long id) {
        // Find the employee by ID
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);

        // Check if the employee was found
        if (optionalEmployee.isPresent()) {
            // If the employee is found, return it with a 200 OK status
            return ResponseEntity.ok(optionalEmployee.get());
        } else {
            // If the employee is not found, return a 404 Not Found status with a custom message
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Employee with ID " + id + " was not found.");
        }
    }

    @PostMapping
    public Employee addEmployee(@RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateEmployee(@PathVariable Long id, @RequestBody Employee updatedEmployee) {
        // Fetch the existing employee from the database
        Optional<Employee> optionalExistingEmployee = employeeRepository.findById(id);

        // Check if the employee exists in the database
        if (optionalExistingEmployee.isPresent()) {
            Employee existingEmployee = optionalExistingEmployee.get();

            // Update the properties of the existing employee
            existingEmployee.setName(updatedEmployee.getName());
            existingEmployee.setDepartment(updatedEmployee.getDepartment());
            existingEmployee.setEmail(updatedEmployee.getEmail());

            // Save the updated employee
            Employee savedEmployee = employeeRepository.save(existingEmployee);

            // Return a success response with the updated employee
            return ResponseEntity.ok(savedEmployee);
        } else {
            // If the employee does not exist, return a 404 Not Found status with a custom message
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Employee with ID " + id + " was not found.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        // Check if the employee with the given ID exists in the database
        if (employeeRepository.existsById(id)) {
            // If the employee exists, delete it
            employeeRepository.deleteById(id);
            // Return a success response with a custom message
            return ResponseEntity.ok("Employee with ID " + id + " has been deleted successfully.");
        } else {
            // If the employee does not exist, return a 404 Not Found status with a custom message
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Employee with ID " + id + " was not found.");
        }
    }
}