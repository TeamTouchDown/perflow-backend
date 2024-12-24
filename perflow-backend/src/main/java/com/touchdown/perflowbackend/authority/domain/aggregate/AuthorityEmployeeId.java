package com.touchdown.perflowbackend.authority.domain.aggregate;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class AuthorityEmployeeId implements Serializable {
    private static final long serialVersionUID = -6000207304944550682L;
    @NotNull
    @Column(name = "authority_id", nullable = false)
    private Long authorityId;

    @Size(max = 30)
    @NotNull
    @Column(name = "emp_id", nullable = false, length = 30)
    private String empId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AuthorityEmployeeId entity = (AuthorityEmployeeId) o;
        return Objects.equals(this.authorityId, entity.authorityId) &&
                Objects.equals(this.empId, entity.empId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorityId, empId);
    }

}