package com.touchdown.perflowbackend.perfomance.command.domain.repository;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.HrPerfoInquiry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HrPerfoInquiryCommandRepository extends JpaRepository<HrPerfoInquiry, Long> {

    Optional<HrPerfoInquiry> findByhrPerfoInquiryId(Long hrPerfoInquiryId);

}
