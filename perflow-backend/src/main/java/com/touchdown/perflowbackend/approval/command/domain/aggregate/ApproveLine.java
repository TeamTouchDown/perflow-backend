package com.touchdown.perflowbackend.approval.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private Doc docId;

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
    private Status status;

    @Column(name = "complete_datetime")
    private LocalDateTime completeDatetime;

    @Column(name = "delete_datetime")
    private LocalDateTime deleteDatetime;

}