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
public class GeminiCustomRequestDTO {

    private List<ContentItem> contents;
    private GenerationConfig generationConfig;

    @Getter
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContentItem {
        private String role;          // "user", "system", etc.
        private List<Part> parts;     // 여러개의 part 가능
    }

    @Getter
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Part {
        private String text;          // 실제 텍스트
    }

    @Getter
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GenerationConfig {
        private Double temperature;
        private Integer topK;
        private Double topP;
        private Integer maxOutputTokens;
    }
}