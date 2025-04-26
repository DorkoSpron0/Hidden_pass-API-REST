package com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.adapters;

import com.sena.hidden_pass.domain.models.SecurityCodesModel;
import com.sena.hidden_pass.domain.usecases.SecurityCodesCases;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.SecurityCodesDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.UserDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.ISecurityCodesRepository;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.IUserRepository;
import com.sena.hidden_pass.infrastructure.mappers.UserMapper;
import com.sena.hidden_pass.infrastructure.services.MailService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class ISecurityCodesAdapter implements SecurityCodesCases {

    private IUserAdapter userAdapter;

    private ISecurityCodesRepository securityCodesRepository;

    private IUserRepository userRepository;

    private MailService mailService;

    @Override
    public String sendSecurityCode(String email) throws MessagingException {
        UserDBO userFounded = UserMapper.userModelToDBO(userAdapter.getUserByUEmail(email));

        SecurityCodesDBO newSecurityCode = securityCodesRepository.save(new SecurityCodesDBO());

        userFounded.setSecurityCodes(newSecurityCode);

        userRepository.save(userFounded);

        String body = String.format("""
    <!DOCTYPE html>
    <html lang="es">
      <head>
        <meta charset="UTF-8" />
        <title>Código de verificación - Hidden Pass</title>
      </head>
      <body style="margin: 0; padding: 0; background-color: #f4f4f7; font-family: Arial, sans-serif;">
        <table align="center" width="100%%" cellpadding="0" cellspacing="0" style="padding: 40px 0;">
          <tr>
            <td>
              <table align="center" width="600" cellpadding="0" cellspacing="0" style="background-color: #ffffff; padding: 30px; border-radius: 6px;">
                <tr>
                  <td align="center" style="padding-bottom: 20px;">
                    <img src="https://i.ibb.co/7xP5SDfn/Logo-Simple.png" alt="Hidden Pass Logo" width="80" style="display: block; margin-bottom: 10px;" />
                    <h1 style="margin: 0; font-size: 24px; color: #222222;">Código de verificación</h1>
                  </td>
                </tr>
                <tr>
                  <td style="font-size: 16px; color: #333333; line-height: 1.5;">
                    <p>Hola,</p>
                    <p>Estás intentando realizar una acción que requiere verificación.</p>
                    <p><strong>Tu código de seguridad es:</strong></p>
                    <p style="font-size: 28px; font-weight: bold; text-align: center; color: #007bff; margin: 20px 0;">%s</p>
                    <p>Si no fuiste tú quien lo solicitó, puedes ignorar este mensaje de forma segura.</p>
                    <p>— El equipo de Hidden Pass</p>
                  </td>
                </tr>
                <tr>
                  <td align="center" style="font-size: 12px; color: #999999; padding-top: 30px;">
                    © 2025 Hidden Pass. Todos los derechos reservados.<br />
                    Este correo fue enviado automáticamente, por favor no respondas a esta dirección.
                  </td>
                </tr>
              </table>
            </td>
          </tr>
        </table>
      </body>
    </html>
    """, newSecurityCode.getSecurity_code());


        mailService.sendEmailAyncImpl(email, "HIDDEN PASS - SECURITY CODE", body);

        return "El código de seguridad fue enviado al correo: " + email +  " revisa tu bandeja de entrada.";
    }

    @Override
    public SecurityCodesModel getSecurityCode() {
        return null;
    }

    @Override
    public boolean validateSecurityCode(UUID security_code, String user_email) {
        UserDBO userFounded = UserMapper.userModelToDBO(userAdapter.getUserByUEmail(user_email));

        SecurityCodesDBO codeFounded = Optional.ofNullable(userFounded.getSecurityCodes())
                .orElseThrow(() -> new IllegalArgumentException("User dont Have security code"));

        if(!codeFounded.getSecurity_code().equals(security_code)) throw new IllegalArgumentException("Security Code invalid");

        userFounded.setSecurityCodes(null);
        userRepository.save(userFounded);

        return true;
    }
}
