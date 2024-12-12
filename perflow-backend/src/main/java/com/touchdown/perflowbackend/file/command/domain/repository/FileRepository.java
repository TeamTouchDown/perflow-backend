package com.touchdown.perflowbackend.file.command.domain.repository;

import com.touchdown.perflowbackend.announcement.command.domain.aggregate.Announcement;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.Doc;
import com.touchdown.perflowbackend.file.command.domain.aggregate.File;

import java.util.List;
import java.util.Optional;

public interface FileRepository {

    File save(File file);

    void delete(File file);

    List<File> findByDoc(Doc doc);

    List<File> findByAnn(Announcement announcement);

    Optional<File> findById(Long fileId);
}
