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
@Table(name = "hr_perfo_inquiry", schema = "perflow")
public class HrPerfoInquiry {
    @Id
    @Column(name = "hr_perfo_inquiry_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hrPerfoInquiryId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hr_perfo_id", nullable = false)
    private HrPerfo hrPerfo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee emp;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "pass_reason", nullable = false)
    private String passReason;

    @Column(name = "create_datetime", nullable = false)
    private Instant createDatetime;

    @Column(name = "update_datetime")
    private Instant updateDatetime;

}