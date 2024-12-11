package com.touchdown.perflowbackend.hr.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.hr.command.application.dto.department.DepartmentCreateDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
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

    @Column(name = "contact", nullable = false, length = 30)
    private String contact;

    @OneToOne(mappedBy = "department", cascade = CascadeType.ALL)
    private Pic pic;

    @Builder
    public Department(DepartmentCreateDTO createDTO, Department manageDept, Pic pic) {
        this.departmentId = createDTO.getDepartmentId();
        this.name = createDTO.getName();
        this.responsibility = createDTO.getResponsibility();
        this.contact = createDTO.getContact();
        this.manageDept = manageDept;
        this.pic = pic;
    }

    public void addPic(Pic pic) {
        this.pic = pic;
    }

    public void addSubDepartment(Department subDepartment) {
        subDepartments.add(subDepartment);
        subDepartment.setManageDept(this);
    }

    private void setManageDept(Department manageDept) {
        this.manageDept = manageDept;
    }
}