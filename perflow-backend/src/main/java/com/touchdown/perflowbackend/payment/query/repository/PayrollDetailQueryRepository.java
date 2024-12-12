package com.touchdown.perflowbackend.payment.query.repository;

import com.touchdown.perflowbackend.payment.command.domain.aggregate.PayrollDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface PayrollDetailQueryRepository extends JpaRepository<PayrollDetail, Long>, JpaSpecificationExecutor<PayrollDetail> {

}
