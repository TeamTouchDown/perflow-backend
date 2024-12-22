package com.touchdown.perflowbackend.approval.query.specification;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveSbj;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.Status;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

// 다중 조건 검색을 위한 Specification 클래스
public class ApproveSbjSpecification {

    // 결재 주체 상태가 APPROVED 또는 REJECTED인지 확인
    public static Specification<ApproveSbj> hasApprovedOrRejectedStatus() {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.or(
                        criteriaBuilder.equal(root.get("status"), Status.APPROVED),
                        criteriaBuilder.equal(root.get("status"), Status.REJECTED)
                );
    }

    // 결재 주체가 특정 사용자(empId)에 해당하는지 확인
    public static Specification<ApproveSbj> belongsToUser(String empId) {

        return (root, query, criteriaBuilder) ->
                empId == null || empId.isEmpty()
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.equal(root.get("sbjUser").get("empId"), empId);
    }

    // 제목 검색
    public static Specification<ApproveSbj> titleContains(String title) {

        return (root, query, criteriaBuilder) -> {
            query.distinct(true); // 중복 제거
            var docJoin = root.join("approveLine", JoinType.INNER).join("doc", JoinType.INNER);
            return title == null || title.isEmpty()
                    ? criteriaBuilder.conjunction()
                    : criteriaBuilder.like(docJoin.get("title"), "%" + title + "%");
        };
    }

    // 작성자 검색
    public static Specification<ApproveSbj> createUserNameContains(String createUserName) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true); // 중복 제거
            var docJoin = root.join("approveLine", JoinType.INNER).join("doc", JoinType.INNER);
            return createUserName == null || createUserName.isEmpty()
                    ? criteriaBuilder.conjunction()
                    : criteriaBuilder.like(docJoin.get("createUser").get("name"), "%" + createUserName + "%");
        };
    }

    // 작성일 검색
    public static Specification<ApproveSbj> createDateBetween(LocalDateTime fromDate, LocalDateTime toDate) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true); // 중복 제거
            var docJoin = root.join("approveLine", JoinType.INNER).join("doc", JoinType.INNER);
            if (fromDate == null && toDate == null) {
                return criteriaBuilder.conjunction();
            }
            if (fromDate != null && toDate != null) {
                return criteriaBuilder.between(docJoin.get("createDatetime"), fromDate, toDate);
            }
            if (fromDate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(docJoin.get("createDatetime"), fromDate);
            }
            return criteriaBuilder.lessThanOrEqualTo(docJoin.get("createDatetime"), toDate);
        };
    }

    // 문서 상태가 APPROVED가 아닌지 확인
    public static Specification<ApproveSbj> docStatusNotApproved() {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true); // 중복 제거
            var docJoin = root.join("approveLine", JoinType.INNER).join("doc", JoinType.INNER);
            return criteriaBuilder.notEqual(docJoin.get("status"), Status.APPROVED);
        };
    }
}
