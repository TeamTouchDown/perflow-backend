package com.touchdown.perflowbackend.perfomance.command.domain.repository;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.Perfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerfoCommandRepository extends JpaRepository<Perfo, Long> {

    List<Perfo> findByPerfoEmp_EmpIdAndPerfoedEmp_EmpId(String perfoEmpId, String perfoedEmpId);
}
