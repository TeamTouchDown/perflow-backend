package com.touchdown.perflowbackend.hr.command.infrastructure.repository;

import com.touchdown.perflowbackend.hr.command.domain.aggregate.Company;
import com.touchdown.perflowbackend.hr.command.domain.repository.CompanyCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCompanyCommandRepository extends JpaRepository<Company, Long>, CompanyCommandRepository {
}
