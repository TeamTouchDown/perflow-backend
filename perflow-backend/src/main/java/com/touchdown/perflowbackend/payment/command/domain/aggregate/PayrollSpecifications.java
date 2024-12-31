package com.touchdown.perflowbackend.payment.command.domain.aggregate;

import org.springframework.data.jpa.domain.Specification;

public class PayrollSpecifications {

    public static Specification<PayrollDetail> hasPayrollId(Long payrollId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("payroll").get("payrollId"), payrollId);
    }

//    public static Specification<PayrollDetail> hasEmpName(String empName) {
//
//        return (root, query, criteriaBuilder) ->
//                criteriaBuilder.like(root.get("emp").get("name"), "%" + empName + "%");
//
//    }

    public static Specification<PayrollDetail> hasEmpId(String empId) {

        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.like(root.get("emp").get("empId"), "%" + empId + "%"),
                criteriaBuilder.equal(root.get("emp").get("status"), "ACTIVE")
        );

    }

//    public static Specification<PayrollDetail> hasDeptName(String deptName) {
//
//        return (root, query, criteriaBuilder) ->
//                criteriaBuilder.like(root.get("emp").get("dept").get("name"), "%" + deptName + "%");
//
//    }
}
