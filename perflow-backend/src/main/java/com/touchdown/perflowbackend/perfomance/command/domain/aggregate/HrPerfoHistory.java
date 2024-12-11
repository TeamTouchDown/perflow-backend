package com.touchdown.perflowbackend.perfomance.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.AccessLevel;
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
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee emp;

    @Column(name = "adjustment_degree", nullable = false)
    private Long adjustmentDegree;

    @Column(name = "adjustment_score", nullable = false)
    private Long adjustmentScore;

    @Column(name = "adjustment_reason", nullable = false)
    private String adjustmentReason;

    @Column(name = "perfo_type", nullable = false)
    private String perfoType;
}