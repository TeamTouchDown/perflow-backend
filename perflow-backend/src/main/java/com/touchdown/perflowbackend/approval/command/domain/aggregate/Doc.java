package com.touchdown.perflowbackend.approval.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "doc", schema = "perflow")
public class Doc extends BaseEntity {

    @Id
    @Column(name = "doc_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long docId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "create_user_id", nullable = false)
    private Employee createUser;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "template_id", nullable = false)
    private Template template;

    @OneToMany(mappedBy = "doc", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT) // approveLines, shares 2개의 컬렉션을 동시에 join fetch 하면 뭐 부터 로드할지 몰라서 오류 발생 -> subselect 적용 후 join fetch 제거
    private List<ApproveLine> approveLines = new ArrayList<>();

    @OneToMany(mappedBy = "doc", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private List<DocShareObj> shares = new ArrayList<>();

    @Column(name = "title", nullable = false, length = 50)
    private String title;

//    @Lob
//    @Column(name = "content", nullable = false)
//    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private Status status = Status.ACTIVATED;

    @Column(name = "collect_datetime")
    private LocalDateTime collectDatetime;

    @Column(name = "draft_datetime")
    private LocalDateTime draftDatetime;

    @Builder
    public Doc(String title, Template template, Employee createUser) {

        this.title = title;
        this.template = template;
        this.createUser = createUser;
    }

    public void updateStatus(Status status) {

        this.status = status;
    }

    public void setApproveLines(List<ApproveLine> approveLines) {

        this.approveLines.clear();
        if (approveLines != null) {
            this.approveLines.addAll(approveLines); // 새로운 데이터 추가
        }
    }
}