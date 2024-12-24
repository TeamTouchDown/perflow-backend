package com.touchdown.perflowbackend.security.util;

import com.touchdown.perflowbackend.authority.domain.aggregate.Authority;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.EmployeeStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class CustomEmployDetail implements UserDetails {

    private final Employee employee;

    public CustomEmployDetail(Employee employee) {

        this.employee = employee;
    }

    public String getEmployeeName() {
        return employee.getName();
    }

    public List<Long> getAuthorityIds() {
        List<Long> authorityIds = employee.getAuthorities().stream()
                .map(Authority::getAuthorityId)
                .collect(Collectors.toList());
        log.info("Authority IDs: {}", authorityIds);
        return authorityIds;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<? extends GrantedAuthority> authorities = employee.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority("ROLE_" + authority.getType().name()))
                .collect(Collectors.toSet());
        log.info("Authorities: {}", authorities);

        return authorities;
    }

    @Override
    public String getPassword() {
        return employee.getPassword();
    }

    @Override
    public String getUsername() {
        return employee.getEmpId();
    }

    public EmployeeStatus getStatus() {
        return employee.getStatus();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
