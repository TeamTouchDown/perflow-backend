package com.touchdown.perflowbackend.employee.command.domain.repository;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;

import java.util.Optional;

public interface EmployeeCommandRepository {

    Employee save(Employee newEmployee);

    Optional<Employee> findById(String empId);
}
