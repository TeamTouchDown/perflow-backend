package com.touchdown.perflowbackend.aiperfosummary.command.domain.aggregate;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "ai_perfo_summary", schema = "perflow")
public class AiPerfoSummary {
    @Id
    @Column(name = "ai_summary_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee emp;

    @Column(name = "perfo_type", nullable = false, length = 30)
    private String perfoType;

    @Column(name = "ai_summary", nullable = false)
    private String aiSummary;

    @Column(name = "create_datetime", nullable = false)
    private Instant createDatetime;

    @Column(name = "update_datetime")
    private Instant updateDatetime;

}