package com.touchdown.perflowbackend.authority.domain.aggregate;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;

@Getter
@Setter
@Entity
@ToString
@Table(name = "authority_employee", schema = "perflow")
@AllArgsConstructor
public class AuthorityEmployee implements Serializable {
    @EmbeddedId
    private AuthorityEmployeeId id;

    @MapsId("authorityId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "authority_id", nullable = false)
    private Authority authority;

    @MapsId("empId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee emp;

    public AuthorityEmployee() {

    }
    // 파라미터 있는 생성자
    @Builder
    public AuthorityEmployee(Authority authority, Employee emp) {
        this.authority = authority;
        this.emp = emp;
        this.id = new AuthorityEmployeeId(authority.getAuthorityId(), emp.getEmpId());
    }
}