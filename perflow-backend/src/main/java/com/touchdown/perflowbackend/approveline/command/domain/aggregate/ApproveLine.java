package com.touchdown.perflowbackend.approveline.command.domain.aggregate;

import com.touchdown.perflowbackend.doc.command.domain.aggregate.Doc;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "approve_line", schema = "perflow")
public class ApproveLine {
    @Id
    @Column(name = "approve_line_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doc_id", nullable = false)
    private Doc doc;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "create_user_id", nullable = false)
    private Employee createUser;

    @Column(name = "name", length = 30)
    private String name;

    @Column(name = "approve_line_order", nullable = false)
    private Integer approveLineOrder;

    @Column(name = "pll_group_id")
    private Long pllGroupId;

    @Column(name = "approve_type", nullable = false, length = 30)
    private String approveType;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "create_datetime", nullable = false)
    private Instant createDatetime;

    @Column(name = "complete_datetime")
    private Instant completeDatetime;

    @Column(name = "update_datetime")
    private Instant updateDatetime;

    @Column(name = "delete_datetime")
    private Instant deleteDatetime;

}