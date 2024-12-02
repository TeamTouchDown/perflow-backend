package com.touchdown.perflowbackend.teamplate.command.domain.aggregate;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "template", schema = "perflow")
public class Template {
    @Id
    @Column(name = "template_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "create_user_id", nullable = false)
    private Employee createUser;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

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