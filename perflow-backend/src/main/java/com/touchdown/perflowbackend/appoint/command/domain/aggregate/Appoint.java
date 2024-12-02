package com.touchdown.perflowbackend.appoint.command.domain.aggregate;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "appoint", schema = "perflow")
public class Appoint {
    @Id
    @Column(name = "appointId", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee emp;

    @Column(name = "type", nullable = false, length = 30)
    private String type;

    @Column(name = "`before`", nullable = false, length = 30)
    private String before;

    @Column(name = "after", nullable = false, length = 30)
    private String after;

    @Column(name = "appoint_datetime", nullable = false)
    private Instant appointDatetime;

}