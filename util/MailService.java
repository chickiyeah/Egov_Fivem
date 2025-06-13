package egovframework.com.fivemlist.util;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

import org.springframework.stereotype.Component;

@Component
public class MailService {

    private final String SMTP_HOST = "smtp.gmail.com";
    private final int SMTP_PORT = 587;
    private final String USERNAME = "noreplyfrozenrp@gmail.com";
    private final String PASSWORD = "xmyz nxos itsn igyv";

    public void sendHtmlEmail(String toEmail, String subject, String htmlBody) {
        Properties props = new Properties();

        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.ssl.trust", SMTP_HOST);

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME, "관리자", "UTF-8"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);

            // HTML 내용 설정
            message.setContent(htmlBody, "text/html; charset=UTF-8");

            Transport.send(message);
            System.out.println("📨 HTML 이메일 전송 성공 → " + toEmail);
        } catch (Exception e) {
            System.err.println("❌ 이메일 전송 실패");
            e.printStackTrace();
        }
    }
}
