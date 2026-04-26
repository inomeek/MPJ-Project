package ino.placement.service;

import ino.placement.entity.Student;
import ino.placement.repository.StudentRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final StudentRepository repo;

    public AuthService(StudentRepository repo) {
        this.repo = repo;
    }

    public Student login(String email, String password) {

        Student student = repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!student.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        return student;
    }
}