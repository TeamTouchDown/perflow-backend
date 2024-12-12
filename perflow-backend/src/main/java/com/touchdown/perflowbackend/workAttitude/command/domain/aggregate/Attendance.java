package com.touchdown.perflowbackend.workAttitude.command.domain.aggregate;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "attendance", schema = "perflow")
public class Attendance { //출퇴

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id", nullable = false)
    private Long attendanceId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee empId;

    @Column(name = "get_work_datetime", nullable = false)
    private LocalDateTime getWorkDatetime;

    @Column(name = "get_off_datetime", nullable = false)
    private LocalDateTime getOffDatetime;

    @Column(name = "auth_num", nullable = true)
    private String authNum;

    @Column(name = "expire_time", nullable = true)
    private LocalDateTime expireTime;

}
