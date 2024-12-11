package com.touchdown.perflowbackend.hr.command.domain.repository;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Pic;

public interface PicCommandRepository {

    Pic save(Pic pic);

    boolean existsByDepartment(Department newDepartment);
}
