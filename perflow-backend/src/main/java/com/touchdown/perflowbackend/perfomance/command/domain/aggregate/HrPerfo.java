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
@Table(name = "hr_perfo", schema = "perflow")
public class HrPerfo {
    @Id
    @Column(name = "hr_perfo_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hrPerfoId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee emp;

    @Column(name = "grade", nullable = false, length = 30)
    private String grade;

    @Column(name = "score", nullable = false)
    private Long score;

    @Column(name = "create_datetime", nullable = false)
    private Instant createDatetime;

    @Column(name = "update_datetime")
    private Instant updateDatetime;

}