package com.touchdown.perflowbackend.menu.query.application.repository;

import com.touchdown.perflowbackend.menu.command.domain.aggregate.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuQueryRepository extends JpaRepository<Menu, Long> {
}
