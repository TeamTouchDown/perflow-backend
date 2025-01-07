package com.touchdown.perflowbackend.file.query.repository;

import com.touchdown.perflowbackend.announcement.command.domain.aggregate.Announcement;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.Doc;
import com.touchdown.perflowbackend.file.command.domain.aggregate.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileQueryRepository extends JpaRepository<File, Long> {

    List<File> findByDoc(Doc doc);

    List<File> findByAnn(Announcement announcement);
}
