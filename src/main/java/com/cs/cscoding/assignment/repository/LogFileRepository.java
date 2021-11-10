package com.cs.cscoding.assignment.repository;

import com.cs.cscoding.assignment.entity.LogEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogFileRepository extends JpaRepository<LogEvent, String> {
    List<LogEvent> findByAlert(Boolean alert);
}
