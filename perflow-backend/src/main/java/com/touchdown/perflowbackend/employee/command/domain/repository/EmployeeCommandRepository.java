package com.touchdown.perflowbackend.employee.command.domain.repository;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;

public interface EmployeeCommandRepository {

    Employee save(Employee newEmployee);
}
