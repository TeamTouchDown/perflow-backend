package com.touchdown.perflowbackend.workAttitude.query.repository;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Travel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkAttributeTravelQueryRepository extends JpaRepository<Travel, Long> {
    // 특정 상태의 Travel 조회
    List<Travel> findAllByTravelStatus(Status travelStatus);

    // 특정 사원의 Travel 조회 (명시적 쿼리 작성) 굳이긴 한데...가 아닌가? 일단 지피티는 써주는게 조금 에러가 없다고 하던데
    @Query("SELECT t FROM Travel t WHERE t.employee = :empId")
    List<Travel> findAllByEmpId(@Param("empId") Employee empId);


}
