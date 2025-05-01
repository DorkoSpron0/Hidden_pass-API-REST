package com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.adapters;

import com.sena.hidden_pass.application.config.JwtFilter;
import com.sena.hidden_pass.domain.models.UserLoginModel;
import com.sena.hidden_pass.domain.models.UserModel;
import com.sena.hidden_pass.domain.usecases.UserUseCases;
import com.sena.hidden_pass.domain.valueObjects.EmailValueObject;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.UserDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.IUserRepository;
import com.sena.hidden_pass.infrastructure.mappers.UserMapper;
import com.sena.hidden_pass.infrastructure.services.MailService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IUserAdapter implements UserUseCases {

    private final IUserRepository userRepository;
    private final JwtFilter jwtFilter;
    private final PasswordEncoder passwordEncoder;
    private MailService mailService;

    public IUserAdapter(JwtFilter jwtFilter, IUserRepository userRepository, PasswordEncoder passwordEncoder, MailService mailService) {
        this.jwtFilter = jwtFilter;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    @Override
    public UserModel registerUser(UserModel UserDBO) {
        try{
            String username = UserDBO.getUsername().getUsername(); // Asumiendo que getUsername() devuelve el nombre directamente

            String body = String.format("""
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

            mailService.sendEmailAyncImpl(UserDBO.getEmail().getEmail(), "BIENVENIDO A HIDDEN PASS", body);
            UserDBO.setMaster_password(passwordEncoder.encode(UserDBO.getMaster_password()));
            return UserMapper.userDBOToModel(userRepository.save(UserMapper.userModelToDBO(UserDBO)));
        }catch (Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public UserModel getUserById(UUID id) {
        return UserMapper.userDBOToModel(userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User with id " + id + " not found")));
    }

    @Override
    public UserModel getUserByUsername(String username) {
        return UserMapper.userDBOToModel(userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found")));
    }

    @Override
    public UserModel getUserByUEmail(String email) {
        return UserMapper.userDBOToModel(userRepository.findByEmail(new EmailValueObject(email)).orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found")));
    }

    @Override
    public UserLoginModel loginUser(UserModel UserDBO) {
        UserDBO userFounded = userRepository.findByEmail_Email(UserDBO.getEmail().getEmail()).orElseThrow(() -> new UsernameNotFoundException("User with email " + UserDBO.getEmail() + " not found"));

        if(!matchPassword(UserDBO.getMaster_password(), userFounded.getMaster_password())) throw new IllegalArgumentException("Credenciales incorrectas");

        return new UserLoginModel(
                userFounded.getId_usuario(),
                userFounded.getUsername(),
                userFounded.getEmail(),
                jwtFilter.generateToken(userFounded.getId_usuario()),
                userFounded.getUrl_image()
        );
    }

    @Override
    public UserModel updateUser(UUID id, UserModel userModel) {

        UserDBO userFounded = UserMapper.userModelToDBO(getUserById(id));
            userFounded.setEmail(userModel.getEmail());
            userFounded.setUsername(userModel.getUsername());
            userFounded.setMaster_password(userFounded.getMaster_password());

            userFounded.setUrl_image(userModel.getUrl_image());

            userFounded.setPasswordList(userFounded.getPasswordList());
            userFounded.setNoteList(userFounded.getNoteList());
            userFounded.setFolderList(userFounded.getFolderList());

            return UserMapper.userDBOToModel(userRepository.save(userFounded));
    }

    @Override
    public UserModel deleteUser(UUID id) {
        return null;
    }

    @Override
    public UserModel recoverMasterPassword(String password, EmailValueObject email) {
        UserDBO userFounded = UserMapper.userModelToDBO(getUserByUEmail(email.getEmail()));

        userFounded.setMaster_password(passwordEncoder.encode(password));

        userRepository.save(userFounded);

        return UserMapper.userDBOToModel(userFounded);
    }

    @Override
    public UserModel updateMasterPassword(UUID id, String current_password, String new_password) {
        UserDBO userFounded = UserMapper.userModelToDBO(getUserById(id));

        if(!matchPassword(current_password, userFounded.getMaster_password())) throw new IllegalArgumentException("Credenciales incorrectas");

        userFounded.setMaster_password(passwordEncoder.encode(new_password));
        userRepository.save(userFounded);

        return UserMapper.userDBOToModel(userFounded);
    }

    public boolean matchPassword(String rawPassword, String encodedPassword){
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
