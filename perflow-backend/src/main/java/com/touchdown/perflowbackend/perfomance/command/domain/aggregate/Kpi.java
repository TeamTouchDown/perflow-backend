package com.touchdown.perflowbackend.perfomance.command.domain.aggregate;

import com.touchdown.perflowbackend.announcement.command.application.dto.AnnouncementRequestDTO;
import com.touchdown.perflowbackend.common.BaseEntity;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.perfomance.command.application.dto.KPIDetailRequestDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "kpi", schema = "perflow")
public class Kpi extends BaseEntity {

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
    @Enumerated(EnumType.STRING)
    private KpiCurrentStatus status;

    @Column(name = "personal_type", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private PersonalType personalType;

    @Column(name = "goal_detail", nullable = false)
    private String goalDetail;

    @Builder
    public Kpi(Employee emp, String goal, Long goalValue, String goalValueUnit, Double currentValue, KpiCurrentStatus status, PersonalType personalType, String goalDetail ){
        this.emp = emp;
        this.goal = goal;
        this.goalValue = goalValue;
        this.goalValueUnit = goalValueUnit;
        this.currentValue = currentValue;
        this.status = status;
        this.personalType = personalType;
        this.goalDetail = goalDetail;
    }

    // kpi 수정
    public void updateKpi(KPIDetailRequestDTO kpiDetailRequestDTO) {

        this.goal = kpiDetailRequestDTO.getGoal();
        this.goalValue = kpiDetailRequestDTO.getGoalValue();
        this.goalValueUnit = kpiDetailRequestDTO.getGoalValueUnit();
        this.goalDetail = kpiDetailRequestDTO.getGoalDetail();
    }

    // kpi 진척도 업데이트
    public void updateProgress(Double progress) {
        this.currentValue = progress;
    }

    // kpi 상태 업데이트
    public void updateStatus(KpiCurrentStatus status) {
        this.status = status;
    }
}