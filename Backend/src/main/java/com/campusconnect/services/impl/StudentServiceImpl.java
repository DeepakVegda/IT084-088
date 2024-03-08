package com.campusconnect.services.impl;

import com.campusconnect.dto.StudentDto;
import com.campusconnect.entities.Student;
import com.campusconnect.repositories.StudentRepo;
import com.campusconnect.services.StudentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    @Qualifier("modelMapper")
    private ModelMapper model;
    @Override
    public StudentDto createStudent(StudentDto studentdto) {
        Student student = this.model.map(studentdto,Student.class);
        Student student1 = this.studentRepo.save(student);
        return this.model.map(student1,StudentDto.class);
    }

    @Override
    public StudentDto updateStudent(StudentDto studentdto,Long studentId) {
        Student student = this.studentRepo.findById(studentId).orElseThrow();
        student.setStudentSem(student.getStudentSem());
        student.setStudentUsername(studentdto.getStudentUsername());
        student.setStudentEmail(studentdto.getStudentEmail());
        student.setStudentPassword(studentdto.getStudentPassword());
        Student updateStudent = this.studentRepo.save(student);
        return this.model.map(updateStudent,StudentDto.class);
    }

    @Override
    public void deleteStudent(Long studentId) {
         Student student = this.studentRepo.findById(studentId).orElseThrow();
         this.studentRepo.delete(student);
    }

    @Override
    public StudentDto getStudentById(Long studentId) {
        Student student = this.studentRepo.findById(studentId).orElseThrow();
        return this.model.map(student,StudentDto.class);
    }
}
