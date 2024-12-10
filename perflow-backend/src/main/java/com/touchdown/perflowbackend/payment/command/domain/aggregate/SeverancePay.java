package com.touchdown.perflowbackend.payment.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "severance_pay", schema = "perflow")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeverancePay extends BaseEntity {

    @Id
    @Column(name = "severance_pay_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long severancePayId;

    @Column(name = "name", nullable = false)
    private String name;

}
