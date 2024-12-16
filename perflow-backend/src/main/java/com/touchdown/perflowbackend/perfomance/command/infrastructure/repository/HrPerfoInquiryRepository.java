package com.touchdown.perflowbackend.perfomance.command.infrastructure.repository;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.HrPerfoInquiry;
import com.touchdown.perflowbackend.perfomance.command.domain.repository.HrPerfoInquiryCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HrPerfoInquiryRepository extends JpaRepository<HrPerfoInquiry, Long> , HrPerfoInquiryCommandRepository {
}
