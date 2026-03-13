package com.example.POD.Service;

import com.example.POD.DTO.Languages;
import com.example.POD.Entity.TestCaseEntity;
import com.example.POD.Repository.TestCaseRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CodeService {

    private final TestCaseRepo testCaseRepository;

    public String runWithTestCases(String code, Languages language, Long problemId) throws Exception {

        // 1. Setup paths and clean code
        String workingDir = System.getProperty("user.dir");
        String fileName = language.getFileName();
        File codeFile = getFile(code, workingDir, fileName);

        try {
            // 2. Compilation Phase
            if (language.getCompileCommand() != null) {
                ProcessBuilder cb;
                if (language.name().equalsIgnoreCase("CPP")) {
                    // C++: g++ Main.cpp -o program
                    cb = new ProcessBuilder("g++", fileName, "-o", "program");
                } else {
                    // Java: javac Main.java
                    cb = new ProcessBuilder(language.getCompileCommand(), fileName);
                }

                cb.directory(new File(workingDir));
                Process compile = cb.start();
                String compileError = readStream(compile.getErrorStream());
                compile.waitFor();

                if (!compileError.isEmpty()) {
                    return "Compilation Error:\n" + compileError;
                }

                // CRITICAL FOR DOCKER/LINUX: Permission dena zaruri hai executable ko
                if (language.name().equalsIgnoreCase("CPP")) {
                    new ProcessBuilder("chmod", "+x", "program")
                            .directory(new File(workingDir))
                            .start()
                            .waitFor();
                }
            }

            // 3. Fetch Testcases
            List<TestCaseEntity> testCases = testCaseRepository.getTestCasesByProblemId(problemId);
            if (testCases.isEmpty()) {
                return "No testcases found for this problem.";
            }

            StringBuilder result = new StringBuilder();
            int passCount = 0;

            // 4. Execution Phase
            for (int i = 0; i < testCases.size(); i++) {
                TestCaseEntity tc = testCases.get(i);
                ProcessBuilder rb;

                if (language.name().equalsIgnoreCase("PYTHON")) {
                    rb = new ProcessBuilder("python3", fileName);
                } else if (language.name().equalsIgnoreCase("CPP")) {
                    rb = new ProcessBuilder("./program");
                } else if (language.name().equalsIgnoreCase("JAVA")) {
                    String className = fileName.replace(".java", "");
                    rb = new ProcessBuilder("java", className);
                } else {
                    rb = new ProcessBuilder(language.getRunCommand());
                }

                rb.directory(new File(workingDir));
                Process run = rb.start();

                // Standard Input handling
                try (BufferedWriter inputWriter = new BufferedWriter(
                        new OutputStreamWriter(run.getOutputStream(), StandardCharsets.UTF_8))) {
                    inputWriter.write(tc.getInputData());
                    inputWriter.newLine();
                    inputWriter.flush();
                }

                // Timeout Protection (5 seconds)
                boolean finished = run.waitFor(5, TimeUnit.SECONDS);
                if (!finished) {
                    run.destroyForcibly();
                    result.append("Test Case ").append(i + 1).append(": Time Limit Exceeded\n");
                    continue;
                }

                String actualOutput = readStream(run.getInputStream()).trim();
                String runtimeError = readStream(run.getErrorStream()).trim();

                if (!runtimeError.isEmpty()) {
                    result.append("Test Case ").append(i + 1).append(": Runtime Error (").append(runtimeError).append(")\n");
                    continue;
                }

                // Comparison Logic (Normalize spaces)
                String expected = tc.getExpectedOutput().trim().replaceAll("\\s+", " ");
                String actual = actualOutput.replaceAll("\\s+", " ");

                if (expected.equals(actual)) {
                    passCount++;
                    result.append("Test Case ").append(i + 1).append(": Passed\n");
                } else {
                    result.append("Test Case ").append(i + 1)
                            .append(": Failed (Expected: [").append(expected)
                            .append("], Got: [").append(actual).append("])\n");
                }
            }

            result.append("\nFinal Score: ").append(passCount).append("/").append(testCases.size());
            return result.toString();

        } finally {
            // 5. Always Cleanup
            if (codeFile.exists()) codeFile.delete();
            new File(workingDir, "program").delete();
            if (language.name().equalsIgnoreCase("JAVA")) {
                new File(workingDir, fileName.replace(".java", ".class")).delete();
            }
        }
    }

    /**
     * Extracts code to a file and cleans non-breaking spaces
     */
    private File getFile(String code, String workingDir, String fileName) throws IOException {
        File file = new File(workingDir, fileName);
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            // invisible spaces (\u00a0) replace logic
            String cleanedCode = code.replace('\u00a0', ' ').replace('\u00A0', ' ');
            writer.write(cleanedCode);
        }
        return file;
    }

    /**
     * Helper to read process output
     */
    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }
}