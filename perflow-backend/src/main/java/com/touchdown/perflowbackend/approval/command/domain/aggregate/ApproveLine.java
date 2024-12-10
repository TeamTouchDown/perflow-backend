package com.touchdown.perflowbackend.approval.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "approve_line", schema = "perflow")
public class ApproveLine extends BaseEntity {

    @Id
    @Column(name = "approve_line_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long approveLineId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doc_id", nullable = false)
    private Doc doc;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "create_user_id", nullable = false)
    private Employee createUser;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "employee_id")
//    private Employee employee;

    @OneToMany(mappedBy = "approveLine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApproveSbj> approveSubjects = new ArrayList<>();

    @Column(name = "name", length = 30)
    private String name;

    @Column(name = "approve_line_order", nullable = false)
    private Integer approveLineOrder;

    @Column(name = "pll_group_id")
    private Long pllGroupId;

    @Enumerated(EnumType.STRING)
    @Column(name = "approve_type", nullable = false, length = 30)
    private ApproveType approveType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private Status status = Status.PENDING;

    @Column(name = "complete_datetime")
    private LocalDateTime completeDatetime;

    @Builder
    public ApproveLine(Doc doc, ApproveType approveType, Integer approveLineOrder, Long pllGroupId, Employee createUser) {

        this.doc = doc;
        this.approveType = approveType;
        this.approveLineOrder = approveLineOrder;
        this.pllGroupId = pllGroupId;
        this.createUser = createUser;
    }
}