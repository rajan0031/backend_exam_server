package com.example.java_crud.dtos;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class TestDto {

    // Question dto goes here without correct ans

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class QuestionOnlyDto {
        private Long question_id;
        private Long teacherId;
        private String title;
        private String questionCategory;
        private String difficultyType;
        private List<String> options;
        private Boolean isDeleted;
        // for record and history
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDateTime deletedAt;
    }

    // test GetAllTestDto
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class GetAllTestDto {

        private Long test_id;
        private Long teacherId;
        private String title;
        private String description;
        private String questionCategory;
        private String difficultyType;
        private List<QuestionOnlyDto> questionsOnly;
        private Boolean isDeleted;

        // for record and history

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

        private LocalDateTime deletedAt;

    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class questionAnswerPairDto {
        private Long question_id;
        private String ans;
    }

    // testSubmittion request
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class testSubmitRequestDto {
        private Long test_id;
        private List<questionAnswerPairDto> quAnswerPairDto;
    }

}
