package com.touchdown.perflowbackend.file.command.infrastructure;

import com.touchdown.perflowbackend.file.command.domain.aggregate.File;
import com.touchdown.perflowbackend.file.command.domain.repository.FileRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaFileRepository extends JpaRepository<File, Long>, FileRepository {
}
