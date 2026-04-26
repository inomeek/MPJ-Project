package ino.placement.service;

import ino.placement.entity.Student;
import ino.placement.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository repo;

    public StudentService(StudentRepository repo) {
        this.repo = repo;
    }

    // Finds a student by ID - used by the Controller to verify existence
    public Optional<Student> getById(Long id) {
        return repo.findById(id);
    }

    // This handles both POST (new) and PUT (update) in JPA
    public Student save(Student s) {
        return repo.save(s);
    }

    public List<Student> getAll() {
        return repo.findAll();
    }

    public boolean existsById(Long id) {
        return repo.existsById(id);
    }

    // Adding this makes the controller logic cleaner
    public Student updateStudent(Student existingStudent, Student updatedData) {
        existingStudent.setFullName(updatedData.getFullName());
        existingStudent.setDepartment(updatedData.getDepartment());
        existingStudent.setBatch(updatedData.getBatch());
        existingStudent.setCgpa(updatedData.getCgpa());
        // Do NOT update ID or Email if you want to keep them constant
        return repo.save(existingStudent);
    }
}