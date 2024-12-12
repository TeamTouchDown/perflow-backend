package com.touchdown.perflowbackend.hr.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import com.touchdown.perflowbackend.hr.command.application.dto.position.PositionCreateDTO;
import com.touchdown.perflowbackend.hr.command.application.dto.position.PositionUpdateDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "`position`", schema = "perflow")
@SQLDelete(sql = "UPDATE position SET status = 'DELETED' WHERE position_id = ?")
public class Position extends BaseEntity {

    @Id
    @Column(name = "position_id", nullable = false)
    private Long positionId;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "position_level", nullable = false)
    private Integer positionLevel;

    @Column(name = "status", nullable = true)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public Position(PositionCreateDTO positionCreateDTO) {

        this.positionId = positionCreateDTO.getPositionId();
        this.name = positionCreateDTO.getName();
        this.positionLevel = positionCreateDTO.getPositionLevel();
    }

    public void updatePostion(PositionUpdateDTO positionUpdateDTO) {

        this.name = positionUpdateDTO.getName();
        this.positionLevel = positionUpdateDTO.getPositionLevel();
    }
}