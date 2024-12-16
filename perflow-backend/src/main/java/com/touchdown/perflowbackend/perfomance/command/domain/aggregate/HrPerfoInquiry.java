package com.touchdown.perflowbackend.perfomance.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.perfomance.command.application.dto.UpdateInquiryRequestDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "emp_id", nullable = true)
    private Employee emp;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "status", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private PassStatus status;

    @Column(name = "pass_reason")
    private String passReason;

    @Builder
    public HrPerfoInquiry(HrPerfo hrPerfo, String reason, PassStatus status) {
        this.hrPerfo = hrPerfo;
        this.reason = reason;
        this.status = status;
    }

    public void updateinquiry(Employee emp, UpdateInquiryRequestDTO updateInquiryRequestDTO) {
        this.emp = emp;
        this.passReason = updateInquiryRequestDTO.getReason();
        this.status = PassStatus.PASS;
    }

    public void updateinquiry(Employee emp, String reason) {
        this.emp = emp;
        this.passReason = reason;
        this.status = PassStatus.REJECT;
    }
}