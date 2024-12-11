package com.touchdown.perflowbackend.hr.command.infrastructure.repository;

import com.touchdown.perflowbackend.hr.command.domain.aggregate.Pic;
import com.touchdown.perflowbackend.hr.command.domain.repository.PicCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPicCommandRepository extends PicCommandRepository, JpaRepository<Pic, Long> {
}
