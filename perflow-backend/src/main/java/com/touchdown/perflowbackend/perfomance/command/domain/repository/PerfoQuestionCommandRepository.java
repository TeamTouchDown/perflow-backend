package com.touchdown.perflowbackend.perfomance.command.domain.repository;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.Perfoquestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerfoQuestionCommandRepository extends JpaRepository<Perfoquestion, Long> {

    Perfoquestion findByperfoQuestionId(Long perfoQuestionId);

    List<Perfoquestion> findByDept_departmentId(Long deptId);
}
