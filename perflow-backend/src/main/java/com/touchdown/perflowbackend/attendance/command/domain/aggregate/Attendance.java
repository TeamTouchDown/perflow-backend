package com.touchdown.perflowbackend.attendance.command.domain.aggregate;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "attendance", schema = "perflow")
public class Attendance {
    @Id
    @Column(name = "attendance_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee emp;

    @Column(name = "get_work_datetime", nullable = false)
    private Instant getWorkDatetime;

    @Column(name = "get_off_datetime", nullable = false)
    private Instant getOffDatetime;

}