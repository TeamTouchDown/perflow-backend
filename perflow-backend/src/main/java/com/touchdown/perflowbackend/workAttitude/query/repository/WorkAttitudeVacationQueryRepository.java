package com.touchdown.perflowbackend.workAttitude.query.repository;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Vacation;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeVacationResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkAttitudeVacationQueryRepository extends JpaRepository<Vacation, Long> {

    // 개인 휴가 사용 정보 조회
    @Query("SELECT new com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeVacationResponseDTO(" +
            "v.vacationId, " +
            "v.empId.empId, " +                       // 사원 ID
            "v.empId.name, " +                        // 사원 이름
            "v.approveSbjId.approveSbjId, " +         // 결재 주제 ID
            "v.approveSbjId.sbjUser.name, " +         // 결재자 이름
            "v.enrollVacation, " +                    // 신청일
            "v.vacationStart, " +
            "v.vacationEnd, " +
            "v.vacationType, " +
            "v.vacationStatus, " +
            "v.vacationRejectReason, " +
            "v.status) " +                            // 상태
            "FROM Vacation v " +
            "WHERE v.empId.empId = :empId")
    List<WorkAttitudeVacationResponseDTO> findVacationUsage(@Param("empId") String empId);

    // 개인 휴가 상세 내역 조회
    @Query("SELECT new com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeVacationResponseDTO(" +
            "v.vacationId, " +
            "v.empId.empId, " +
            "v.empId.name, " +
            "v.approveSbjId.approveSbjId, " +
            "v.approveSbjId.sbjUser.name, " +
            "v.enrollVacation, " +
            "v.vacationStart, " +
            "v.vacationEnd, " +
            "v.vacationType, " +
            "v.vacationStatus, " +
            "v.vacationRejectReason, " +
            "v.status) " +
            "FROM Vacation v " +
            "WHERE v.empId.empId = :empId")
    List<WorkAttitudeVacationResponseDTO> findVacationDetails(@Param("empId") String empId);

    // 팀장: 팀원 휴가 조회
    @Query("SELECT new com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeVacationResponseDTO(" +
            "v.vacationId, " +
            "v.empId.empId, " +
            "v.empId.name, " +
            "v.approveSbjId.approveSbjId, " +
            "v.approveSbjId.sbjUser.name, " +
            "v.enrollVacation, " +
            "v.vacationStart, " +
            "v.vacationEnd, " +
            "v.vacationType, " +
            "v.vacationStatus, " +
            "v.vacationRejectReason, " +
            "v.status) " +
            "FROM Vacation v JOIN v.empId e " +
            "WHERE e.dept.departmentId = :deptId")
    List<WorkAttitudeVacationResponseDTO> findTeamVacationList(@Param("deptId") Long deptId);

    // 인사팀: 모든 휴가 내역 조회
    @Query("SELECT new com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeVacationResponseDTO(" +
            "v.vacationId, " +
            "v.empId.empId, " +
            "v.empId.name, " +
            "v.approveSbjId.approveSbjId, " +
            "v.approveSbjId.sbjUser.name, " +
            "v.enrollVacation, " +
            "v.vacationStart, " +
            "v.vacationEnd, " +
            "v.vacationType, " +
            "v.vacationStatus, " +
            "v.vacationRejectReason, " +
            "v.status) " +
            "FROM Vacation v")
    List<WorkAttitudeVacationResponseDTO> findAllVacationList();
}
