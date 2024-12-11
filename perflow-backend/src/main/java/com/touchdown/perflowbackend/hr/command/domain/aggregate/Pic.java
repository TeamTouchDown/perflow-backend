package com.touchdown.perflowbackend.hr.command.domain.aggregate;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "pic", schema = "perflow")
public class Pic { // 부서 담당자

    @Id
    @Column(name = "dept_id")
    private Long deptId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "dept_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "emp_id")
    private Employee employee;

    @Builder
    public Pic(Department department, Employee employee) {

        this.department = department;
        this.employee = employee;
    }
}