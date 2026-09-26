package com.example.apisecurity.repository;

import com.example.apisecurity.entity.TestRun;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestRunRepository extends JpaRepository<TestRun, Long> {
    List<TestRun> findByProjectIdOrderByStartedAtDesc(Long projectId);
}
