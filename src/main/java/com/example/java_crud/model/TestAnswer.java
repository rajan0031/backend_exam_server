package com.example.java_crud.model;

import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.java_crud.dtos.TestDto.testSubmitRequestDto;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "testanswer")
@Data
@EntityListeners(AuditingEntityListener.class)
public class TestAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long test_id;
    private List<testSubmitRequestDto> tSubmitRequestDtos;

}
