package com.touchdown.perflowbackend.hr.command.domain.aggregate;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.hr.command.application.dto.Appoint.AppointCreateDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "appoint", schema = "perflow")
public class Appoint {

    @Id
    @Column(name = "appoint_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee emp;

    @Column(name = "type", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "`before`", nullable = false, length = 30)
    private String before;

    @Column(name = "after", nullable = false, length = 30)
    private String after;

    @Column(name = "appoint_date", nullable = false)
    private LocalDate appointDate;

    @Builder
    public Appoint(AppointCreateDTO appointCreateDTO, String after, String before, Employee emp) {

        this.emp = emp;
        this.type = appointCreateDTO.getType();
        this.before = before;
        this.after = after;
        this.appointDate = appointCreateDTO.getAppointDate();
    }


}