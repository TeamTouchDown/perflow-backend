package com.touchdown.perflowbackend.perfomance.command.domain.repository;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.Perfo;
import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.Perfoquestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerfoCommandRepository extends JpaRepository<Perfo, Long> {

}
