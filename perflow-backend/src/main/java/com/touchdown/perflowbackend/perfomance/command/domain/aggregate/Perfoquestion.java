package com.touchdown.perflowbackend.perfomance.command.domain.aggregate;

import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "perfoquestion", schema = "perflow")
public class Perfoquestion {
    @Id
    @Column(name = "perfo_question_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long perfoQuestionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dept_id", nullable = false)
    private Department dept;

    @Column(name = "question_type", nullable = false, length = 30)
    private String questionType;

    @Column(name = "question_content", nullable = false)
    private String questionContent;

    @Column(name = "perfo_type", nullable = false, length = 30)
    private String perfoType;

    @Column(name = "create_datetime", nullable = false)
    private Instant createDatetime;

    @Column(name = "update_datetime")
    private Instant updateDatetime;

}