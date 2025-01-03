package com.touchdown.perflowbackend.employee.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final String BASE_URL;

    public EmailService (
            JavaMailSender mailSender,
            @Value("${EMPLOYEE_REGISTER_BASE_URL}") String baseUrl
    ) {
        this.mailSender = mailSender;
        this.BASE_URL = baseUrl;
    }

    @Async
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
                <meta charset="UTF-8">
                <title>초대 메세지</title>
            </head>
            <body>
                <div style="font-family: Arial, sans-serif; line-height: 1.6; color: #333; padding: 20px; border: 1px solid #ddd; border-radius: 8px; width: 600px; margin: 0 auto;">
                    <div style="text-align: center; font-size: 20px; font-weight: bold; color: #F37321;">
                        Perflow에서 전송된 초대 메세지입니다.
                    </div>
                    <div style="margin-top: 20px;">
                        <p>Dear %s,</p>
                        <p>서비스 사용을 위해 초기 비밀번호를 입력해주세요.</p>
                        <a href="%s" style="display: inline-block; padding: 10px 20px; background-color: #F37321; color: #FFFFFF; text-decoration: none; border-radius: 5px;">비밀번호 등록</a>
                        <p>문의 사항이 있으시다면 아래로 연락주시길 바랍니다.</p>
                    </div>
                    <div style="margin-top: 30px; text-align: center; font-size: 12px; color: #777;">
                        © Our Company. All rights reserved.
                    </div>
                </div>
            </body>
            </html>
            """.formatted(name, link);
    }
}
