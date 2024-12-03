package com.touchdown.perflowbackend.file.command.domain.aggregate;

import com.touchdown.perflowbackend.announcement.command.domain.aggregate.Announcement;
import com.touchdown.perflowbackend.approve.command.domain.aggregate.Doc;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "file", schema = "perflow")
public class File {
    @Id
    @Column(name = "file_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_id")
    private Doc doc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ann_id")
    private Announcement ann;

    @Column(name = "origin_name", nullable = false)
    private String originName;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "url")
    private String url;

    @Column(name = "type", nullable = false, length = 10)
    private String type;

    @Column(name = "size", nullable = false)
    private Integer size;

    @Column(name = "upload_datetime", nullable = false)
    private Instant uploadDatetime;

    @Column(name = "attach_datetime", nullable = false)
    private Instant attachDatetime;

    @Column(name = "delete_datetime")
    private Instant deleteDatetime;

    @ColumnDefault("'SAVED'")
    @Column(name = "status", nullable = false, length = 30)
    private String status;

}