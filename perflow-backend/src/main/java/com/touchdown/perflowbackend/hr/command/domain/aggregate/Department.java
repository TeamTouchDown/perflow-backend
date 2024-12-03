package com.touchdown.perflowbackend.hr.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "department", schema = "perflow")
public class Department extends BaseEntity {

    @Id
    @Column(name = "dept_id", nullable = false)
    private Long departmentId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "manage_dept_id")
    private Department manageDept;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "responsibility", nullable = false)
    private String responsibility;

    @Column(name = "pic", nullable = false, length = 30)
    private String pic;

    @Column(name = "contact", nullable = false, length = 30)
    private String contact;

}