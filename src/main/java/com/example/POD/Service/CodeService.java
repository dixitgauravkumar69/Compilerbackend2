package com.example.POD.Service;

import com.example.POD.DTO.Languages;
import com.example.POD.DTO.ResponseCodeExecution;
import com.example.POD.Entity.TestCaseEntity;
import com.example.POD.Repository.TestCaseRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CodeService {

    private final TestCaseRepo testCaseRepository;

    public ResponseCodeExecution runWithTestCases(String code, Languages language, Long problemId) throws Exception {
        ResponseCodeExecution response = new ResponseCodeExecution();

        // 1. Har request ke liye alag folder ban rh  hai (/tmp/uuid)
        String uniqueId = UUID.randomUUID().toString();
        Path requestDirPath = Paths.get("/tmp", uniqueId);
        Files.createDirectories(requestDirPath);
        String workingDir = requestDirPath.toString();

        // 2. Java ke liye file ka naam hamesha 'Main.java' hi hoga jo ki save hoga
        String fileName = language.name().equalsIgnoreCase("JAVA") ? "Main.java" : language.getFileName();
        String executableName = "program"; // C++ ke liye simple name

        File codeFile = getFile(code, workingDir, fileName);
        List<String> resultList = new ArrayList<>();
        int passCount = 0;

        try {
            // --- Compilation Phase ---
            if (language.getCompileCommand() != null) {
                ProcessBuilder cb;
                if (language.name().equalsIgnoreCase("CPP")) {
                    cb = new ProcessBuilder("g++", fileName, "-o", executableName);
                } else if (language.name().equalsIgnoreCase("JAVA")) {
                    cb = new ProcessBuilder("javac", fileName);
                } else {
                    cb = new ProcessBuilder(language.getCompileCommand(), fileName);
                }

                cb.directory(new File(workingDir));
                Process compile = cb.start();
                String compileError = readStream(compile.getErrorStream());
                compile.waitFor();

                if (!compileError.isEmpty()) {
                    resultList.add("Compilation Error:\n" + compileError);
                    response.setTestCases(resultList);
                    response.setMarks(0);
                    return response;
                }

                if (language.name().equalsIgnoreCase("CPP")) {
                    new ProcessBuilder("chmod", "+x", workingDir + "/" + executableName).start().waitFor();
                }
            }

            // --- Fetch Testcases ---
            List<TestCaseEntity> testCases = testCaseRepository.getTestCasesByProblemId(problemId);
            if (testCases.isEmpty()) {
                resultList.add("No testcases found");
                response.setTestCases(resultList);
                return response;
            }

            // --- Execution Phase ---
            for (int i = 0; i < testCases.size(); i++) {
                TestCaseEntity tc = testCases.get(i);
                ProcessBuilder rb;

                if (language.name().equalsIgnoreCase("PYTHON")) {
                    rb = new ProcessBuilder("python3", fileName);
                } else if (language.name().equalsIgnoreCase("CPP")) {
                    rb = new ProcessBuilder("./" + executableName);
                } else if (language.name().equalsIgnoreCase("JAVA")) {
                    // Yahan fix: Hamesha 'Main' class run hogi
                    rb = new ProcessBuilder("java", "Main");
                } else {
                    rb = new ProcessBuilder("./" + executableName);
                }

                rb.directory(new File(workingDir));
                Process run = rb.start();

                // Input bhejiyo
                try (BufferedWriter inputWriter = new BufferedWriter(
                        new OutputStreamWriter(run.getOutputStream(), StandardCharsets.UTF_8))) {
                    inputWriter.write(tc.getInputData());
                    inputWriter.flush();
                }

                boolean finished = run.waitFor(5, TimeUnit.SECONDS);
                if (!finished) {
                    run.destroyForcibly();
                    resultList.add("Test Case " + (i + 1) + ": TLE");
                    continue;
                }

                String actualOutput = readStream(run.getInputStream()).trim();
                String runtimeError = readStream(run.getErrorStream()).trim();

                if (!runtimeError.isEmpty()) {
                    resultList.add("Test Case " + (i + 1) + ": Runtime Error (" + runtimeError + ")");
                    continue;
                }

                String expected = tc.getExpectedOutput().trim().replaceAll("\\s+", " ");
                String actual = actualOutput.replaceAll("\\s+", " ");

                if (expected.equals(actual)) {
                    passCount++;
                    resultList.add("Test Case " + (i + 1) + ": Passed");
                } else {
                    resultList.add("Test Case " + (i + 1) + ": Failed");
                }
            }

            response.setTestCases(resultList);
            response.setMarks(passCount);
            return response;

        } finally {
            // Cleanup: Poora request folder delete kar dein
            try {
                Files.walk(requestDirPath)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            } catch (Exception ignored) {}
        }
    }

    private File getFile(String code, String workingDir, String fileName) throws IOException {
        File file = new File(workingDir, fileName);
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            String cleanedCode = code.replace('\u00a0', ' ').replace('\u00A0', ' ');
            writer.write(cleanedCode);
        }
        return file;
    }

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