package br.com.empresa.intranet.security;

import br.com.empresa.intranet.entity.Usuario;
import br.com.empresa.intranet.exception.RecursoNaoEncontradoException;
import br.com.empresa.intranet.repository.UsuarioRepository;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticatedUserService {

    private final UsuarioRepository usuarioRepository;

    public AuthenticatedUserService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public Usuario getUsuarioAutenticado() {
        return getUsuarioAutenticadoOptional()
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario autenticado nao encontrado"));
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> getUsuarioAutenticadoOptional() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        String username = authentication.getName();
        if (username == null || "anonymousUser".equals(username)) {
            return Optional.empty();
        }

        return usuarioRepository.findByUsernameIgnoreCase(username);
    }
}
