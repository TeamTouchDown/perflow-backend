package com.touchdown.perflowbackend.hr.command.domain.repository;

import com.touchdown.perflowbackend.hr.command.domain.aggregate.Company;

public interface CompanyCommandRepository {

    Object save(Company company);

    boolean existsById(long l);
}
