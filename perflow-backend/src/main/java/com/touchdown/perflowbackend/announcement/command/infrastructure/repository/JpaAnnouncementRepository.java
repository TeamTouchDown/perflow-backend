package com.touchdown.perflowbackend.announcement.command.infrastructure.repository;

import com.touchdown.perflowbackend.announcement.command.domain.aggregate.Announcement;
import com.touchdown.perflowbackend.announcement.command.domain.repository.AnnouncementCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAnnouncementRepository extends JpaRepository<Announcement, Long>, AnnouncementCommandRepository {
}
