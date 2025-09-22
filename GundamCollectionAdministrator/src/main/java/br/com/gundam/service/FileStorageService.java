package br.com.gundam.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class FileStorageService {

    private final Path root;

    public FileStorageService(@Value("${app.storage.root}") String rootDir) throws IOException {
        this.root = Paths.get(rootDir).toAbsolutePath().normalize();
        Files.createDirectories(this.root);
    }

    public String save(MultipartFile file, String prefix) throws IOException {
        if (file == null || file.isEmpty()) return null;
        String original = StringUtils.cleanPath(file.getOriginalFilename());
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String filename = prefix + "_" + timestamp + "_" + original.replaceAll("\\s+", "_");
        Path target = this.root.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        return filename; // será servido por um handler estático
    }

    public Path resolve(String filename) {
        return this.root.resolve(filename);
    }
}
