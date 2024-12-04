package com.touchdown.perflowbackend.perfomance.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "hr_perfo_inquiry", schema = "perflow")
public class HrPerfoInquiry extends BaseEntity {
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
}