package com.touchdown.perflowbackend.hr.command.infrastructure.repository;

import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.repository.DepartmentCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaDepartmentCommandRepository extends JpaRepository<Department, Long>, DepartmentCommandRepository {

}
