package com.touchdown.perflowbackend.perfomance.command.domain.repository;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.Weight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WeightCommandRepository extends JpaRepository<Weight, Long> {

    @Query("SELECT w FROM Weight w WHERE w.dept.departmentId = :deptId")
    Weight findWeightByDeptId(@Param("deptId") Long deptId);
}
