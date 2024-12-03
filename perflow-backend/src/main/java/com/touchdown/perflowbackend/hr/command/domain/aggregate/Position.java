package com.touchdown.perflowbackend.hr.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "position", schema = "perflow")
public class Position extends BaseEntity {

    @Id
    @Column(name = "position_id", nullable = false)
    private Long positionId;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "level", nullable = false)
    private Integer level;

}