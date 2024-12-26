package com.touchdown.perflowbackend.workAttitude.command.domain.aggregate;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "approval_request", schema = "perflow")
public class ApprovalRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId; // 요청 ID

    @ManyToOne
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee empId; // 신청자 (사번)

    @ManyToOne
    @JoinColumn(name = "approver_id", nullable = false)
    private Employee approverId; // 결재자 (사번)

    @Column(name = "related_id", nullable = false)
    private Long relatedId; // 관련 요청 ID

    @Enumerated(EnumType.STRING)
    @Column(name = "request_type", nullable = false)
    private RequestType requestType; // 요청 타입

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.PENDING; // 상태 (기본값: 대기)

    @Column(name = "create_datetime", nullable = false)
    private LocalDateTime createDatetime; // 생성일시

    @Column(name = "update_datetime")
    private LocalDateTime updateDatetime; // 업데이트일시

    @Column(name = "reject_reason")
    private String rejectReason; // 반려 사유

    @Enumerated(EnumType.STRING)
    @Column(name = "delete_status", nullable = false)
    private DeleteStatus deleteStatus = DeleteStatus.ACTIVATED; // 소프트 딜리트 상태 (기본값: 활성화)

}
