package com.touchdown.perflowbackend.workAttitude.command.domain.aggregate;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    @Column(name = "check_in_datetime", nullable = true)
    private LocalDateTime checkInDateTime;

    @Column(name = "check_out_datetime", nullable = true) // 퇴근 시간이 없을 수도 있음
    private LocalDateTime checkOutDateTime;

    @Column(name = "status", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private AttendanceStatus attendanceStatus; // 상태: "출근" / "퇴근"


    @Builder
    public Attendance(Employee empId,
                      LocalDateTime checkInDateTime,
                      LocalDateTime checkOutDateTime,
                      AttendanceStatus attendanceStatus) {
        this.empId = empId;
        this.checkInDateTime = checkInDateTime;
        this.checkOutDateTime = checkOutDateTime;
        this.attendanceStatus = attendanceStatus;
    }

    public void updateCheckOut(LocalDateTime checkOutDateTime, AttendanceStatus attendanceStatus) {
        this.checkOutDateTime = checkOutDateTime;
        this.attendanceStatus = attendanceStatus;

    }
}
