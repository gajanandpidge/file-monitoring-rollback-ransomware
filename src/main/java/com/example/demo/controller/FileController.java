package com.example.demo.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.MonitoredFile;
import com.example.demo.service.FileMonitorService;

@Controller
public class FileController {

    private final FileMonitorService service;

    public FileController(FileMonitorService service) {
        this.service = service;
    }

   
    @GetMapping("/")
    public String index(Model model) {
        List<MonitoredFile> files = service.getAllFiles();
        model.addAttribute("files", files);
        return "index";
    }

   
    @PostMapping("/files/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        service.saveFile(file);
        return "redirect:/";
    }

   
    @GetMapping("/files/encrypt/{id}")
    public String encryptFile(@PathVariable Long id) throws IOException {
        service.encryptFile(id);
        return "redirect:/";
    }

   
    @GetMapping("/files/rollback/{id}")
    public String rollbackFile(@PathVariable Long id) throws IOException {
        service.rollbackFile(id);
        return "redirect:/";
    }
}
