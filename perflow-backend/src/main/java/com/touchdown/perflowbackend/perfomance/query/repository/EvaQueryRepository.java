package com.touchdown.perflowbackend.perfomance.query.repository;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.Perfo;
import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.PerfoType;
import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.QuestionType;
import com.touchdown.perflowbackend.perfomance.query.dto.EvaAnswerResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.dto.EvaDetailResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.dto.EvaQuestionDetailResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EvaQueryRepository extends JpaRepository<Perfo, Long> {


    // 사번, 부서번호를 이용해 동료 평가 데이터를 받아오고 동료 평가 여부와 평가 진행 여부를 확인
    @Query("SELECT new com.touchdown.perflowbackend.perfomance.query.dto.EvaDetailResponseDTO( " +
            "e.empId, e.name, d.name, p.name, j.name, " +
            "CASE WHEN COUNT(p2) > 0 THEN true ELSE false END " +
            ") " +
            "FROM Employee e " +
            "JOIN e.dept d " +
            "JOIN e.position p " +
            "JOIN Perfo f ON e.empId = f.perfoEmp.empId " +
            "JOIN f.perfoQuestion q " +
            "JOIN Job j ON j.jobId = e.job.jobId " +
            "LEFT JOIN Perfo p2 ON p2.perfoEmp.empId = e.empId AND FUNCTION('YEAR', p2.createDatetime) = :currentYear " +
            "WHERE e.empId != :empId AND d.departmentId = :deptId AND q.perfoType = 'COL' " +
            "GROUP BY e.empId, e.name, d.name, p.name, j.name")
    List<EvaDetailResponseDTO> findEmployeeDetailsWithExistence(
            @Param("deptId") Long deptId,
            @Param("empId") String empId,
            @Param("currentYear") int currentYear
    );

    // 평가자, 피평가자 사번, 현재 년도를 이용해 동료 평가 상세 정보 조회
    @Query("SELECT new com.touchdown.perflowbackend.perfomance.query.dto.EvaAnswerResponseDTO( " +
            "p.perfoQuestion.perfoQuestionId, p.answer " +
            ") " +
            "FROM Perfo p " +
            "WHERE p.perfoEmp.empId = :perfoempId " +
            "AND p.perfoedEmp.empId = :perfoedempId " +
            "AND p.perfoQuestion.perfoType = 'COL' " +
            "AND FUNCTION('YEAR', p.createDatetime) = :currentYear ")
    List<EvaAnswerResponseDTO> findAnswerByEmpIds(
            @Param("perfoempId") String perfoempId,
            @Param("perfoedempId") String perfoedempId,
            @Param("currentYear") int currentYear
    );

    // 부서 아이디, 문항의 타입 등을 이용해 동료 평가 문항 상세 조회
    @Query("SELECT new com.touchdown.perflowbackend.perfomance.query.dto.EvaQuestionDetailResponseDTO( " +
            "q.perfoQuestionId, q.questionContent " +
            ") " +
            "FROM Perfoquestion q " +
            "WHERE q.dept.departmentId = :deptId " +
            "AND q.perfoType = :perfoType " +
            "AND q.questionType = :questionType " +
            "AND FUNCTION('YEAR', q.createDatetime) = :currentYear")
    List<EvaQuestionDetailResponseDTO> findQuestionBydept(
            @Param("deptId") Long deptId,
            @Param("perfoType") PerfoType perfoType,
            @Param("questionType") QuestionType questionType,
            @Param("currentYear") int currentYear
    );
}
