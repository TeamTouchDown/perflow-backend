package com.touchdown.perflowbackend.approval.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "template", schema = "perflow")
public class Template extends BaseEntity {

    @Id
    @Column(name = "template_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long templateId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "create_user_id", nullable = false)
    private Employee createUser;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @ColumnDefault("'ACTIVATED'")
    @Column(name = "status", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVATED;

    @Builder
    public Template(Long templateId, Employee createUser, String name, String description, Status status) {
        this.templateId = templateId;
        this.createUser = createUser;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public void updateTemplate(String name, String description) {
        if (name != null) {
            this.name = name;
        }
        if (description != null) {
            this.description = description;
        }
    }
}