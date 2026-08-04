package com.example.diplomneto.controller;

import com.example.diplomneto.dto.FileDto;
import com.example.diplomneto.model.User;
import com.example.diplomneto.service.FileService;
import com.example.diplomneto.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class FileController {

    private final FileService fileService;
    private final UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostMapping("/file")
    public ResponseEntity<Void> uploadFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String filename,
            @RequestParam("file") MultipartFile file) throws IOException {
        User user = userService.getUserByToken(authToken);
        fileService.uploadFile(file, filename, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/file")
    public ResponseEntity<Void> deleteFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String filename) throws IOException {
        User user = userService.getUserByToken(authToken);
        fileService.deleteFile(filename, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/file")
    public ResponseEntity<byte[]> downloadFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String filename) throws IOException {
        User user = userService.getUserByToken(authToken);
        byte[] data = fileService.downloadFile(filename, user);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", filename);
        return ResponseEntity.ok().headers(headers).body(data);
    }

    @PutMapping("/file")
    public ResponseEntity<Void> renameFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String filename,
            @RequestBody Map<String, String> body) {
        User user = userService.getUserByToken(authToken);
        String newName = body.get("name");
        fileService.renameFile(filename, newName, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<FileDto>> listFiles(
            @RequestHeader("auth-token") String authToken,
            @RequestParam(value = "limit", defaultValue = "3") int limit) {
        User user = userService.getUserByToken(authToken);
        List<FileDto> files = fileService.listFiles(user, limit);
        return ResponseEntity.ok(files);
    }
}
