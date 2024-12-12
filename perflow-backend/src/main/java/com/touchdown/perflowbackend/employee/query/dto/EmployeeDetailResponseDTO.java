package com.touchdown.perflowbackend.employee.query.dto;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.EmployeeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class EmployeeDetailResponseDTO {

    private String empId;

    private Long positionId;

    private String positionName;

    private Long jobId;

    private String jobName;

    private Long deptId;

    private String deptName;

    private String name;

    private String gender;

    private String rrn; // 주민등록번호

    private Long pay;

    private String address;

    private String contact;

    private String email;

    private EmployeeStatus Status;

    private LocalDate joinDate;

    @Builder
    public EmployeeDetailResponseDTO(Employee emp) {

        this.empId = emp.getEmpId();
        this.positionId = emp.getPosition().getPositionId();
        this.positionName = emp.getPosition().getName();
        this.jobId = emp.getJob().getJobId();
        this.jobName = emp.getJob().getName();
        this.deptId = emp.getDept().getDepartmentId();
        this.deptName = emp.getDept().getName();
        this.name = emp.getName();
        this.gender = emp.getGender();
        this.rrn = emp.getRrn();
        this.pay = emp.getPay();
        this.address = emp.getAddress();
        this.contact = emp.getContact();
        this.email = emp.getEmail();
        this.Status = emp.getStatus();
        this.joinDate = emp.getJoinDate();
    }
}
