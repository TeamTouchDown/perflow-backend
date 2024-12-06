package com.touchdown.perflowbackend.approval.command.domain.aggregate;

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
    private Template template;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "field_type_id", nullable = false)
    private FieldType fieldType;

    @Lob
    @Column(name = "field_order", nullable = false)
    private Long fieldOrder;

    @Column(name = "details", nullable = false)
    private String details = "{}";

    @Column(name = "is_repeated", nullable = false)
    private Boolean isRepeated = false;

    @Column(name = "delete_datetime")
    private LocalDateTime deleteDatetime;

    @ColumnDefault("'ACTIVATED'")   // todo: builder.default가 아니면 기본값 지정이 안 될 수도 있다고?
    @Column(name = "status", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVATED;

    @Builder
    public TemplateField(Long templateFieldId, Template template, FieldType fieldType, String details, Boolean isRepeated, Long fieldOrder, LocalDateTime deleteDatetime, Status status) {
        this.templateFieldId = templateFieldId;
        this.template = template;
        this.fieldType = fieldType;
        this.details = details;
        this.isRepeated = isRepeated;
        this.fieldOrder = fieldOrder;
        this.deleteDatetime = deleteDatetime;
        this.status = status;
    }


    public void updateField(TemplateField updatedField) {
        this.fieldOrder = updatedField.fieldOrder;
        this.details = updatedField.details;
        this.isRepeated = updatedField.isRepeated;
    }
}