package com.touchdown.perflowbackend.perfomance.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "hr_perfo", schema = "perflow")
public class HrPerfo extends BaseEntity {

    @Id
    @Column(name = "hr_perfo_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hrPerfoId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee emp;

    @Column(name = "grade", length = 30)
    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Column(name = "score", nullable = false)
    private Double score;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private HrPerfoStatus status;

    @Builder
    public HrPerfo(Employee emp, Double score, HrPerfoStatus status) {
        this.emp = emp;
        this.score = score;
        this.status = status;
    }
}