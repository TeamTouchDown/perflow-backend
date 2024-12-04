package com.touchdown.perflowbackend.perfomance.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "hr_perfo_history", schema = "perflow")
public class HrPerfoHistory extends BaseEntity {
    @Id
    @Column(name = "hr_perfo_history_id", nullable = false)
    private Long hrPerfoHistoryId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hr_perfo_id", nullable = false)
    private HrPerfo hrPerfo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee emp;

    @Column(name = "adjustment_dehree", nullable = false)
    private Long adjustmentDehree;

    @Column(name = "adjustment_score", nullable = false)
    private Long adjustmentScore;

    @Column(name = "adjustment_reason", nullable = false)
    private String adjustmentReason;
}