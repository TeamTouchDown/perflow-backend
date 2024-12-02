package com.touchdown.perflowbackend.perfo.command.domain.aggregate;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.perfoquestion.command.domain.aggregate.Perfoquestion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "perfo", schema = "perflow")
public class Perfo {
    @Id
    @Column(name = "perfo_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "perfo_question_id", nullable = false)
    private Perfoquestion perfoQuestion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "perfo_emp_id", nullable = false)
    private Employee perfoEmp;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "perfoed_emp_id", nullable = false)
    private Employee perfoedEmp;

    @Column(name = "answer", nullable = false)
    private String answer;

    @Column(name = "create_datetime", nullable = false)
    private Instant createDatetime;

    @Column(name = "update_datetime")
    private Instant updateDatetime;

}