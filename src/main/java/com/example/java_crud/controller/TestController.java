package com.example.java_crud.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.java_crud.dtos.TestDto;
import com.example.java_crud.model.Test;
import com.example.java_crud.service.TestService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/test")
public class TestController {

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @PostMapping("/create")
    public Test CreateTest(HttpServletRequest httpServletRequest, @RequestBody Test test) {

        var val = testService.CreateTest(httpServletRequest, test);

        return val;
    }

    // get all th etst in the database by its teacher id

    @GetMapping("/alltestby-teacherid")
    public List<Test> GetAllTestByTeacherId(HttpServletRequest request) {
        return testService.GetAllTestByTeacherId(request);
    }

    // get all the tests without ans

    @GetMapping("/alltest")
    public List<TestDto.GetAllTestDto> GetAllTest() {
        return testService.getAllTest();
    }

}
