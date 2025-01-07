package com.touchdown.perflowbackend.approval.query.specification;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveLine;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveSbj;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.Doc;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.Status;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

// 다중 조건 검색을 위한 Specification 클래스
public class DocSpecification {

    // 제목 검색
    public static Specification<Doc> titleContains(String title) {

        return (root, query, criteriaBuilder) ->
                title == null || title.isEmpty()
                        ? criteriaBuilder.conjunction() // 조건이 없으면 무시
                        : criteriaBuilder.like(root.get("title"), "%" + title + "%");
    }

    // 작성자 이름 검색
    public static Specification<Doc> createUserNameContains(String createUserName) {

        return (root, query, criteriaBuilder) ->
                createUserName == null || createUserName.isEmpty()
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.like(root.get("createUser").get("name"), "%" + createUserName + "%");
    }

    // 작성자 id 검색
    public static Specification<Doc> createdBy(String empId) {
        return (root, query, criteriaBuilder) ->
                empId == null || empId.isEmpty()
                        ? criteriaBuilder.conjunction() // 조건 없음
                        : criteriaBuilder.equal(root.get("createUser").get("empId"), empId);
    }

    // 작성일 기간 검색
    public static Specification<Doc> createDateBetween(LocalDateTime fromDate, LocalDateTime toDate) {

        return (root, query, criteriaBuilder) -> {
            if (fromDate == null && toDate == null) {
                return criteriaBuilder.conjunction();
            }
            if (fromDate != null && toDate != null) {
                return criteriaBuilder.between(root.get("createDatetime"), fromDate, toDate);
            }
            if (fromDate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("createDatetime"), fromDate);
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("createDatetime"), toDate);
        };
    }

    // 유저가 결재 주체인지 확인
    public static Specification<Doc> hasActiveApproveSbjForUser(String empId) {

        return (root, query, criteriaBuilder) -> {
            if (empId == null || empId.isEmpty()) {
                return criteriaBuilder.conjunction(); // 조건이 없으면 true 반환
            }

            // 중복 제거
            query.distinct(true);

            // 조인 및 조건 설정
            var approveLines = root.join("approveLines");
            var approveSbjs = approveLines.join("approveSbjs");
            return criteriaBuilder.and(
                    criteriaBuilder.equal(approveSbjs.get("sbjUser").get("empId"), empId),
                    criteriaBuilder.equal(approveSbjs.get("status"), Status.ACTIVATED)
            );
        };

    }

    // 유저가 처리한 문서인지 확인
    public static Specification<Doc> hasProcessedByUser(String empId) {

        return (root, query, criteriaBuilder) -> {
            Join<Doc, ApproveLine> approveLineJoin = root.join("approveLines");
            Join<ApproveLine, ApproveSbj> approveSbjJoin = approveLineJoin.join("approveSbjs");
            return criteriaBuilder.and(
                    criteriaBuilder.equal(approveSbjJoin.get("sbjUser").get("empId"), empId),
                    criteriaBuilder.notEqual(approveSbjJoin.get("status"), Status.ACTIVATED) // 활성화된 문서 제외
            );
        };
    }

    public static Specification<Doc> hasActiveApproveSbjForDept(Long deptId, Integer positionLevel) {

        return (root, query, criteriaBuilder) -> {
            if (deptId == null || positionLevel == null) {
                return criteriaBuilder.conjunction();
            }

            // 중복 제거
            query.distinct(true);

            // 결재선 및 부서/직위 조건
            var approveLines = root.join("approveLines");
            var approveSbjs = approveLines.join("approveSbjs");
            return criteriaBuilder.and(
                    criteriaBuilder.equal(approveSbjs.get("sbjUser").get("dept").get("departmentId"), deptId),
                    criteriaBuilder.lessThanOrEqualTo(approveSbjs.get("sbjUser").get("position").get("positionLevel"), positionLevel)
            );
        };
    }
}
