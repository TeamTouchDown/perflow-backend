package com.touchdown.perflowbackend.employee.command.domain.repository;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;

import java.util.Optional;

import java.util.List;

public interface EmployeeCommandRepository {

    Employee save(Employee newEmployee);

    Optional<Employee> findById(String empId);

    List<Employee> findAll(); // 사원 목록 조회

}
