package com.touchdown.perflowbackend.Approval.command.domain.aggregate;

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
@Table(name = "doc", schema = "perflow")
public class Doc extends BaseEntity {
    @Id
    @Column(name = "doc_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long docId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "create_user_id", nullable = false)
    private Employee createUserId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "template_id", nullable = false)
    private Template templateId;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "status", nullable = false, length = 30)
    private Status status;

    @Column(name = "delete_datetime")
    private LocalDateTime deleteDatetime;

    @Column(name = "collect_datetime")
    private LocalDateTime collectDatetime;

    @Column(name = "draft_datetime")
    private LocalDateTime draftDatetime;

}