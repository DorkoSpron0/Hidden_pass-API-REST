package com.sena.hidden_pass.infrastructure.services;

import org.springframework.stereotype.Service;

public class MailTemplate {

    public static String buildTemplate(String username){
        return String.format("""
                <!DOCTYPE html>
                <html lang="es">
                  <head>
                    <meta charset="UTF-8" />
                    <title>Bienvenido a Hidden Pass</title>
                  </head>
                  <body style="margin: 0; padding: 0; background-color: #f4f4f7; font-family: Arial, sans-serif;">
                    <table align="center" width="100%%" cellpadding="0" cellspacing="0" style="padding: 40px 0;">
                      <tr>
                        <td>
                          <table align="center" width="600" cellpadding="0" cellspacing="0" style="background-color: #ffffff; padding: 30px; border-radius: 6px;">
                            <tr>
                              <td align="center" style="padding-bottom: 20px;">
                                <img src="https://i.ibb.co/7xP5SDfn/Logo-Simple.png" alt="Hidden Pass Logo" width="80" style="display: block; margin-bottom: 10px;" />
                                <h1 style="margin: 0; font-size: 24px; color: #222222;">¡Bienvenido a Hidden Pass!</h1>
                              </td>
                            </tr>
                            <tr>
                              <td style="font-size: 16px; color: #333333; line-height: 1.5;">
                                <p>Hola %s,</p>
                                <p>Gracias por registrarte en <strong>Hidden Pass</strong>, tu nuevo gestor seguro de contraseñas y notas.</p>
                                <p>Con Hidden Pass podrás:</p>
                                <ul style="padding-left: 20px;">
                                  <li>Guardar tus contraseñas de forma segura y cifrada</li>
                                  <li>Guardar tus notas de forma segura</li>
                                  <li>Acceder a tus datos desde múltiples dispositivos</li>
                                  <li>Generar contraseñas seguras con un solo click</li>
                                </ul>
                                <p>Si tienes alguna pregunta, no dudes en contactarnos. ¡Estamos aquí para ayudarte!</p>
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
                """, username);
    }
}
