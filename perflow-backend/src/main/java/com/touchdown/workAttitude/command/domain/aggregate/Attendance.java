package com.touchdown.workAttitude.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "attendance", schema = "perflow")
public class Attendance extends BaseEntity {
    @Id
    @Column(name = "attendance_id", nullable = false)
    private Long attendanceId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee empId;

    @Column(name = "get_work_datetime", nullable = false)
    private Instant getWorkDatetime;

    @Column(name = "get_off_datetime", nullable = false)
    private Instant getOffDatetime;

}