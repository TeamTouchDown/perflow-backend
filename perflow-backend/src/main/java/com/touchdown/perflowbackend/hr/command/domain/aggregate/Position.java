package com.touchdown.perflowbackend.hr.command.domain.aggregate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "position", schema = "perflow")
public class Position {
    @Id
    @Column(name = "position_id", nullable = false)
    private Long positionId;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "create_datetime", nullable = false)
    private Instant createDatetime;

    @Column(name = "update_datetime", nullable = false)
    private Instant updateDatetime;

}