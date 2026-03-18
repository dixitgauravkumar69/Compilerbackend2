package com.example.POD.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Async // Isse Signup slow nahi hoga, mail background mein jayega
    public void sendWelcomeEmail(String toEmail, String username) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Welcome to POD Panel! 🚀");

            // HTML Template (Aap isse alag file mein bhi rakh sakte hain)
            String htmlContent = """
    <!DOCTYPE html>
    <html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body style="margin: 0; padding: 0; background-color: #f4f7fa; font-family: 'Segoe UI', Helvetica, Arial, sans-serif;">
        <table align="center" border="0" cellpadding="0" cellspacing="0" width="100%%" style="max-width: 600px; margin: 20px auto; background-color: #ffffff; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 10px rgba(0,0,0,0.05); border: 1px solid #e1e8f0;">
            <tr>
                <td style="background-color: #2563eb; padding: 40px 20px; text-align: center;">
                    <h1 style="color: #ffffff; margin: 0; font-size: 28px; letter-spacing: 1px;">POD Panel</h1>
                    <p style="color: #bfdbfe; margin-top: 10px; font-size: 16px;">Elevating Coding Standards</p>
                </td>
            </tr>

            <tr>
                <td style="padding: 40px 30px;">
                    <h2 style="color: #1e293b; margin: 0 0 20px 0; font-size: 22px;">Welcome to the Community, %s! 🚀</h2>
                    <p style="color: #475569; font-size: 16px; line-height: 1.6; margin-bottom: 25px;">
                        Hume khushi hai ki aapne <strong>POD Panel</strong> join kiya. Hamara platform aapko coding challenges manage karne aur students ki real-time progress track karne mein help karta hai.
                    </p>
                    
                    <div style="background-color: #f8fafc; border-left: 4px solid #2563eb; padding: 15px; margin-bottom: 25px;">
                        <p style="color: #1e293b; margin: 0; font-size: 15px;"><strong>Quick Tip:</strong> Aap naye problem statements "Add Statement" tab se create kar sakte hain.</p>
                    </div>

                    <table border="0" cellpadding="0" cellspacing="0" width="100%%">
                        <tr>
                            <td align="center">
                                <a href="https://compiler-testcase.vercel.app/login" 
                                   style="display: inline-block; background-color: #10b981; color: #ffffff; padding: 14px 30px; text-decoration: none; border-radius: 8px; font-weight: 600; font-size: 16px; box-shadow: 0 4px 6px rgba(16, 185, 129, 0.2);">
                                   Get Started Now
                                </a>
                            </td>
                        </tr>
                    </table>

                    <p style="color: #475569; font-size: 16px; line-height: 1.6; margin-top: 30px;">
                        Agar aapko koi help chahiye, toh bas is mail ka reply karein. Hum aapki madad ke liye hamesha taiyar hain!
                    </p>
                </td>
            </tr>

            <tr>
                <td style="padding: 30px; background-color: #f1f5f9; text-align: center; border-top: 1px solid #e2e8f0;">
                    <p style="color: #64748b; font-size: 14px; margin: 0;">&copy; 2026 POD Panel Team. All rights reserved.</p>
                    <div style="margin-top: 15px;">
                        <a href="#" style="color: #2563eb; text-decoration: none; font-size: 13px; margin: 0 10px;">Privacy Policy</a>
                        <a href="#" style="color: #2563eb; text-decoration: none; font-size: 13px; margin: 0 10px;">Terms of Service</a>
                    </div>
                </td>
            </tr>
        </table>
    </body>
    </html>
    """.formatted(username);
            helper.setText(htmlContent, true); // 'true' ka matlab hai ye HTML hai
            mailSender.send(message);

        } catch (MessagingException e) {
            System.out.println("Email failed: " + e.getMessage());
        }
    }

    @Async
    public void sendStudentPerformanceReport(String toEmail, String username, Long problemId, int marks, String takenTime) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Coding Report: Problem #" + problemId + " Submission ✅");

            // Dynamic Color logic: 80+ marks pe green, baaki pe orange
            String statusColor = (marks >= 80) ? "#10b981" : "#f59e0b";

            String htmlContent = """
            <div style="font-family: 'Segoe UI', sans-serif; max-width: 550px; margin: auto; border: 1px solid #eee; border-radius: 15px; overflow: hidden;">
                <div style="background: #0f172a; color: white; padding: 30px; text-align: center;">
                    <h2 style="margin: 0;">Submission Received!</h2>
                    <p style="opacity: 0.7;">Keep pushing your limits.</p>
                </div>
                <div style="padding: 30px; background: #ffffff;">
                    <p>Hello <b>%s</b>,</p>
                    <p>Aapka code successfully submit ho gaya hai. Niche aapki performance details hain:</p>
                    
                    <div style="margin: 25px 0; padding: 20px; background: #f8fafc; border-radius: 10px; border: 1px solid #e2e8f0;">
                        <table width="100%%">
                            <tr>
                                <td style="color: #64748b; padding-bottom: 10px;">Problem ID:</td>
                                <td style="text-align: right; font-weight: bold; padding-bottom: 10px;">#%d</td>
                            </tr>
                            <tr>
                                <td style="color: #64748b; padding-bottom: 10px;">Score:</td>
                                <td style="text-align: right; font-weight: bold; color: %s; padding-bottom: 10px;">%d / 100</td>
                            </tr>
                            <tr>
                                <td style="color: #64748b;">Time Taken:</td>
                                <td style="text-align: right; font-weight: bold;">%s</td>
                            </tr>
                        </table>
                    </div>
                    
                    <p style="font-size: 14px; color: #475569;">Aap dashboard par jaakar complete analysis aur test cases dekh sakte hain.</p>
                </div>
                <div style="text-align: center; padding: 20px; background: #f1f5f9; font-size: 12px; color: #94a3b8;">
                    Build with ❤️ by POD Panel Team
                </div>
            </div>
            """.formatted(username, problemId, statusColor, marks, takenTime);

            helper.setText(htmlContent, true);
            mailSender.send(message);

        } catch (MessagingException e) {
            // Sirf log karein taaki main flow na toote
            System.err.println("Async Mail Error: " + e.getMessage());
        }
    }

}