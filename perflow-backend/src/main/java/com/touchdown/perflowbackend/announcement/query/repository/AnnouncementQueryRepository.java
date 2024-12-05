package com.touchdown.perflowbackend.announcement.query.repository;

import com.touchdown.perflowbackend.announcement.command.domain.aggregate.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementQueryRepository extends JpaRepository<Announcement, Long> {
}
