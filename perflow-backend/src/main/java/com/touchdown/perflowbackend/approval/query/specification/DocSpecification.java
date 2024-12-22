package com.touchdown.perflowbackend.approval.query.specification;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.Doc;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.Status;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

// 다중 조건 검색을 위한 Specification 클래스
public class DocSpecification {

    // 제목
    public static Specification<Doc> titleContains(String title) {

        return (root, query, criteriaBuilder) ->
                title == null || title.isEmpty()
                        ? criteriaBuilder.conjunction() // 조건이 없으면 무시
                        : criteriaBuilder.like(root.get("title"), "%" + title + "%");
    }

    public static Specification<Doc> createUserNameContains(String createUserName) {

        return (root, query, criteriaBuilder) ->
                createUserName == null || createUserName.isEmpty()
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.like(root.get("createUser").get("name"), "%" + createUserName + "%");
    }

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
}
