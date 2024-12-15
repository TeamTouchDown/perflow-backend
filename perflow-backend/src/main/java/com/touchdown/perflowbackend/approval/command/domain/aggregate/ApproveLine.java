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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_id")
    private Doc doc;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "create_user_id", nullable = false)
    private Employee createUser;

    @JoinColumn(name = "group_id")
    private Long groupId;

    @OneToMany(mappedBy = "approveLine", cascade = CascadeType.ALL)
    private List<ApproveSbj> approveSbjs = new ArrayList<>();

    @Column(name = "name", length = 30)
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "approve_template_type", nullable = false, length = 30)
    private ApproveTemplateType approveTemplateType;

    @Column(name = "approve_line_order", nullable = false)
    private Long approveLineOrder;

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
    public ApproveLine(Doc doc, ApproveTemplateType approveTemplateType, Long groupId, String name, String description, ApproveType approveType, Long approveLineOrder, Long pllGroupId, Employee createUser) {

        this.doc = doc;
        this.approveTemplateType = approveTemplateType;
        this.name = name;
        this.groupId = groupId;
        this.description = description;
        this.approveType = approveType;
        this.approveLineOrder = approveLineOrder;
        this.pllGroupId = pllGroupId;
        this.createUser = createUser;
    }

    public void addApproveSbj(ApproveSbj approveSbj) {
        // 결재 대상 리스트가 null 인 경우 초기화
        if (this.approveSbjs == null) {
            this.approveSbjs = new ArrayList<>();
        }

        // 리스트에 추가하고 ApproveSbj 객체에 결재선을 설정
        this.approveSbjs.add(approveSbj);
        approveSbj.setApproveLine(this);
    }

    public void updateStatus(Status status) {

        this.status = status;
    }

}