package com.example.java_crud.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.CascadeType;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "tests")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "teacher_id")
    private Long teacherId;
    private String title;
    private String description;
    private String questionCategory;
    private String difficultyType;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "test_id")
    private List<Question> questions;
    private Boolean isDeleted;
    // for record and history
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;
}
