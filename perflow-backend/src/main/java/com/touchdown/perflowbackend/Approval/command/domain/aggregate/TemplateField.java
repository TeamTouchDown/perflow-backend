package com.touchdown.perflowbackend.Approval.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "template_field", schema = "perflow")
public class TemplateField extends BaseEntity {

    @Id
    @Column(name = "template_field_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long templateFieldId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "template_id", nullable = false)
    private Template templateId;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "type", nullable = false, length = 30)
    private String type;

    @ColumnDefault("0")
    @Column(name = "is_req", nullable = false)
    private Boolean isReq;

    @Column(name = "default_value", length = 30)
    private String defaultValue;

    @Column(name = "delete_datetime")
    private LocalDateTime deleteDatetime;

    @ColumnDefault("'ACTIVATED'")   // todo: builder.default가 아니면 기본값 지정이 안 될 수도 있다고?
    @Column(name = "status", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVATED;

    @Builder
    public TemplateField(Long templateFieldId, Template templateId, String name, String type, Boolean isReq, String defaultValue, LocalDateTime deleteDatetime, Status status) {
        this.templateFieldId = templateFieldId;
        this.templateId = templateId;
        this.name = name;
        this.type = type;
        this.isReq = isReq;
        this.defaultValue = defaultValue;
        this.deleteDatetime = deleteDatetime;
        this.status = status;
    }
}