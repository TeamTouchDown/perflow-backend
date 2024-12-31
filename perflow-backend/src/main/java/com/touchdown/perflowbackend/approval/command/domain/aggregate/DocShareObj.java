package com.touchdown.perflowbackend.approval.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "doc_share_obj", schema = "perflow")
public class DocShareObj extends BaseEntity {

    @Id
    @Column(name = "doc_share_obj", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long docShareObj;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doc_id", nullable = false)
    private Doc doc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "share_obj_user_id")
    private Employee shareObjUser;    // 문서 공유 대상자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "share_obj_department_id")
    private Department shareObjDepartment;  // 문서 공유 대상 부서

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "share_add_user_id", nullable = false)
    private Employee shareAddUser;  // 공유 대상을 추가한 사원

    @Enumerated(EnumType.STRING)
    @Column(name = "share_obj_type", nullable = false, length = 30)
    private EmpDeptType shareEmpDeptType;   // 공유 대상 유형(EMPLOYEE, DEPARTMENT)

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private Status status = Status.ACTIVATED;

    @Builder
    public DocShareObj(Doc doc, EmpDeptType shareEmpDeptType, Employee shareObjUser, Department shareObjDepartment, Employee shareAddUser) {

        this.doc = doc;
        this.shareEmpDeptType = shareEmpDeptType;
        this.shareObjUser = shareObjUser;
        this.shareObjDepartment = shareObjDepartment;
        this.shareAddUser = shareAddUser;
    }
}