package com.touchdown.perflowbackend.hr.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "department", schema = "perflow")
public class Department extends BaseEntity {

    @Id
    @Column(name = "dept_id", nullable = false)
    private Long departmentId;

    @ManyToOne(fetch = FetchType.LAZY)  // optional = false 지웠습니다
    @JoinColumn(name = "manage_dept_id")
    private Department manageDept;

    // 양방향 매핑
    @OneToMany(mappedBy = "manageDept", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Department> subDepartments = new ArrayList<>();

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "responsibility", nullable = false)
    private String responsibility;

    @Column(name = "pic", nullable = false, length = 30)
    private String pic;

    @Column(name = "contact", nullable = false, length = 30)
    private String contact;

    @Builder
    public Department(Long departmentId, String name, String responsibility, String pic, String contact, Department manageDept) {
        this.departmentId = departmentId;
        this.name = name;
        this.responsibility = responsibility;
        this.pic = pic;
        this.contact = contact;
        this.manageDept = manageDept;
    }

    public void addSubDepartment(Department subDepartment) {
        subDepartments.add(subDepartment);
        subDepartment.setManageDept(this);
    }

    private void setManageDept(Department manageDept) {
        this.manageDept = manageDept;
    }
}