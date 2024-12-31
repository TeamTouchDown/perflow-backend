package com.touchdown.perflowbackend.workAttitude.query.repository;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Vacation;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeVacationResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WorkAttitudeVacationQueryRepository extends JpaRepository<Vacation, Long> {

    // 전체 휴가 내역 조회
    @Query("SELECT new com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeVacationResponseDTO(" +
            "v.vacationId, v.empId.empId, v.empId.name, v.approver.empId, v.approver.name, " +
            "v.enrollVacation, v.vacationStart, v.vacationEnd, v.vacationType, v.vacationStatus, " +
            "v.vacationRejectReason, v.status) " +
            "FROM Vacation v")
    List<WorkAttitudeVacationResponseDTO> findAllVacations();

    // 부서별 휴가 내역 조회
    @Query("SELECT new com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeVacationResponseDTO(" +
            "v.vacationId, v.empId.empId, v.empId.name, v.approver.empId, v.approver.name, " +
            "v.enrollVacation, v.vacationStart, v.vacationEnd, v.vacationType, v.vacationStatus, " +
            "v.vacationRejectReason, v.status) " +
            "FROM Vacation v WHERE v.empId.dept.departmentId = :deptId")
    List<WorkAttitudeVacationResponseDTO> findByDepartment(@Param("deptId") Long deptId);

    // 가장 가까운 휴가 내역 조회
    @Query("SELECT new com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeVacationResponseDTO(" +
            "v.vacationId, v.empId.empId, v.empId.name, v.approver.empId, v.approver.name, " +
            "v.enrollVacation, v.vacationStart, v.vacationEnd, v.vacationType, v.vacationStatus, " +
            "v.vacationRejectReason, v.status) " +
            "FROM Vacation v " +
            "WHERE v.empId.empId = :empId AND v.vacationStart >= :currentDate " +
            "ORDER BY v.vacationStart ASC")
    Optional<WorkAttitudeVacationResponseDTO> findNearestVacation(@Param("empId") String empId,
                                                                  @Param("currentDate") LocalDateTime currentDate);

    // 특정 직원의 휴가 상세 내역 조회
    @Query("SELECT new com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeVacationResponseDTO(" +
            "v.vacationId, v.empId.empId, v.empId.name, v.approver.empId, v.approver.name, " +
            "v.enrollVacation, v.vacationStart, v.vacationEnd, v.vacationType, v.vacationStatus, " +
            "v.vacationRejectReason, v.status) " +
            "FROM Vacation v WHERE v.empId.empId = :empId")
    List<WorkAttitudeVacationResponseDTO> findDetailsByEmpId(@Param("empId") String empId);

    // 특정 직원의 휴가 내역 조회 (사용된 휴가 포함)
    @Query("SELECT new com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeVacationResponseDTO(" +
            "v.vacationId, v.empId.empId, v.empId.name, v.approver.empId, v.approver.name, " +
            "v.enrollVacation, v.vacationStart, v.vacationEnd, v.vacationType, v.vacationStatus, " +
            "v.vacationRejectReason, v.status) " +
            "FROM Vacation v WHERE v.empId.empId = :empId")
    List<WorkAttitudeVacationResponseDTO> findByEmpId(@Param("empId") String empId);

}
