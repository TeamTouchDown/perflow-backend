package com.touchdown.perflowbackend.employee.command.domain.repository;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EmployeeCommandRepository {

    Employee save(Employee newEmployee);

    Optional<Employee> findById(String empId);

    @Query("SELECT e FROM Employee e WHERE e.empId IN :empIds")
    List<Employee> findAllById(@Param("empIds") Set<String> empIds);
}
