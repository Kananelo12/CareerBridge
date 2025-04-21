package utils;

import java.io.IOException;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author kanan
 */
public final class NotificationService {
    
    private static final String SENDER_EMAIL;
    private static final String SENDER_PASSWORD;
    private static final Session MAIL_SESSION;
    
    static {
        String envPath = System.getProperty("env.filepath");
        if (envPath == null) {
            throw new IllegalStateException("System property 'env.filepath' must be set");
        }

        EnvLoader loader;
        try {
            loader = new EnvLoader(envPath);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load environment file: " + envPath, e);
        }

        SENDER_EMAIL = loader.get("SENDER_EMAIL");
        SENDER_PASSWORD = loader.get("APP_PASSWORD");
        if (SENDER_EMAIL == null || SENDER_PASSWORD == null) {
            throw new IllegalStateException("Email configuration missing: ensure SENDER_EMAIL and APP_PASSWORD are set");
        }

        Properties mailProps = new Properties();
        mailProps.put("mail.smtp.auth", "true");
        mailProps.put("mail.smtp.starttls.enable", "true");
        mailProps.put("mail.smtp.host", "smtp.gmail.com");
        mailProps.put("mail.smtp.port", "587");

        MAIL_SESSION = Session.getInstance(mailProps, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });
    }
    
    // Prevent instantiation
    private NotificationService() { }
    
    public static void sendEmail(String recipientEmail, String subject, String body) throws MessagingException {
        MimeMessage message = new MimeMessage(MAIL_SESSION);
        message.setFrom(new InternetAddress(SENDER_EMAIL));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
    }
}
