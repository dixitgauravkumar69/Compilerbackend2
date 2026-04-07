package com.example.POD.Service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import sendinblue.ApiClient;
import sendinblue.ApiException;
import sendinblue.Configuration;
import sendinblue.auth.ApiKeyAuth;
import sibApi.TransactionalEmailsApi;
import sibModel.SendSmtpEmail;
import sibModel.SendSmtpEmailSender;
import sibModel.SendSmtpEmailTo;

import java.util.Collections;
import java.util.Random;

@Service
public class EmailService {

    @Value("${brevo.api.key}")
    private String apiKeyString;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    @Async
    public void sendWelcomeEmail(String toEmail, String username, String OTP) {
        try {
            ApiClient defaultClient = Configuration.getDefaultApiClient();
            ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
            apiKey.setApiKey(apiKeyString);

            TransactionalEmailsApi apiInstance = new TransactionalEmailsApi();

            String htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
            </head>
            <body style="margin: 0; padding: 0; background-color: #f4f7fa; font-family: 'Segoe UI', Helvetica, Arial, sans-serif;">
                <table align="center" width="100%%" style="max-width: 600px; margin: 20px auto; background-color: #ffffff; border-radius: 12px; overflow: hidden; border: 1px solid #e1e8f0;">
                    
                    <!-- Header -->
                    <tr>
                        <td style="background-color: #2563eb; padding: 40px 20px; text-align: center;">
                            <h1 style="color: #ffffff; margin: 0;">POD Panel</h1>
                            <p style="color: #bfdbfe;">Elevating Coding Standards</p>
                        </td>
                    </tr>

                    <!-- Body -->
                    <tr>
                        <td style="padding: 40px 30px;">
                            <h2 style="color: #1e293b;">Hello %s 👋</h2>

                            <p style="color: #475569; font-size: 16px;">
                                Welcome to <strong>POD Panel</strong> 🎉<br><br>
                                Use the OTP below to verify your email:
                            </p>

                            
                            <div style="text-align: center; margin: 30px 0;">
                                <div style="display: inline-block; padding: 15px 30px; font-size: 28px; font-weight: bold; letter-spacing: 5px; background-color: #f1f5f9; border: 2px dashed #2563eb; border-radius: 10px; color: #1e293b;">
                                    %s
                                </div>
                            </div>

                            <!-- Expiry -->
                            <p style="color: #ef4444; font-size: 14px; text-align: center;">
                                ⏳ This OTP is valid for 5 minutes only
                            </p>

                            <p style="color: #475569; font-size: 15px;">
                                Do not share this OTP with anyone.
                            </p>
                        </td>
                    </tr>

                    <!-- Footer -->
                    <tr>
                        <td style="padding: 20px; background-color: #f1f5f9; text-align: center;">
                            <p style="color: #64748b; font-size: 14px;">
                                &copy; 2026 POD Panel Team
                            </p>
                        </td>
                    </tr>

                </table>
            </body>
            </html>
            """.formatted(username, OTP);

            SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();
            sendSmtpEmail.setSubject("Your OTP Code 🔐");
            sendSmtpEmail.setHtmlContent(htmlContent);

            sendSmtpEmail.setSender(new SendSmtpEmailSender()
                    .email(senderEmail)
                    .name("DEV CAMPUS Team"));

            sendSmtpEmail.setTo(Collections.singletonList(
                    new SendSmtpEmailTo().email(toEmail)
            ));

            apiInstance.sendTransacEmail(sendSmtpEmail);

            System.out.println("OTP Email sent successfully to " + username);

        } catch (ApiException e) {
            System.err.println("Brevo API Error Code: " + e.getCode());
            System.err.println("Response: " + e.getResponseBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Async
    public void sendStudentPerformanceReport(String toEmail, String username, Long problemId, int marks, String takenTime) {
        try {
            ApiClient defaultClient = Configuration.getDefaultApiClient();
            ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
            apiKey.setApiKey(apiKeyString);

            TransactionalEmailsApi apiInstance = new TransactionalEmailsApi();

            String statusColor = (marks >= 80) ? "#10b981" : "#f59e0b";

            // Performance mail template
            String htmlContent = """
                <div style="font-family: 'Segoe UI', sans-serif; max-width: 550px; margin: auto; border: 1px solid #eee; border-radius: 15px; overflow: hidden;">
                    <div style="background: #0f172a; color: white; padding: 30px; text-align: center;">
                        <h2 style="margin: 0;">Submission Received!</h2>
                        <p style="opacity: 0.7;">Keep pushing your limits.</p>
                    </div>
                    <div style="padding: 30px; background: #ffffff;">
                        <p>Hello <b>%s</b>,</p>
                        <p>YOUR CODE SUBMITTED SUCCESSFULLY AND DETAILS ARE FOLLOWING:</p>
                        <div style="margin: 25px 0; padding: 20px; background: #f8fafc; border-radius: 10px; border: 1px solid #e2e8f0;">
                            <table width="100%%">
                                <tr><td style="color: #64748b;">Problem ID:</td><td style="text-align: right; font-weight: bold;">#%d</td></tr>
                                <tr><td style="color: #64748b;">Score:</td><td style="text-align: right; font-weight: bold; color: %s;">%d / 100</td></tr>
                                <tr><td style="color: #64748b;">Time Taken:</td><td style="text-align: right; font-weight: bold;">%s</td></tr>
                            </table>
                        </div>
                        <p style="font-size: 14px; color: #475569;">You can check your performance in performance section of website..</p>
                    </div>
                    <div style="text-align: center; padding: 20px; background: #f1f5f9; font-size: 12px; color: #94a3b8;">
                        Build with ❤️ by DEV CAMPUS Team
                    </div>
                </div>
                """.formatted(username, problemId, statusColor, marks, takenTime);

            SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();
            sendSmtpEmail.setSubject("Coding Report: Problem #" + problemId + " Submission ✅");
            sendSmtpEmail.setHtmlContent(htmlContent);

            sendSmtpEmail.setSender(new SendSmtpEmailSender().email(senderEmail).name("POD Performance Bot"));
            sendSmtpEmail.setTo(Collections.singletonList(new SendSmtpEmailTo().email(toEmail)));

            apiInstance.sendTransacEmail(sendSmtpEmail);
            System.out.println("Performance Report sent successfully!");

        } catch (Exception e) {
            System.err.println("Performance Mail Error: " + e.getMessage());
        }
    }





    public Integer generateOTP() {
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000);
        return otp;
    }
}