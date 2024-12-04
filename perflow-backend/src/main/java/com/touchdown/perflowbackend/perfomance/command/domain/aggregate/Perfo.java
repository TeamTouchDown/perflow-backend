package com.touchdown.perflowbackend.perfomance.command.domain.aggregate;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "perfo", schema = "perflow")
public class Perfo {
    @Id
    @Column(name = "perfo_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long perfoId;

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