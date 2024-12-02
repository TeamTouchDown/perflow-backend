package com.touchdown.perflowbackend.docshareobj.command.domain.aggregate;

import com.touchdown.perflowbackend.department.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.doc.command.domain.aggregate.Doc;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "doc_share_obj", schema = "perflow")
public class DocShareObj {
    @Id
    @Column(name = "doc_share_obj", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doc_id", nullable = false)
    private Doc doc;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "share_obj_user_id", nullable = false)
    private Employee shareObjUser;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "share_obj_department_id", nullable = false)
    private Department shareObjDepartment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "share_add_user_id", nullable = false)
    private Employee shareAddUser;

    @Column(name = "share_obj_type", nullable = false, length = 30)
    private String shareObjType;

    @Column(name = "share_datetime", nullable = false)
    private Instant shareDatetime;

    @Column(name = "delete_datetime")
    private Instant deleteDatetime;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

}