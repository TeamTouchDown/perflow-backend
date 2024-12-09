package com.touchdown.perflowbackend.employee.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final String BASE_URL = "http://localhost:8080/api/v1/hr/employees/pwd";

    public void sendStyledEmail(String toEmail, String employeeName, String token) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            // 이메일 제목 설정
            helper.setSubject("Welcome to Our Company!");
            helper.setTo(toEmail);

            String invitationLink = BASE_URL + "/" + token;

            // HTML 템플릿 본문 생성
            String emailContent = buildHtmlContent(employeeName, invitationLink);

            // 본문 설정 (HTML 형식)
            helper.setText(emailContent, true);

            // 이메일 전송
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new CustomException(ErrorCode.SEND_EMAIL_ERROR);
        }
    }

    private String buildHtmlContent(String name, String link) {
        return """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <style>
                            .email-container { font-family: Arial, sans-serif; line-height: 1.6; color: #333; padding: 20px; border: 1px solid #ddd; border-radius: 8px; width: 600px; margin: 0 auto; }
                            .header { text-align: center; font-size: 20px; font-weight: bold; color: #007bff; }
                            .content { margin-top: 20px; }
                            .footer { margin-top: 30px; text-align: center; font-size: 12px; color: #777; }
                            .button { display: inline-block; padding: 10px 20px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px; }
                        </style>
                    </head>
                    <body>
                        <div class="email-container">
                            <div class="header">Welcome to Our Company</div>
                            <div class="content">
                                <p>Dear %s,</p>
                                <p>We are excited to invite you to our platform. Please click the link below to set up your initial password and complete your onboarding process:</p>
                                <a href="%s" class="button">Set Your Password</a>
                                <p>If you have any questions, feel free to contact us.</p>
                            </div>
                            <div class="footer">
                                © Our Company. All rights reserved.
                            </div>
                        </div>
                    </body>
                    </html>
                """.formatted(name, link);
    }
}
