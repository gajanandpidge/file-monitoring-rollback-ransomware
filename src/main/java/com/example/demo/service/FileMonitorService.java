package com.example.demo.service;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.MonitoredFile;
import com.example.demo.repository.MonitoredFileRepository;

@Service
public class FileMonitorService {

    @Value("${file.monitor.directory}")
    private String monitorDir;

    @Value("${file.backup.directory}")
    private String backupDir;

    private final MonitoredFileRepository repo;

    public FileMonitorService(MonitoredFileRepository repo) {
        this.repo = repo;
    }

    // Upload file and create a backup
    public MonitoredFile saveFile(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(monitorDir, file.getOriginalFilename());
        Files.copy(file.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);

        // Backup copy
        Path backupPath = Paths.get(backupDir, file.getOriginalFilename());
        Files.copy(file.getInputStream(), backupPath, StandardCopyOption.REPLACE_EXISTING);

        MonitoredFile monitoredFile = new MonitoredFile(
                file.getOriginalFilename(),
                uploadPath.toString(),
                backupPath.toString(),
                LocalDateTime.now()
        );
        return repo.save(monitoredFile);
    }

    public List<MonitoredFile> getAllFiles() {
        return repo.findAll();
    }

    // Simulate ransomware attack (encrypt file)
    public String encryptFile(Long id) throws IOException {
        MonitoredFile mf = repo.findById(id).orElseThrow();
        Path path = Paths.get(mf.getFilePath());
        Files.write(path, "ENCRYPTED FILE CONTENT".getBytes());
        mf.setEncrypted(true);
        repo.save(mf);
        return "File " + mf.getFileName() + " encrypted successfully!";
    }

    
    // Rollback to original backup
    public String rollbackFile(Long id) throws IOException {
        MonitoredFile mf = repo.findById(id).orElseThrow();
        Path original = Paths.get(mf.getFilePath());
        Path backup = Paths.get(mf.getBackupPath());
        Files.copy(backup, original, StandardCopyOption.REPLACE_EXISTING);
        mf.setEncrypted(false);
        repo.save(mf);
        return "File " + mf.getFileName() + " restored successfully!";
    }
}
