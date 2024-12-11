package com.touchdown.perflowbackend.perfomance.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.perfomance.command.application.dto.UpdateQuestionRequestDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "perfoquestion", schema = "perflow")
public class Perfoquestion extends BaseEntity {

    @Id
    @Column(name = "perfo_question_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long perfoQuestionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dept_id", nullable = false)
    private Department dept;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee emp;

    @Column(name = "question_type", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @Column(name = "question_content", nullable = false)
    private String questionContent;

    @Column(name = "perfo_type", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private PerfoType perfoType;

    @Builder
    public Perfoquestion(Department dept, Employee emp, QuestionType questionType, String questionContent, PerfoType perfoType) {
        this.dept = dept;
        this.emp = emp;
        this.questionType = questionType;
        this.questionContent = questionContent;
        this.perfoType = perfoType;
    }

    public void updateQuestion(Employee emp, UpdateQuestionRequestDTO updateQuestionRequestDTO) {
        this.emp = emp;
        this.questionContent = updateQuestionRequestDTO.getQuestionContext();
        this.questionType = QuestionType.valueOf(updateQuestionRequestDTO.getQuestionType());
    }
}