package com.example.renting;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender extends AsyncTask<Void, Void, Void> {
    private String recipientEmail;
    private String subject;
    private String body;

    public EmailSender(String recipientEmail, String subject, String body) {
        this.recipientEmail = recipientEmail;
        this.subject = subject;
        this.body = body;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        final String username = "alina.maxemiuc@student.usv.ro";
        final String password = "hrqz tska fisk ijla";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setContent(body, "text/html; charset=utf-8");

            Transport.send(message);
        } catch (MessagingException e) {
            Log.e("EmailSender", "Error sending email: " + e.getMessage());
        }
        return null;
    }
}
