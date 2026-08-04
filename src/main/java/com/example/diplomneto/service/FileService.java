package com.example.diplomneto.service;

import com.example.diplomneto.dto.FileDto;
import com.example.diplomneto.model.CloudFile;
import com.example.diplomneto.model.User;
import com.example.diplomneto.repository.FileRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class FileService {

    private final FileRepository fileRepository;
    private final Path storagePath;

    public FileService(FileRepository fileRepository,
                       @Value("${app.storage.path}") String storagePathStr) {
        this.fileRepository = fileRepository;
        this.storagePath = Paths.get(storagePathStr).toAbsolutePath().normalize();
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(storagePath);
        } catch (IOException e) {
            throw new RuntimeException("Could not create storage directory", e);
        }
    }

    public void uploadFile(MultipartFile file, String filename, User user) throws IOException {
        String uniqueFilename = UUID.randomUUID() + "_" + filename;
        Path targetPath = storagePath.resolve(uniqueFilename);
        file.transferTo(targetPath.toFile());

        CloudFile cloudFile = CloudFile.builder()
                .filename(filename)
                .size(file.getSize())
                .user(user)
                .storagePath(uniqueFilename)
                .build();
        fileRepository.save(cloudFile);
    }

    public List<FileDto> listFiles(User user, int limit) {
        return fileRepository.findByUser(user, PageRequest.of(0, limit))
                .stream()
                .map(f -> new FileDto(f.getFilename(), f.getSize()))
                .collect(Collectors.toList());
    }

    public byte[] downloadFile(String filename, User user) throws IOException {
        CloudFile cloudFile = fileRepository.findByFilenameAndUser(filename, user)
                .orElseThrow(() -> new RuntimeException("File not found"));
        Path filePath = storagePath.resolve(cloudFile.getStoragePath());
        return Files.readAllBytes(filePath);
    }

    public void deleteFile(String filename, User user) throws IOException {
        CloudFile cloudFile = fileRepository.findByFilenameAndUser(filename, user)
                .orElseThrow(() -> new RuntimeException("File not found"));
        Path filePath = storagePath.resolve(cloudFile.getStoragePath());
        Files.deleteIfExists(filePath);
        fileRepository.delete(cloudFile);
    }

    public void renameFile(String oldFilename, String newFilename, User user) {
        CloudFile cloudFile = fileRepository.findByFilenameAndUser(oldFilename, user)
                .orElseThrow(() -> new RuntimeException("File not found"));
        cloudFile.setFilename(newFilename);
        fileRepository.save(cloudFile);
    }
}