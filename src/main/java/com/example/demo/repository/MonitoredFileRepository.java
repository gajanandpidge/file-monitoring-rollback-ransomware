package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.MonitoredFile;

public interface MonitoredFileRepository extends JpaRepository<MonitoredFile, Long> {
}

