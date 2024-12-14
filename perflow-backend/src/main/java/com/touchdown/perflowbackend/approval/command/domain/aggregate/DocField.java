package com.touchdown.perflowbackend.approval.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "doc_field", schema = "perflow")
public class DocField extends BaseEntity {

    @Id
    @Column(name = "doc_field_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long docFieldId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doc_id", nullable = false)
    private Doc doc;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "template_field_id", nullable = false)
    private TemplateField templateField;

    @Lob
    @Column(name = "user_value")
    private String userValue;   // 사용자가 입력한 값

    @Column(name = "status", nullable = false)
    private Status status = Status.ACTIVATED;

    @Builder
    public DocField(Doc doc, TemplateField templateField, String userValue) {
        this.doc = doc;
        this.templateField = templateField;
        this.userValue = userValue;
    }

    public void updateUserValue(String userValue) {
        this.userValue = userValue;
    }
}