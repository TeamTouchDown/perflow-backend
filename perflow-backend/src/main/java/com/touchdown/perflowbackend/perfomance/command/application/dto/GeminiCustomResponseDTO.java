package com.touchdown.perflowbackend.perfomance.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeminiCustomResponseDTO {
    private List<Candidate> candidates;

    @Getter
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Candidate {
        private Content content;
        // 필요에 따라 finishReason, safetyRatings, etc. 추가 가능
    }

    @Getter
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Content {
        private String role;
        private List<Part> parts;
    }

    @Getter
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Part {
        private String text;
    }
}
