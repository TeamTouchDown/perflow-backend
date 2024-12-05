package com.touchdown.perflowbackend.approval.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "field_type", schema = "perflow")
public class FieldType {

    @Id
    @Column(name = "field_type_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fieldTypeId;

    @Column(name = "type", nullable = false)
    private String type;

    @Lob
    @Column(name = "details", nullable = false)
    private String details;

    @OneToMany(mappedBy = "fieldType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TemplateField> templateFields;

}
