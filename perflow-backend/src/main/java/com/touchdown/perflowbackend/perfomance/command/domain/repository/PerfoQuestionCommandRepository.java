package com.touchdown.perflowbackend.perfomance.command.domain.repository;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.Perfoquestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerfoQuestionCommandRepository extends JpaRepository<Perfoquestion, Long> {

    Perfoquestion findByperfoQuestionId(Long perfoQuestionId);
}
