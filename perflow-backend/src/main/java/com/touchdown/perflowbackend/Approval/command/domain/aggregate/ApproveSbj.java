package com.touchdown.perflowbackend.Approval.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "approve_sbj", schema = "perflow")
public class ApproveSbj extends BaseEntity {
    @Id
    @Column(name = "approve_sbj_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long approveSbjId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "approve_line_id", nullable = false)
    private ApproveLine approveLineId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sbj_user_id")
    private Employee sbjUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id")
    private Department deptId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sbj_department_pic", nullable = false)
    private Employee sbjDepartmentPic;

    @Column(name = "sbj_type", nullable = false, length = 30)
    private String sbjType;

    @Column(name = "seq_approve_order")
    private Long seqApproveOrder;

    @ColumnDefault("0")
    @Column(name = "is_pll", nullable = false)
    private Boolean isPll;

    @ColumnDefault("0")
    @Column(name = "is_auto", nullable = false)
    private Boolean isAuto;

    @Column(name = "status", nullable = false, length = 30)
    private Status status;

    @Column(name = "complete_datetime")
    private LocalDateTime completeDatetime;

}