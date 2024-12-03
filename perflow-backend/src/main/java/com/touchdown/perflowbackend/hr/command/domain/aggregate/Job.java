package com.touchdown.perflowbackend.hr.command.domain.aggregate;

import com.touchdown.perflowbackend.department.command.domain.aggregate.Department;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "job", schema = "perflow")
public class Job {
    @Id
    @Column(name = "job_id", nullable = false)
    private Long jobId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dept_id", nullable = false)
    private Department dept;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "responsibility", nullable = false)
    private String responsibility;

    @ColumnDefault("1")
    @Column(name = "is_active", nullable = false)
    private Byte isActive;

    @Column(name = "create_datetime", nullable = false)
    private Instant createDatetime;

    @Column(name = "update_datetime", nullable = false)
    private Instant updateDatetime;

}