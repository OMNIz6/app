package com.app.util.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.util.app.model.Student;

public interface StudentRepo extends JpaRepository<Student,Long>{
    
}
