package ru.hogwarts.school.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.AvatarRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class AvatarService {
    private final AvatarRepository avatarRepository;
    private final StudentService studentService;

    @Value("${avatars.directory.path}")
    private String avatarsDir;

    public AvatarService (AvatarRepository avatarRepository, StudentService studentService) {
        this.avatarRepository = avatarRepository;
        this.studentService = studentService;
    }

    @Transactional
    public void uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        Student student = studentService.getStudentById(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Студент не найден");
        }

        Path dirPath = Paths.get(avatarsDir);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = "avatar_" + studentId + extension;
        Path filePath = Paths.get(avatarsDir, fileName);
        Files.write(filePath, file.getBytes());

        Avatar avatar = avatarRepository.findByStudentId(studentId).orElse(new Avatar());
        avatar.setFilePath(filePath.toString());
        avatar.setMediaType(file.getContentType());
        avatar.setFileSize(file.getSize());
        avatar.setData(file.getBytes());
        avatar.setStudent(student);

        avatarRepository.save(avatar);
        student.setAvatar(avatar);
    }

    @Transactional(readOnly = true)
    public Avatar getAvatarFromDb(Long studentId) {
        return avatarRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Аватар не найден"));
    }

    public Path getAvatarFromDisk(Long studentId) {
        Avatar avatar = getAvatarFromDb(studentId);
        return Paths.get(avatar.getFilePath());
    }

    @Transactional
    public void deleteAvatar(Long studentId) {
        Avatar avatar = avatarRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Аватар не найден"));

        try {
            // Удаление файла с диска
            Files.deleteIfExists(Paths.get(avatar.getFilePath()));
            // Удаление из БД
            avatarRepository.delete(avatar);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при удалении аватара", e);
        }
    }
}
