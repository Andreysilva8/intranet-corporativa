package br.com.empresa.intranet.config;

import br.com.empresa.intranet.entity.Usuario;
import br.com.empresa.intranet.entity.enums.Role;
import br.com.empresa.intranet.repository.UsuarioRepository;
import java.util.EnumSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminUsername;
    private final String adminEmail;
    private final String adminPassword;

    public DataInitializer(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.bootstrap.admin-username}") String adminUsername,
            @Value("${app.bootstrap.admin-email}") String adminEmail,
            @Value("${app.bootstrap.admin-password}") String adminPassword
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminUsername = adminUsername;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void criarAdminInicial() {
        if (usuarioRepository.existsByUsernameIgnoreCase(adminUsername)) {
            return;
        }

        Usuario admin = new Usuario(
                "Administrador",
                adminUsername,
                adminEmail,
                passwordEncoder.encode(adminPassword)
        );
        admin.setRoles(EnumSet.of(Role.ADMIN, Role.RH, Role.COLABORADOR));
        admin.setAtivo(true);

        usuarioRepository.save(admin);
    }
}
