package com.sena.hidden_pass.infrastructure.services;

import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.SecurityCodesDBO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
public class MailService {

    private JavaMailSender javaMailSender;

    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void asyncMethodWithVoidReturnType() {
        System.out.println("Execute method asynchronously. "
                + Thread.currentThread().getName());
    }


    @Async
    public Future<String> asyncMethodWithReturnType() {
        System.out.println("Execute method asynchronously - "
                + Thread.currentThread().getName());
        try {
            Thread.sleep(5000);
            return new AsyncResult<>("hello world !!!!");
        } catch (InterruptedException e) {
            //
        }

        return null;
    }

    @Async
    public void sendEmailAsync(String to, String subject, String body) {
        System.out.println("Sending email in thread: " + Thread.currentThread().getName());

        // Simula envío de correo
        try {
            Thread.sleep(5000); // Simula tiempo de envío
            System.out.println("Correo enviado a " + to);
        } catch (InterruptedException e) {
            System.out.println("Error al enviar el correo");
        }
    }

    @Async
    public void sendEmailAyncImpl(String email, String subject, String body) throws MessagingException {
        try{
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(body, true); // 'true' permite HTML en el mensaje

            javaMailSender.send(message);

            System.out.println("Correo enviado a " + email);
        }catch (MessagingException ex){
            System.out.println(ex.getMessage());
        }
    }
}
