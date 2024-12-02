package com.touchdown.perflowbackend.approvesbj.command.domain.aggregate;

import com.touchdown.perflowbackend.approveline.command.domain.aggregate.ApproveLine;
import com.touchdown.perflowbackend.department.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "approve_sbj", schema = "perflow")
public class ApproveSbj {
    @Id
    @Column(name = "approve_sbj_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "approve_line_id", nullable = false)
    private ApproveLine approveLine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sbj_user_id")
    private Employee sbjUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id")
    private Department dept;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sbj_department_pic", nullable = false)
    private Employee sbjDepartmentPic;

    @Column(name = "sbj_type", nullable = false, length = 30)
    private String sbjType;

    @Column(name = "seq_approve_order")
    private Long seqApproveOrder;

    @ColumnDefault("0")
    @Column(name = "is_pll", nullable = false)
    private Byte isPll;

    @ColumnDefault("0")
    @Column(name = "is_auto", nullable = false)
    private Byte isAuto;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "complete_datetime")
    private Instant completeDatetime;

}