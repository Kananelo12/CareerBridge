package utils;

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
public class EmailUtil {

    // Admin email
    private static final String SENDER_EMAIL = "kananeloj12@gmail.com";
    // Google App Password
    private static final String SENDER_PASSWORD = "yuok zbhi mhvv hcsv";

    public static void sendEmail(String recipientEmail, String subject, String body) throws MessagingException {
        // Set up SMTP Protocol properties.
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        // Creating a mail session with authentication.
        Session mailSession = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });

        // Build the email body.
        Message message = new MimeMessage(mailSession);
        message.setFrom(new InternetAddress(SENDER_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
        message.setSubject(subject);
        message.setText(body);

        // Send the email.
        Transport.send(message);
    }
}
