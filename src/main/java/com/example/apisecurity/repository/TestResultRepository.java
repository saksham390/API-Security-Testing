package com.example.apisecurity.repository;

import com.example.apisecurity.entity.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestResultRepository extends JpaRepository<TestResult, Long> {
    List<TestResult> findByTestRunId(Long testRunId);
}
