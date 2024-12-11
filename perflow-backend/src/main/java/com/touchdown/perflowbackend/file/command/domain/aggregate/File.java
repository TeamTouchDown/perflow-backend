package com.touchdown.perflowbackend.file.command.domain.aggregate;

import com.touchdown.perflowbackend.announcement.command.domain.aggregate.Announcement;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.Doc;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "file", schema = "perflow")
@SQLDelete(sql = "UPDATE file SET status = 'DELETED', delete_datetime = now() WHERE file_id = ?")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id", nullable = false)
    private Long fileId;

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
    @Enumerated(EnumType.STRING)
    private FileType type;

    @Column(name = "size", nullable = false)
    private Integer size;

    @CreatedDate
    @Column(name = "upload_datetime", nullable = false)
    private LocalDateTime uploadDatetime;

    @LastModifiedDate
    @Column(name = "delete_datetime")
    private LocalDateTime deleteDatetime;

    @ColumnDefault("SAVED")
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private FileStatus status = FileStatus.SAVED;

    @Builder
    public File(Long fileId, Doc doc, Announcement ann,
                String originName, String fileName, String url,
                FileType type, Integer size) {
        this.fileId = fileId;
        this.doc = doc;
        this.ann = ann;
        this.originName = originName;
        this.fileName = fileName;
        this.url = url;
        this.type = type;
        this.size = size;
    }
}