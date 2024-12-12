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

    @Column(name = "get_work_datetime", nullable = true)
    private LocalDateTime checkInDateTime;

    @Column(name = "get_off_datetime", nullable = true) // 퇴근 시간이 없을 수도 있음
    private LocalDateTime checkOutDateTime;

    @Column(name = "status", nullable = false, length = 10)
    private String status; // 상태: "출근" / "퇴근"


    @Builder
    public Attendance(Employee empId,
                      LocalDateTime CheckInDateTime,
                      LocalDateTime CheckOutDateTime,
                      String status) {
        this.empId = empId;
        this.checkInDateTime = CheckInDateTime;
        this.checkOutDateTime = CheckOutDateTime;
        this.status = status;
    }
}
