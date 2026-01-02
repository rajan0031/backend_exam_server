package com.example.java_crud.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.java_crud.dtos.TestDto;
import com.example.java_crud.dtos.GeneralDto.GenResponse;
import com.example.java_crud.dtos.TestDto.GetAllTestDto;
import com.example.java_crud.dtos.TestDto.QuestionOnlyDto;
import com.example.java_crud.model.Test;
import com.example.java_crud.model.TestAnswer;
import com.example.java_crud.model.User;
import com.example.java_crud.repository.TestRepository;
import com.example.java_crud.utils.*;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class TestService {

    private final TestRepository testRepository;
    private final JwtUtils jwtUtils;

    public TestService(TestRepository testRepository, JwtUtils jwtUtils) {
        this.testRepository = testRepository;
        this.jwtUtils = jwtUtils;
    }

    public Test CreateTest(HttpServletRequest request, Test test) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // remove "Bearer "
            User user = jwtUtils.extractUser(token);
            test.setTeacherId(user.getId());
            if (test.getQuestions() != null) {
                test.getQuestions().forEach(q -> q.setTeacherId(user.getId()));
            }
        }
        return testRepository.save(test);
    }

    // get all the tests creaed by a teacherby the id of the teacher

    public List<Test> GetAllTestByTeacherId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        Long teacherId = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // remove "Bearer "
            User user = jwtUtils.extractUser(token);
            teacherId = user.getId();
        }

        System.out.println("the teacher id is " + teacherId);
        List<Test> allTestByTeacherId = testRepository.findByTeacherId(teacherId);
        System.out.println("all the tests for this teacher id: " + allTestByTeacherId);
        return allTestByTeacherId;
    }

    // get all the exams presents in the system without exposing the correct ans to
    // the frontend
    public List<GetAllTestDto> getAllTest() {
        try {
            List<Test> tests = testRepository.findAll();

            return tests.stream().map(test -> {
                GetAllTestDto dto = new GetAllTestDto();
                dto.setTest_id(test.getId());
                dto.setTeacherId(test.getTeacherId());
                dto.setTitle(test.getTitle());
                dto.setDescription(test.getDescription());
                dto.setQuestionCategory(test.getQuestionCategory());
                dto.setDifficultyType(test.getDifficultyType());
                dto.setIsDeleted(test.getIsDeleted());
                dto.setCreatedAt(test.getCreatedAt());
                dto.setUpdatedAt(test.getUpdatedAt());
                dto.setDeletedAt(test.getDeletedAt());

                List<QuestionOnlyDto> questionDtos = test.getQuestions().stream().map(q -> {
                    QuestionOnlyDto qdto = new QuestionOnlyDto();
                    qdto.setQuestion_id(q.getId());
                    qdto.setTeacherId(q.getTeacherId());
                    qdto.setTitle(q.getTitle());
                    qdto.setQuestionCategory(q.getQuestionCategory());
                    qdto.setDifficultyType(q.getDifficultyType());
                    qdto.setOptions(q.getOptions());
                    qdto.setIsDeleted(q.getIsDeleted());
                    qdto.setCreatedAt(q.getCreatedAt());
                    qdto.setUpdatedAt(q.getUpdatedAt());
                    qdto.setDeletedAt(q.getDeletedAt());
                    return qdto;
                }).collect(Collectors.toList());

                dto.setQuestionsOnly(questionDtos);
                return dto;
            }).collect(Collectors.toList());

        } catch (Exception ex) {
            System.err.println("Error fetching tests: " + ex.getMessage());
            throw new RuntimeException("Failed to retrieve tests", ex);
        }
    }

    // submit test in the backend

    public GenResponse SubmitTest(TestDto.testSubmitRequestDto tSubmitRequestDto) {

        // save the test attempt in the database
        TestAnswer testAnswer = new TestAnswer();
        // testAnswer.setTest_id()

        GenResponse genResponse = new GenResponse();
        genResponse.setMessage("The Test ans Subbmitted successfully");
        genResponse.setStatusCode(201);
        return genResponse;

    }

}
