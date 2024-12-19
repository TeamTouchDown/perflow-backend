package com.touchdown.perflowbackend.notification.command.domain.aggregate;

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

    @Column(name = "content", nullable = false, length = 50)
    private String content;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "target_token", nullable = false)
    private String targetToken;

    @Column(name = "create_datetime", nullable = false)
    @CreatedDate
    private LocalDateTime createDatetime;

    @Builder
    public Notification(Long notiId, Long refId, RefType refType, String content, String url, String targetToken) {
        this.notiId = notiId;
        this.refId = refId;
        this.refType = refType;
        this.content = content;
        this.url = url;
        this.targetToken = targetToken;
    }
}