package com.touchdown.perflowbackend.payment.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "payroll", schema = "perflow")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payroll extends BaseEntity {

    @Id
    @Column(name = "payroll_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payrollId;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "payroll", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PayrollDetail> payrollDetailList = new ArrayList<>();

    // Payroll 객체 생성 시 name을 전달하는 생성자
    public Payroll(String name) {
        this.name = name;
    }

    // PayrollDetail을 추가하는 메서드
    public void addPayrollDetail(PayrollDetail payrollDetail) {

        payrollDetail.assignPayroll(this); // Payroll 객체 설정
        this.payrollDetailList.add(payrollDetail);

    }
}
