package com.touchdown.perflowbackend.perfomance.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "perfo", schema = "perflow")
public class Perfo extends BaseEntity {

    @Id
    @Column(name = "perfo_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long perfoId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "perfo_question_id", nullable = false)
    private Perfoquestion perfoQuestion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "perfo_emp_id", nullable = false)
    private Employee perfoEmp;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "perfoed_emp_id", nullable = false)
    private Employee perfoedEmp;

    @Column(name = "answer", nullable = false)
    private String answer;

    @Builder
    public Perfo(Perfoquestion perfoQuestion, Employee perfoEmp, Employee perfoedEmp, String answer) {

        this.perfoQuestion = perfoQuestion;
        this.perfoEmp = perfoEmp;
        this.perfoedEmp = perfoedEmp;
        this.answer = answer;
    }

}