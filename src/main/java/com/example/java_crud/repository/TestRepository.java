package com.example.java_crud.repository;

import java.util.List;

import com.example.java_crud.dtos.TestDto;
import com.example.java_crud.model.Test;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {

    // custom query to get the all the tests by teacher id
    List<Test> findByTeacherId(Long teacherId);

}
