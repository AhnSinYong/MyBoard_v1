package com.board.portfolio.mail;

import com.board.portfolio.mail.manager.AuthMail;
import com.board.portfolio.mail.manager.AuthMailManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@RequiredArgsConstructor
@Component
public class EmailSender {
    private final JavaMailSender mailSender;
    private final AuthMailManager manager;
    @Value("${mail.from}")
    private String from;

    @Async
    public void sendAuthMail(AuthMail authMail) {
        String to = authMail.getEmail();
        String authKey = authMail.getAuthKey();
        MimeMessage message = mailSender.createMimeMessage();
        try {
            message.setSubject("[인증] 회원 가입 인증", "UTF-8");
            message.setText(getAuthContent(to,authKey), "UTF-8", "html");
            message.setFrom(from);
            message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(to));
            mailSender.send(message);
            manager.startManage(authMail);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (MailException e) {
            e.printStackTrace();
        }
    }
    private String getAuthContent(String to, String authKey){
        return new StringBuffer()
                .append("<h1>[이메일 인증]</h1>")
                .append("<p>아래 링크를 클릭하시면 이메일 인증이 완료됩니다.</p>")
                .append("<a href='"+"localhost:8181/api/authenticate?")
                .append("email="+to)
                .append("&authKey="+authKey)
                .append("' target='_blenk'>이메일 인증 확인</a>")
                .toString();
    }

    public void completeAuthMail(String email){
        manager.removeAuthMailList(email);
    }

}
