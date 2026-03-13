package com.example.POD.Repository;

import com.example.POD.DTO.Jobdescription;
import com.example.POD.DTO.ShowJobsStudent;
import com.example.POD.Entity.CampusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CampusRepository extends JpaRepository<CampusEntity,Long> {
    List<CampusEntity> findBySemester(Integer Semester);

    @Query("""
SELECT new com.example.POD.DTO.Jobdescription(
c.id,
c.company,
c.title,
c.jobType,
c.industry,
c.location,
c.semester,
c.salaryPackage,
c.eligibility,
c.bond,
c.skillsRequired,
c.jobDescription,
c.selectionProcess,
c.registrationLastDate,
c.attachment
)
FROM CampusEntity c
WHERE c.id = :id
""")
    Jobdescription getJobDescription(Long id);
}
