package com.touchdown.perflowbackend.templatefield.command.domain.aggregate;

import com.touchdown.perflowbackend.teamplate.command.domain.aggregate.Template;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "template_field", schema = "perflow")
public class TemplateField {
    @Id
    @Column(name = "template_field_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "template_id", nullable = false)
    private Template template;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "type", nullable = false, length = 30)
    private String type;

    @ColumnDefault("0")
    @Column(name = "is_req", nullable = false)
    private Byte isReq;

    @Column(name = "default_value", length = 30)
    private String defaultValue;

    @Column(name = "create_datetime", nullable = false)
    private Instant createDatetime;

    @Column(name = "update_datetime")
    private Instant updateDatetime;

    @Column(name = "delete_datetime")
    private Instant deleteDatetime;

    @ColumnDefault("'ACTIVATED'")
    @Column(name = "status", nullable = false, length = 30)
    private String status;

}