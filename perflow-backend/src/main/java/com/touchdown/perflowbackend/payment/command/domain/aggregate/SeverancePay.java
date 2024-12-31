package com.touchdown.perflowbackend.payment.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "severancePay", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SeverancePayDetail> severancePayDetailList = new ArrayList<>();

    // SeverancePay 객체 생성 시 name을 전달하는 생성자
    public SeverancePay(String name) {
        this.name = name;
    }

    // SeverancePayDetail을 추가하는 메서드
    public void addSeverancePayDetail(SeverancePayDetail severancePayDetail) {

        severancePayDetail.assignSeverancePay(this);

        this.severancePayDetailList.add(severancePayDetail);

    }
}
