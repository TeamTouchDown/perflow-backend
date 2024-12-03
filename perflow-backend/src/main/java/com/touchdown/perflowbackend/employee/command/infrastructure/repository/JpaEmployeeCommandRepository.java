package com.touchdown.perflowbackend.employee.command.infrastructure.repository;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaEmployeeCommandRepository extends JpaRepository<Employee, String>, EmployeeCommandRepository {

}
