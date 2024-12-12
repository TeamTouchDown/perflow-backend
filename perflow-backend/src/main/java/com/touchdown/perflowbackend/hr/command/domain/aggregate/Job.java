package com.touchdown.perflowbackend.hr.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import com.touchdown.perflowbackend.hr.command.application.dto.job.JobCreateDTO;
import com.touchdown.perflowbackend.hr.command.application.dto.job.JobUpdateDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "job", schema = "perflow")
public class Job extends BaseEntity {

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

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public Job(JobCreateDTO createDTO, Department dept) {

        this.jobId = createDTO.getJobId();
        this.dept = dept;
        this.name = createDTO.getName();
        this.responsibility = createDTO.getResponsibility();
        this.status = createDTO.getStatus();
    }

    public void updateJob(JobUpdateDTO jobUpdateDTO, Department department) {

        this.dept = department;
        this.name = jobUpdateDTO.getName();
        this.responsibility = jobUpdateDTO.getResponsibility();
        this.status = jobUpdateDTO.getStatus();
    }
}