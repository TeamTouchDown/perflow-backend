package com.touchdown.perflowbackend.hr.query.repository;

import com.touchdown.perflowbackend.hr.command.domain.aggregate.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyQueryRepository extends JpaRepository<Company, Long> {
}
