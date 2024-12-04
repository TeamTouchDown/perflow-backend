package com.touchdown.perflowbackend.approve.command.domain.aggregate;

import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "doc_share_obj", schema = "perflow")
public class DocShareObj {
    @Id
    @Column(name = "doc_share_obj", nullable = false)
    private Long docShareObj;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doc_id", nullable = false)
    private Doc docId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "share_obj_user_id", nullable = false)
    private Employee shareObjUserId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "share_obj_department_id", nullable = false)
    private Department shareObjDepartmentId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "share_add_user_id", nullable = false)
    private Employee shareAddUserId;

    @Column(name = "share_obj_type", nullable = false, length = 30)
    private String shareObjType;

    @CreatedDate
    @Column(name = "share_datetime", nullable = false)
    private LocalDateTime shareDatetime;

    @Column(name = "delete_datetime")
    private LocalDateTime deleteDatetime;

    @Column(name = "status", nullable = false, length = 30)
    private Status status;

}