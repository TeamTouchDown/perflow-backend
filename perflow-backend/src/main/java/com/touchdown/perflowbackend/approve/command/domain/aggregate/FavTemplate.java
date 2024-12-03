package com.touchdown.perflowbackend.approve.command.domain.aggregate;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "fav_template", schema = "perflow")
public class FavTemplate {
    @Id
    @Column(name = "fav_template_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "template_id", nullable = false)
    private Template template;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee emp;

    @Column(name = "fav_datetime", nullable = false)
    private LocalDateTime favDatetime;

}