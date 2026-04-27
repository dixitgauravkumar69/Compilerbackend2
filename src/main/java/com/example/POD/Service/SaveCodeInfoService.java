package com.example.POD.Service;

import com.example.POD.DTO.GettingCodeSimilarityResponse;
import com.example.POD.DTO.SimilarCodePercentageDTO;
import com.example.POD.Entity.CodeSaveEntity;
import com.example.POD.Entity.ProblemStatement;
import com.example.POD.Entity.StudentsCodeReport;
import com.example.POD.Entity.UserEntity;
import com.example.POD.Repository.CodeRepository;
import com.example.POD.Repository.ProblemStatementRepo;
import com.example.POD.Repository.SaveCodeResponseRepo;
import com.example.POD.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SaveCodeInfoService {

    private final SaveCodeResponseRepo repo;
    private final UserRepository userRepository;
    private final ProblemStatementRepo problemRepository;
    private final CodeRepository codeRepository;

    public String addStudentResponse(StudentsCodeReport studentsCodeReport, Long userId, Long problemId) {

        // 1. Check if the report already exists
        Optional<StudentsCodeReport> existingReport = repo.findByUserUseridAndProblemId(userId, problemId);

        if (existingReport.isPresent()) {
            //  Purani ID naye report object mein set kr li
            studentsCodeReport.setId(existingReport.get().getId());
            System.out.println("Updating existing report for User: " + userId);
        } else {
            System.out.println("Creating new report for User: " + userId);
        }

        // 2. Fetch actual entities
        UserEntity user = userRepository.findByuserid(userId);
        ProblemStatement problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new RuntimeException("Problem not found"));

        // 3. Set nested entities
        studentsCodeReport.setUser(user);
        studentsCodeReport.setProblem(problem);

        // 4. Save (If ID exists, it updates. If ID is null, it inserts.)
        repo.save(studentsCodeReport);


        int tmpM=studentsCodeReport.getMarks();// debugging line ..............



        return existingReport.isPresent() ? "Response updated successfully!" : "Information saved successfully!"+ tmpM;
    }


    public ResponseEntity<CodeSaveEntity> savingActualCode(Long userId, Long problemId, String code)
    {
        CodeSaveEntity codeData=new CodeSaveEntity();

        // 1. Via problem and userID i will found if already exist than get code and id will same not
        //will updated.......  Sirf problem ke base me nikalna hoga data
        CodeSaveEntity savedData=codeRepository.findByUserAndProblem(userId, problemId);

        List<CodeSaveEntity> anotherSolutions=codeRepository.findByProblemId(problemId);


        if(savedData!=null)
        {
            codeData.setId(savedData.getId());
        }

        UserEntity user=userRepository.findByuserid(userId);
        if(user!=null)
        {
            codeData.setUser(user);
        }

        ProblemStatement problemStatement =
                problemRepository.findById(problemId)
                        .orElseThrow(() -> new RuntimeException("Problem not found"));

        if(problemStatement!=null)
        {
            codeData.setProblem(problemStatement);
        }


        List<SimilarCodePercentageDTO>SimilarCodeInfo=new ArrayList<>();


        List<String>similarityCodeMessages=new ArrayList<>();

        //loop laga ke save krna hai similarity,,,,, List lauta dunga aise nhii
        for(CodeSaveEntity anotherSolution:anotherSolutions)
        {
            SimilarCodePercentageDTO similarCodeData=new SimilarCodePercentageDTO();


            Integer similarityOfCode=similarityOfCode(anotherSolution.getCode(),code);

            similarCodeData.setSimilarityPercentage(similarityOfCode);
            similarCodeData.setUserId(anotherSolution.getUser().getUserid());


            similarityCodeMessages.add("Similarity measure is : "+ similarityOfCode+ "with"+ anotherSolution.getUser().getUserid());

            System.out.println("Similarity measure is : "+ similarityOfCode+ "with"+ anotherSolution.getUser().getUserid());
            SimilarCodeInfo.add(similarCodeData);

        }

        //Print list of similarity percetage of user code to existing code
        for(SimilarCodePercentageDTO data : SimilarCodeInfo){
            System.out.println("UserId: " + data.getUserId() +
                    " Similarity: " + data.getSimilarityPercentage());

        }


        //Saving code of user
        codeData.setCode(code);
        codeData.setSimilarity(similarityCodeMessages);


       CodeSaveEntity saveCode= codeRepository.save(codeData);

        return ResponseEntity.ok(saveCode);

    }



    public Integer similarityOfCode(String code,String newCode)
    {
       //1. I will convert this string in normalize form
        String normalizedCode=normalizedCode(code);
        String normalizedNewCode=normalizedCode(newCode);



        // 2. Calculate token form of both existing and new code

         List<String> codeToken=tokenize(normalizedCode);
         List<String>newCodeToken=tokenize(normalizedNewCode);

        //3. Calculate LMS between both(Longest matching sequence)

          int lcsMatch=lcs(codeToken,newCodeToken);
        System.out.println("LCS MAtch:"+lcsMatch);

        int maxLength = Math.max(codeToken.size(), newCodeToken.size());
        System.out.println("Code MAx length"+maxLength);

        int similarityPercentage = (int) ((lcsMatch * 100.0) / maxLength);

         return similarityPercentage;
    }


    public String normalizedCode(String code)
    {
        return code
                .replaceAll("//.*", "")      // remove single line comments
                .replaceAll("\\s+", "")      // remove spaces, tabs, newlines
                .toLowerCase();  // remove case if applicable
    }


    public List<String> tokenize(String code) {
        return Arrays.asList(code.split("[^a-zA-Z0-9]+"));
    }


    public int lcs(List<String> a, List<String> b) {
        int[][] dp = new int[a.size()+1][b.size()+1];

        for (int i = 1; i <= a.size(); i++) {
            for (int j = 1; j <= b.size(); j++) {
                if (a.get(i-1).equals(b.get(j-1))) {
                    dp[i][j] = dp[i-1][j-1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1]);
                }
            }
        }
        return dp[a.size()][b.size()];
    }





    public List<GettingCodeSimilarityResponse>getSimilarityCodeWithUser(Long userId,Long problemId)
    {
       String studentName=userRepository.findUsernameByUserid(userId);

        List<GettingCodeSimilarityResponse>Response=new ArrayList<>();
        List<String>similarityCodeResponseList=new ArrayList<>();


        CodeSaveEntity similarityInfos=codeRepository.findByUserAndProblem(userId,problemId);

        for(String similarityInfo:similarityInfos.getSimilarity())
        {
            similarityCodeResponseList.add(similarityInfo);
        }

        //List a gyi jisme string hai similarity vali ab isse Similarity percentage and similar with ka calculation kr lunga
        for (String item : similarityCodeResponseList) {


            GettingCodeSimilarityResponse similarityResponse=new GettingCodeSimilarityResponse();

            // ":" ke baad wala part
            String valuePart = item.split(":")[1].trim();

            // "with" ke basis pe split
            String[] parts = valuePart.split("with");

            int percentage = Integer.parseInt(parts[0]);
            long similarId = Long.parseLong(parts[1]);

            System.out.println("Percentage: " + percentage);
            System.out.println("Similar ID: " + similarId);




                similarityResponse.setSimilarWith(similarId);
                similarityResponse.setSimialrityPercentage(percentage);
                similarityResponse.setStudentName(studentName);

                //Check student's similarity score
            //1. if score less than 70% --> good
            //2. if score less than 50% --> better
            //3. if score less than 30%--> best

          if(percentage<70)
          {
              similarityResponse.setRemark("Good");
          }
          else if(percentage<=50)
          {
              similarityResponse.setRemark("Better");
          }
         else if(percentage<=30)
          {
              similarityResponse.setRemark("Best");
          }
         else if((percentage>70) &&(percentage<85))
          {
              similarityResponse.setRemark("Partial match");
          }
         else
          {
              similarityResponse.setRemark("Fully matched");
          }


        Response.add(similarityResponse);
        }


        return Response;
    }
}