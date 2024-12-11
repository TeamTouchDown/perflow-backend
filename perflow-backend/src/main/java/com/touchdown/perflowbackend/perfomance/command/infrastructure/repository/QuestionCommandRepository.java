package com.touchdown.perflowbackend.perfomance.command.infrastructure.repository;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.Perfoquestion;
import com.touchdown.perflowbackend.perfomance.command.domain.repository.PerfoQuestionCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionCommandRepository extends JpaRepository<Perfoquestion, Long>, PerfoQuestionCommandRepository {
}
