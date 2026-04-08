package com.example.POD.Controller.TeacherController;


import com.example.POD.Entity.ProblemStatement;
import com.example.POD.Repository.ProblemStatementRepo;
import com.example.POD.Repository.SaveCodeResponseRepo;
import com.example.POD.Repository.TestCaseRepo;
import com.example.POD.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor

@RequestMapping("/teacher")
public class AssignProblem {
    private final UserService userService;
    private final ProblemStatementRepo problemStatementRepo;
    private final TestCaseRepo testCaseRepo;
    private final SaveCodeResponseRepo saveCodeResponseRepo;


    @CacheEvict(value = {"allProblemStatementsCache", "assignedProblemsQuery"}, allEntries = true)
    @GetMapping("/assignProblem/{problemId}")
    public String assignProblem(@PathVariable Long problemId)
    {
        String mess=userService.assignProblemStatement(problemId);
        return mess;
    }

    // delete Problem statement by the teacher request......
    @CacheEvict(value = {"allProblemStatementsCache", "assignedProblemsQuery", "testCaseCache", "problemStudentsCache"}, allEntries = true)
    @DeleteMapping("/deleteProblem/{id}")
    public String deleteProblem(@PathVariable Long id)
    {
        testCaseRepo.deleteByProblemId(id);
        saveCodeResponseRepo.deleteByProblemId(id);
        problemStatementRepo.deleteById(id);
        return "Problem deleted successfully.....";
    }

  @GetMapping("/editProblem/{problemId}")
    public ProblemStatement editProblem(@PathVariable Long problemId)
  {
     ProblemStatement Problem= problemStatementRepo.findById(problemId).orElseThrow(() -> new RuntimeException("Problem not found with id: " + problemId));
     return Problem;
  }

  @CacheEvict(value = {"allProblemStatementsCache", "assignedProblemsQuery"}, allEntries = true)
  @PatchMapping("/updateProblem/{problemId}")
    public String updateProblem(@PathVariable Long problemId, @RequestBody ProblemStatement updatedData )
  {
      ProblemStatement existingProblem= editProblem(problemId);

      if(existingProblem==null)
      {
          return "Sorry problem not exist..";
      }
      if(updatedData.getTitle()!=null)
      {
          existingProblem.setTitle(updatedData.getTitle());
      }
      if(updatedData.getProblemStatement()!=null)
      {
          existingProblem.setProblemStatement(updatedData.getProblemStatement());
      }
      if (updatedData.getSemester() != 0)
      {
          existingProblem.setSemester(updatedData.getSemester());
      }

      problemStatementRepo.save(existingProblem);
      return ("Problem Updated Successfully!");

  }


}
