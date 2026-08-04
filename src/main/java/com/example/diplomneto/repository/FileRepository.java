package com.example.diplomneto.repository;

import com.example.diplomneto.model.CloudFile;
import com.example.diplomneto.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<CloudFile, Long> {
    List<CloudFile> findByUser(User user, Pageable pageable);

    Optional<CloudFile> findByFilenameAndUser(String filename, User user);
}