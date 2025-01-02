package com.touchdown.perflowbackend.payment.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "insurance_rate", schema = "perflow")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InsuranceRate extends BaseEntity {

    @Id
    @Column(name = "insurance_rate_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long insuranceRateId;

    @Column(name = "national_pension_rate", nullable = false)
    private double nationalPensionRate;

    @Column(name = "health_insurance_rate", nullable = false)
    private double healthInsuranceRate;

    @Column(name = "hire_insurance_rate", nullable = false)
    private double hireInsuranceRate;

    @Column(name = "long_term_care_insurance_rate", nullable = false)
    private double longTermCareInsuranceRate;

    @Builder
    public InsuranceRate(double nationalPensionRate, double healthInsuranceRate,
                         double hireInsuranceRate, double longTermCareInsuranceRate) {

        this.nationalPensionRate = nationalPensionRate;
        this.healthInsuranceRate = healthInsuranceRate;
        this.hireInsuranceRate = hireInsuranceRate;
        this.longTermCareInsuranceRate = longTermCareInsuranceRate;
    }

}
