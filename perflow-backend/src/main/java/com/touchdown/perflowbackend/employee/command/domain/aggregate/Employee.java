package com.touchdown.perflowbackend.employee.command.domain.aggregate;

import com.touchdown.perflowbackend.authority.domain.aggregate.Authority;
import com.touchdown.perflowbackend.common.BaseEntity;
import com.touchdown.perflowbackend.employee.command.application.dto.EmployeeCreateDTO;
import com.touchdown.perflowbackend.employee.command.application.dto.EmployeeUpdateRequestDTO;
import com.touchdown.perflowbackend.employee.command.application.dto.MyInfoUpdateDTO;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Job;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Position;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Slf4j
@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "employee", schema = "perflow")
public class Employee extends BaseEntity {

    @Id
    @Column(name = "emp_id", nullable = false, length = 30)
    private String empId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "position_id", nullable = false)
    private Position position;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dept_id", nullable = false)
    private Department dept;

    @Column(name = "password", nullable = true, length = 255)
    private String password;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "gender", nullable = false, length = 30)
    private String gender;

    @Column(name = "rrn", nullable = false, length = 255)
    private String rrn;

    @Column(name = "pay", nullable = false)
    private Long pay;

    @Column(name = "address", nullable = false, length = 30)
    private String address;

    @Column(name = "contact", nullable = false, length = 30)
    private String contact;

    @Column(name = "email", nullable = false, length = 30)
    private String email;

    @Column(name = "seal", nullable = true)
    private String seal;

    @Column(name = "join_date", nullable = false)
    private LocalDate joinDate;

    @Column(name = "status", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private EmployeeStatus status;

    @Column(name = "resign_date")
    private LocalDate resignDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "authority_employee",
            joinColumns = @JoinColumn(name = "emp_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id"))
    private Set<Authority> authorities = new LinkedHashSet<>();

    @Builder
    public Employee(EmployeeCreateDTO registerDTO, Position position, Job job, Department department, String rrn) {
        this.empId = registerDTO.getEmpId();
        this.position = position;
        this.job = job;
        this.dept = department;
        this.name = registerDTO.getName();
        this.gender = registerDTO.getGender();
        this.rrn = rrn;
        this.pay = registerDTO.getPay();
        this.address = registerDTO.getAddress();
        this.contact = registerDTO.getContact();
        this.email = registerDTO.getEmail();
        this.joinDate = registerDTO.getJoinDate();
        this.status = EmployeeStatus.PENDING;
    }

    public void updateEmployee(EmployeeUpdateRequestDTO updateRequestDTO) {

        this.name = updateRequestDTO.getName();
        this.pay = updateRequestDTO.getPay();
        this.address = updateRequestDTO.getAddress();
        this.contact = updateRequestDTO.getContact();
        this.email = updateRequestDTO.getEmail();
        this.joinDate = updateRequestDTO.getJoinDate();
    }

    public void updateMyInfo(MyInfoUpdateDTO updateDTO) {

        this.address = updateDTO.getAddress();
        this.contact = updateDTO.getContact();
        this.email = updateDTO.getEmail();
    }

    public void registerPassword(String password) {
        this.password = password;
    }

    public void updateStatus(EmployeeStatus status) {
        this.status = status;
    }

    public void updateSeal(String seal) { this.seal = seal; }

    public void updatePosition(Position position) {this.position = position; }

    public void updateDepartment(Department dept) { this.dept = dept; }

    public void updateJob(Job job) { this.job = job; }

}