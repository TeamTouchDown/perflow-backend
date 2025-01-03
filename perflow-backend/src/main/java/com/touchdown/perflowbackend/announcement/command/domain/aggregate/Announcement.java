package com.touchdown.perflowbackend.announcement.command.domain.aggregate;

import com.touchdown.perflowbackend.announcement.command.application.dto.AnnouncementRequestDTO;
import com.touchdown.perflowbackend.common.BaseEntity;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "announcement", schema = "perflow")
@SQLDelete(sql = "UPDATE announcement SET status = 'DELETED' WHERE ann_id = ?")
@Where(clause = "status != 'DELETED'")
public class Announcement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ann_id", nullable = false)
    private Long annId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dept_id", nullable = false)
    private Department dept;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee emp;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @ColumnDefault("SAVED")
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.SAVED;

    @Builder
    public Announcement(Long annId, Department dept, Employee emp, String title, String content) {
        this.annId = annId;
        this.dept = dept;
        this.emp = emp;
        this.title = title;
        this.content = content;
    }

    public void updateAnnouncement(AnnouncementRequestDTO announcementRequestDTO) {
        this.title = announcementRequestDTO.getTitle();
        this.content = announcementRequestDTO.getContent();
    }
}