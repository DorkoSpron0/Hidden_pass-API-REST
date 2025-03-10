package com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.adapters;

import com.sena.hidden_pass.domain.usecases.SecurityCodesCases;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.SecurityCodesDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.UserDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.ISecurityCodesRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class ISecurityCodesAdapter implements SecurityCodesCases {

    private IUserAdapter userAdapter;

    private ISecurityCodesRepository securityCodesRepository;

    private JavaMailSender javaMailSender;

    @Override
    public String sendSecurityCode(String email) throws MessagingException {
        UserDBO userFounded = userAdapter.getUserByUEmail(email);

        SecurityCodesDBO newSecurityCode = securityCodesRepository.save(new SecurityCodesDBO(userFounded));

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject("HIDDEN PASS - SECURITY CODE");
        helper.setText("Tu codigo de seguridad es: " + newSecurityCode.getSecurity_code(), true); // 'true' permite HTML en el mensaje

        javaMailSender.send(message);


        return "El código de seguridad fue enviado al correo: " + email +  " revisa tu bandeja de entrada.";
    }

    @Override
    public SecurityCodesDBO getSecurityCode() {
        return null;
    }

    @Override
    public boolean validateSecurityCode(UUID security_code, String user_email) {
        UserDBO userFounded = userAdapter.getUserByUEmail(user_email);

        SecurityCodesDBO codeFounded = securityCodesRepository.findByUserDBO(userFounded).orElseThrow(() -> new IllegalArgumentException("User don't have securityCode"));

        System.out.println(codeFounded.getSecurity_code());
        System.out.println(security_code);

        if(!codeFounded.getSecurity_code().equals(security_code)) throw new IllegalArgumentException("Security Code invalid");

        return true;
    }
}
