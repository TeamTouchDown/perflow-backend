package com.touchdown.perflowbackend.announcement.query.repository;

import com.touchdown.perflowbackend.announcement.command.domain.aggregate.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementQueryRepository extends JpaRepository<Announcement, Long> {

    Page<Announcement> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<Announcement> findByDeptName(String deptName, Pageable pageable);
}
