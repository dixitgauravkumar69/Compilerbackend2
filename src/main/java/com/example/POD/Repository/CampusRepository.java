package com.example.POD.Repository;

import com.example.POD.DTO.Jobdescription;

import com.example.POD.Entity.CampusEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CampusRepository extends JpaRepository<CampusEntity,Long> {
    @Cacheable(value = "jobsCache", key = "#Semester + '-' + #branch")
    List<CampusEntity> findBySemesterAndEligibleBranch(Integer Semester,String branch);

    @Query("""
SELECT new com.example.POD.DTO.Jobdescription(
c.id,
c.company,
c.title,
c.jobType,
c.location,
c.semester,
c.salaryPackage,
c.eligibleBranch,
c.cgpa,
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
    @Cacheable(value = "jobDescriptionCache", key = "#id")
    Jobdescription getJobDescription(Long id);


    @Override
    @Cacheable(value = "allCompaniesCache")
    List<CampusEntity> findAll();

}
