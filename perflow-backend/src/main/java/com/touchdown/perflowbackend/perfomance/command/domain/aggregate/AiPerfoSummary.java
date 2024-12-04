package com.touchdown.perflowbackend.perfomance.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "ai_perfo_summary", schema = "perflow")
public class AiPerfoSummary extends BaseEntity {

    @Id
    @Column(name = "ai_summary_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aiSummaryId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee emp;

    @Column(name = "perfo_type", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private PerfoType perfoType;

    @Column(name = "ai_summary", nullable = false)
    private String aiSummary;
}