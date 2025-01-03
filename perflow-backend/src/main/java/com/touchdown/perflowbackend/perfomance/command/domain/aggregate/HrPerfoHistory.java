package com.touchdown.perflowbackend.perfomance.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.perfomance.command.application.dto.CreatePerfoAdjustmentDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "hr_perfo_history", schema = "perflow")
public class HrPerfoHistory extends BaseEntity {

    @Id
    @Column(name = "hr_perfo_history_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hrPerfoHistoryId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "perfo_emp_id", nullable = false)
    private Employee perfo_emp;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "perfoed_emp_id", nullable = false)
    private Employee perfoed_emp;

    @Column(name = "adjustment_degree", nullable = false)
    private Long adjustmentDegree;

    @Column(name = "adjustment_col_score", nullable = false)
    private Long adjustmentColScore;

    @Column(name = "adjustment_down_score", nullable = false)
    private Long adjustmentDownScore;

    @Column(name = "adjustment_reason", nullable = false)
    private String adjustmentReason;

    @Builder
    public HrPerfoHistory(Employee perfo_emp, Employee perfoed_emp, Long adjustmentColScore, Long adjustmentDownScore ,Long adjustmentDegree, String reason){
        this.perfo_emp = perfo_emp;
        this.perfoed_emp = perfoed_emp;
        this.adjustmentColScore = adjustmentColScore;
        this.adjustmentDownScore = adjustmentDownScore;
        this.adjustmentDegree = adjustmentDegree;
        this.adjustmentReason = reason;
    }
}