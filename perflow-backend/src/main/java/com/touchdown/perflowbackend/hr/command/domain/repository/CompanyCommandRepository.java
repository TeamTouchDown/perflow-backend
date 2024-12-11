package com.touchdown.perflowbackend.hr.command.domain.repository;

import com.touchdown.perflowbackend.hr.command.domain.aggregate.Company;

import java.util.Optional;

public interface CompanyCommandRepository {

    Object save(Company company);

    boolean existsById(long l);

    Optional<Company> findById(Long companyId);
}
