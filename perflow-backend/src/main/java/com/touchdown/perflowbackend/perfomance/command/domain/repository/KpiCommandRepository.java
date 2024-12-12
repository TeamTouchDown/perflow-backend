package com.touchdown.perflowbackend.perfomance.command.domain.repository;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.Kpi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface KpiCommandRepository extends JpaRepository<Kpi, Long> {

    Optional<Kpi> findByKpiId(Long kpiId);

    @Query("SELECT k FROM Kpi k WHERE k.emp.empId = :empId")
    List<Kpi> findByEmpId(@Param("empId") String empId);

//    List<Kpi> findByEmp_EmpId(String empId);

    Kpi save(Kpi kpi);

    void deleteById(Long kpiId);
}
