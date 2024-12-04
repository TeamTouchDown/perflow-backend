package com.touchdown.perflowbackend.announcement.command.domain.repository;

import com.touchdown.perflowbackend.announcement.command.domain.aggregate.Announcement;

import java.util.Optional;

public interface AnnouncementCommandRepository {

    Announcement save(Announcement announcement);

    Optional<Announcement> findById(Long annId);

    void deleteById(Long annId);
}
