package com.touchdown.perflowbackend.notification.command.domain.aggregate;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "notification", schema = "perflow")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "noti_id", nullable = false)
    private Long notiId;

    @Column(name = "ref_id", nullable = false)
    private Long refId;

    @Column(name = "ref_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private RefType refType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee employee;

    @Column(name = "content", nullable = false, length = 50)
    private String content;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "create_datetime", nullable = false)
    @CreatedDate
    private LocalDateTime createDatetime;

    @Builder
    public Notification(Long notiId, Long refId, RefType refType, Employee employee, String content, String url) {
        this.notiId = notiId;
        this.refId = refId;
        this.refType = refType;
        this.employee = employee;
        this.content = content;
        this.url = url;
    }
}