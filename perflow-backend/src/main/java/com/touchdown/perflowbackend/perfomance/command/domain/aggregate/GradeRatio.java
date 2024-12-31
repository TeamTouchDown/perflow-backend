package com.touchdown.perflowbackend.perfomance.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "grade_ratio", schema = "perflow")
public class GradeRatio extends BaseEntity {

    @Id
    @Column(name = "grade_ratio_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gradeRatioId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee emp;

    @Column(name = "s_ratio", nullable = false)
    private Long sRatio;

    @Column(name = "a_ratio", nullable = false)
    private Long aRatio;

    @Column(name = "b_ratio", nullable = false)
    private Long bRatio;

    @Column(name = "c_ratio", nullable = false)
    private Long cRatio;

    @Column(name = "d_ratio", nullable = false)
    private Long dRatio;

    @Column(name = "update_reason", nullable = false)
    private String updateReason;

    @Builder
    public GradeRatio(Employee emp, Long sRatio, Long aRatio, Long bRatio, Long cRatio, Long dRatio, String reason) {
        this.emp = emp;
        this.sRatio = sRatio;
        this.aRatio = aRatio;
        this.bRatio = bRatio;
        this.cRatio = cRatio;
        this.dRatio = dRatio;
        this.updateReason = reason;
    }
}