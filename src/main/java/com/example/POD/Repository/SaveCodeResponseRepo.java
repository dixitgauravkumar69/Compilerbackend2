package com.example.POD.Repository;

import com.example.POD.Entity.StudentsCodeReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaveCodeResponseRepo extends JpaRepository<StudentsCodeReport,Long> {
}
