package com.example.POD.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

@Service
public class EmailService {

    @Value("${brevo.api.key}")
    private String apiKeyString;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    // ===================== SEND OTP EMAIL =====================
    @Async
    public void sendWelcomeEmail(String toEmail, String username, String OTP) {
        try {

            String htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
            </head>
            <body style="font-family: Arial, sans-serif; background:#f4f7fa; padding:20px;">
                <div style="max-width:600px; margin:auto; background:#ffffff; padding:20px; border-radius:10px;">
                    <h2>Hello %s 👋</h2>
                    <p>Welcome to <b>POD Panel</b></p>
                    <p>Your OTP is:</p>

                    <div style="text-align:center; margin:20px;">
                        <h1 style="letter-spacing:5px;">%s</h1>
                    </div>

                    <p style="color:red;">⏳ Valid for 5 minutes</p>
                    <p>Do not share this OTP.</p>

                    <hr>
                    <p style="font-size:12px;">© 2026 POD Panel</p>
                </div>
            </body>
            </html>
            """.formatted(username, OTP);

            sendEmail(toEmail, "Your OTP Code 🔐", htmlContent);

            System.out.println("✅ OTP Email sent successfully to " + toEmail);

        } catch (Exception e) {
            System.out.println("❌ Email error: " + e.getMessage());
        }
    }

    // ===================== PERFORMANCE REPORT =====================
    @Async
    public void sendStudentPerformanceReport(String toEmail, String username, Long problemId, int marks, String takenTime) {
        try {

            String statusColor = (marks >= 80) ? "#10b981" : "#f59e0b";

            String htmlContent = """
            <div style="font-family: Arial; max-width:550px; margin:auto; border-radius:10px;">
                <div style="background:#0f172a; color:white; padding:20px; text-align:center;">
                    <h2>Submission Received!</h2>
                </div>

                <div style="padding:20px;">
                    <p>Hello <b>%s</b>,</p>
                    <p>Your code submitted successfully.</p>

                    <table style="width:100%%; margin-top:20px;">
                        <tr>
                            <td>Problem ID:</td>
                            <td style="text-align:right;"><b>#%d</b></td>
                        </tr>
                        <tr>
                            <td>Score:</td>
                            <td style="text-align:right; color:%s;"><b>%d</b></td>
                        </tr>
                        <tr>
                            <td>Time:</td>
                            <td style="text-align:right;"><b>%s</b></td>
                        </tr>
                    </table>
                </div>

                <div style="text-align:center; padding:10px; font-size:12px;">
                    DEV CAMPUS Team
                </div>
            </div>
            """.formatted(username, problemId, statusColor, marks, takenTime);

            sendEmail(toEmail, "Coding Report #" + problemId + " ✅", htmlContent);

            System.out.println("✅ Performance Report sent successfully");

        } catch (Exception e) {
            System.out.println("❌ Performance mail error: " + e.getMessage());
        }
    }

    // ===================== COMMON EMAIL METHOD =====================
    private void sendEmail(String toEmail, String subject, String htmlContent) throws Exception {

        URL url = new URL("https://api.brevo.com/v3/smtp/email");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("accept", "application/json");
        conn.setRequestProperty("api-key", apiKeyString);
        conn.setRequestProperty("content-type", "application/json");
        conn.setDoOutput(true);

        String safeHtml = htmlContent.replace("\"", "\\\"").replace("\n", "");

        String jsonInput = "{"
                + "\"sender\":{\"email\":\"" + senderEmail + "\",\"name\":\"DEV CAMPUS Team\"},"
                + "\"to\":[{\"email\":\"" + toEmail + "\"}],"
                + "\"subject\":\"" + subject + "\","
                + "\"htmlContent\":\"" + safeHtml + "\""
                + "}";

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonInput.getBytes("utf-8"));
        }

        int responseCode = conn.getResponseCode();

        if (responseCode != 201) {
            throw new RuntimeException("Failed with HTTP code: " + responseCode);
        }
    }

    // ===================== OTP GENERATOR =====================
    public Integer generateOTP() {
        Random random = new Random();
        return 1000 + random.nextInt(9000);
    }
}