package com.touchdown.perflowbackend.approval.command.domain.repository;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.DocShareObj;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocShareObjCommandRepository extends JpaRepository<DocShareObj, Long> {

}
