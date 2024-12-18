package com.touchdown.perflowbackend.perfomance.command.application.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.perfomance.command.application.dto.GeminiCustomRequestDTO;
import com.touchdown.perflowbackend.perfomance.command.application.dto.GeminiCustomResponseDTO;
import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.*;
import com.touchdown.perflowbackend.perfomance.command.domain.repository.AiPerfoSummaryCommandRepository;
import com.touchdown.perflowbackend.perfomance.command.domain.repository.PerfoCommandRepository;
import com.touchdown.perflowbackend.perfomance.command.domain.repository.PerfoQuestionCommandRepository;
import com.touchdown.perflowbackend.perfomance.command.mapper.PerformanceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AISummaryService {

    private final EmployeeCommandRepository employeeCommandRepository;
    private final PerfoQuestionCommandRepository perfoQuestionCommandRepository;
    private final PerfoCommandRepository perfoCommandRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AiPerfoSummaryCommandRepository aiPerfoSummaryCommandRepository;

    {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.api.model}")
    private String apiModel;

    // AI Summary 생성
    @Transactional
    public void createAISummary(String empId){

        // 피평가자 정보 확인하기
        Employee Emp = findEmployeeByEmpId(empId);

        // 평가자 부서의 평가 문항 가져오기
        List<Perfoquestion> perfoQuestions = findPerfoQuestionByDeptId(Emp.getDept().getDepartmentId());

        // AI에 전송할 문장 생성하기
        List<String> inputs = createAiInput(perfoQuestions,empId);

        // 동료 평가 AI 요약 받아오기
        String colSummary = getAIResponse(inputs.get(0));

        // 하향 평가 AI 요약 받아오기
        String downSummary = getAIResponse(inputs.get(1));

        // 동료 평가 AI 요약 생성
        AiPerfoSummary colAiSummary = PerformanceMapper.createAiSummary(Emp, PerfoType.COL, colSummary);

        // 하향 평가 AI 요약 생성
        AiPerfoSummary downAiSummary = PerformanceMapper.createAiSummary(Emp, PerfoType.DOWN, downSummary);

        // 동료 평가 AI 요약 저장
        aiPerfoSummaryCommandRepository.save(colAiSummary);

        // 하향 평가 AI 요약 저장
        aiPerfoSummaryCommandRepository.save(downAiSummary);
    }

    // 받아온 EMP id를 이용해 EMP 정보 불러오기
    private Employee findEmployeeByEmpId(String empId) {
        return employeeCommandRepository.findById(empId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMP));
    }

    // 받아온 Dept id를 이용해 PerfoQuestion 정보 불러오기
    private List<Perfoquestion> findPerfoQuestionByDeptId(Long deptId) {
        List<Perfoquestion> perfoQuestions = perfoQuestionCommandRepository.findByDept_departmentId(deptId);
        if (perfoQuestions.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_PERFOQUESTION);
        }
        return perfoQuestions;
    }

    // 받아온 questionId와 perfoedempId 를 통해 문제 답변 조회하기
    private List<Perfo> findAnswersByquestionIdAndperfoedempId (Long questionId, String perfoedempId) {
        List<Perfo> answers = perfoCommandRepository.findByPerfoQuestion_perfoQuestionIdAndPerfoedEmp_EmpId(questionId,perfoedempId);
        if (answers.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_PERFOQUESTION_ANSWER);
        }
        return answers;
    }

    // 최종 문자열만 추출하는 헬퍼 메서드
    public String extractText(GeminiCustomResponseDTO geminiCustomResponseDTO) {
        if (geminiCustomResponseDTO.getCandidates() == null || geminiCustomResponseDTO.getCandidates().isEmpty()) {
            return null;
        }
        GeminiCustomResponseDTO.Candidate candidate = geminiCustomResponseDTO.getCandidates().get(0);
        if (candidate.getContent() == null || candidate.getContent().getParts() == null || candidate.getContent().getParts().isEmpty()) {
            return null;
        }
        return candidate.getContent().getParts().get(0).getText();
    }

    // 받아온 문항 정보에 답변을 불러와 문장 형식으로 생성하기
    private List<String> createAiInput(List<Perfoquestion> perfoQuestions, String empId) {
        // perfo_type에 따른 문자열 빌더 초기화
        StringBuilder peerEvaluationBuilder = new StringBuilder();
        StringBuilder downwardEvaluationBuilder = new StringBuilder();

        // AI에 응답 요청할 문장 생성
        for (Perfoquestion question : perfoQuestions) {
            // 객관식 문제 여과하기
            if (question.getQuestionType() == QuestionType.SUBJECTIVE) {
                // 동료 평가와 하향 평가로 나누기
                if (question.getPerfoType() == PerfoType.COL) {
                    peerEvaluationBuilder.append("문제: ").append(question.getQuestionContent()).append("\n");

                    // 해당 질문에 대한 답변 조회
                    List<Perfo> answers = findAnswersByquestionIdAndperfoedempId(question.getPerfoQuestionId(),empId);
                    for (int i = 0; i < answers.size(); i++) {
                        peerEvaluationBuilder.append("답변").append(i + 1).append(": ").append(answers.get(i).getAnswer()).append("\n");
                    }
                    peerEvaluationBuilder.append("\n"); // 문제와 답변 사이에 공백 추가
                } else if (question.getPerfoType() == PerfoType.DOWN) {
                    downwardEvaluationBuilder.append("문제: ").append(question.getQuestionContent()).append("\n");

                    // 해당 질문에 대한 답변 조회
                    List<Perfo> answers = findAnswersByquestionIdAndperfoedempId(question.getPerfoQuestionId(),empId);
                    for (int i = 0; i < answers.size(); i++) {
                        downwardEvaluationBuilder.append("답변").append(i + 1).append(": ").append(answers.get(i).getAnswer()).append("\n");
                    }
                    downwardEvaluationBuilder.append("\n"); // 문제와 답변 사이에 공백 추가
                }
            }
        }

        // 최종 문자열 생성
        return List.of(
                peerEvaluationBuilder.toString(),
                downwardEvaluationBuilder.toString()
        );
    }

    // 요청 만들기
    private GeminiCustomRequestDTO buildRequest(String input) {
        // contents 배열 생성
        GeminiCustomRequestDTO.ContentItem userContent1 = new GeminiCustomRequestDTO.ContentItem(
                "user",
                Collections.singletonList(new GeminiCustomRequestDTO.Part("내가 입력하는 특정 사람에 대해 남겨진 평가를 요약해줘\n" +
                        "맨 처음에는 그 사원의 이름이 주어질거고, 그 뒤에는 평가 질문 내용, 그 뒤에는 그 질문에 대한 대답들이 주어질거야\n" +
                        "이를 아래 조건에 따라 요약해줘\n" +
                        "요약 해야 하는 조건은 다음과 같아\n" +
                        "- 100자 이상, 300자 이내\n" +
                        "- 여러 가지 문항과 그 문항에 대한 답변들을 모두 하나로 모아서 요약해줘\n" +
                        "- 너는 추가 정보를 요청한 권리가 없으니 반드시 지금 있는 정보 안에서 답변해야해\n" +
                        "- 평가 마지막은 ~~ 인것이 평가입니다. 라는 느낌으로 마무리해줘"))
        );

        GeminiCustomRequestDTO.ContentItem userContent2 = new GeminiCustomRequestDTO.ContentItem(
                "user",
                Collections.singletonList(new GeminiCustomRequestDTO.Part(input))
        );

        List<GeminiCustomRequestDTO.ContentItem> contents = List.of(userContent1, userContent2);

        // generationConfig 생성
        GeminiCustomRequestDTO.GenerationConfig generationConfig = new GeminiCustomRequestDTO.GenerationConfig(
                0.7,     // temperature
                40,      // topK
                0.95,    // topP
                512      // maxOutputTokens
        );

        return new GeminiCustomRequestDTO(contents, generationConfig);
    }


    @Transactional
    public String getAIResponse(String input) {
        // 요청 DTO 만들기
        GeminiCustomRequestDTO requestBody = buildRequest(input);

        // 실제 API Endpoint
        String endpoint = apiUrl + "?key=" + apiKey;

        try {
            // 요청 전 로그 출력 (디버깅 용도)
            String requestJson = objectMapper.writeValueAsString(requestBody);

            // HTTP POST 요청 보내기
            GeminiCustomResponseDTO response = restTemplate.postForObject(endpoint, requestBody, GeminiCustomResponseDTO.class);

            // 결과 처리
            if (response != null) {
                String extractedText = extractText(response);
                if (extractedText != null) {
                    return extractedText;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new CustomException(ErrorCode.NOT_FOUND_AI_RESPONSE);
    }
}
