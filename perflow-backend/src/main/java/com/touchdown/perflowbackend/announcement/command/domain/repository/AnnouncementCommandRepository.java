package com.touchdown.perflowbackend.announcement.command.domain.repository;

import com.touchdown.perflowbackend.announcement.command.domain.aggregate.Announcement;

public interface AnnouncementCommandRepository {

    void save(Announcement announcement);
}
