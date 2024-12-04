package com.touchdown.perflowbackend.perfomance.command.domain.aggregate;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "kpi", schema = "perflow")
public class Kpi {
    @Id
    @Column(name = "kpi_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kpiId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee emp;

    @Column(name = "goal", nullable = false)
    private String goal;

    @Column(name = "goal_value", nullable = false)
    private Long goalValue;

    @Column(name = "goal_value_unit", nullable = false, length = 30)
    private String goalValueUnit;

    @ColumnDefault("0")
    @Column(name = "current_value", nullable = false)
    private Double currentValue;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "personal_type", nullable = false, length = 30)
    private String personalType;

    @Column(name = "goal_detail", nullable = false)
    private String goalDetail;

    @Column(name = "create_datetime", nullable = false)
    private Instant createDatetime;

    @Column(name = "update_datetime")
    private Instant updateDatetime;

}