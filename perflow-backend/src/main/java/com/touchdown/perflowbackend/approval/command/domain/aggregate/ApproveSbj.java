package com.touchdown.perflowbackend.approval.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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
    private ApproveLine approveLine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sbj_user_id")
    private Employee sbjUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id")
    private Department dept;

    // 부서 결재 시 담당자(수신함에서 접수 선택 시 담당자로 지정됨)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sbj_department_pic", nullable = false)
    private Employee sbjDepartmentPic;

    @Enumerated(EnumType.STRING)
    @Column(name = "sbj_type", nullable = false, length = 30)
    private SbjType sbjType;

    @ColumnDefault("false")
    @Column(name = "is_pll", nullable = false)
    private Boolean isPll;

    @Column(name = "status", nullable = false, length = 30)
    private Status status;

    @Column(name = "complete_datetime")
    private LocalDateTime completeDatetime;

    @Builder
    public ApproveSbj(ApproveLine approveLine, Employee sbjUser) {

        this.approveLine = approveLine;
        this.sbjUser = sbjUser;
    }
}