package com.touchdown.perflowbackend.announcement.query.repository;

import com.touchdown.perflowbackend.announcement.command.domain.aggregate.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnnouncementQueryRepository extends JpaRepository<Announcement, Long> {

    Page<Announcement> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query("SELECT a FROM Announcement a " +
            "JOIN a.emp e " +
            "WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :empName, '%'))")
    Page<Announcement> findByEmpName(@Param("empName") String empName, Pageable pageable);
}
