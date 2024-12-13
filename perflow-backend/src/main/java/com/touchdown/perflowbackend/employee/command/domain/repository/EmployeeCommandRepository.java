package com.touchdown.perflowbackend.employee.command.domain.repository;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface EmployeeCommandRepository {

    Employee save(Employee newEmployee);

    Optional<Employee> findById(String empId);
}
