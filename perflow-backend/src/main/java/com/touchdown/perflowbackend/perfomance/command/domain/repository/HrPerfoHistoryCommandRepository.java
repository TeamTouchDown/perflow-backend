package com.touchdown.perflowbackend.perfomance.command.domain.repository;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.HrPerfoHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HrPerfoHistoryCommandRepository extends JpaRepository<HrPerfoHistory, Long> {

    @Query("SELECT h FROM HrPerfoHistory h WHERE h.emp.empId = :empId")
    List<HrPerfoHistory> findByEmpId(@Param("empId") String empId);



}
