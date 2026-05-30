package br.com.empresa.intranet.service.impl;

import br.com.empresa.intranet.entity.Usuario;
import br.com.empresa.intranet.entity.enums.Role;
import br.com.empresa.intranet.exception.RecursoNaoEncontradoException;
import br.com.empresa.intranet.exception.RegraNegocioException;
import br.com.empresa.intranet.repository.UsuarioRepository;
import br.com.empresa.intranet.service.UsuarioService;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional(readOnly = true)
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Page<Usuario> listar(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    @Override
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario nao encontrado"));
    }

    @Override
    public Usuario buscarPorUsername(String username) {
        return buscarPorUsernameOptional(username)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario nao encontrado"));
    }

    @Override
    public Optional<Usuario> buscarPorUsernameOptional(String username) {
        if (!StringUtils.hasText(username)) {
            return Optional.empty();
        }
        return usuarioRepository.findByUsernameIgnoreCase(username.trim());
    }

    @Override
    @Transactional
    public Usuario criar(Usuario usuario, String senhaEmTexto, Set<Role> roles) {
        if (!StringUtils.hasText(senhaEmTexto)) {
            throw new RegraNegocioException("A senha e obrigatoria para criar usuario");
        }

        normalizarUsuario(usuario);
        validarUnicidade(null, usuario.getUsername(), usuario.getEmail());
        usuario.setSenha(passwordEncoder.encode(senhaEmTexto));
        usuario.setRoles(normalizarRoles(roles));

        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public Usuario atualizar(Long id, Usuario dadosAtualizados, String novaSenhaEmTexto, Set<Role> roles) {
        Usuario usuario = buscarPorId(id);

        dadosAtualizados.setUsername(normalizarUsername(dadosAtualizados.getUsername()));
        dadosAtualizados.setEmail(normalizarEmail(dadosAtualizados.getEmail()));
        validarUnicidade(id, dadosAtualizados.getUsername(), dadosAtualizados.getEmail());

        usuario.setNome(normalizarTexto(dadosAtualizados.getNome()));
        usuario.setUsername(dadosAtualizados.getUsername());
        usuario.setEmail(dadosAtualizados.getEmail());
        usuario.setAtivo(dadosAtualizados.isAtivo());

        if (StringUtils.hasText(novaSenhaEmTexto)) {
            usuario.setSenha(passwordEncoder.encode(novaSenhaEmTexto));
        }

        usuario.setRoles(normalizarRoles(roles));

        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void alterarStatus(Long id, boolean ativo) {
        Usuario usuario = buscarPorId(id);
        usuario.setAtivo(ativo);
        usuarioRepository.save(usuario);
    }

    private void validarUnicidade(Long idAtual, String username, String email) {
        usuarioRepository.findByUsernameIgnoreCase(username)
                .filter(usuario -> !Objects.equals(usuario.getId(), idAtual))
                .ifPresent(usuario -> {
                    throw new RegraNegocioException("Username ja cadastrado");
                });

        usuarioRepository.findByEmailIgnoreCase(email)
                .filter(usuario -> !Objects.equals(usuario.getId(), idAtual))
                .ifPresent(usuario -> {
                    throw new RegraNegocioException("E-mail ja cadastrado");
                });
    }

    private void normalizarUsuario(Usuario usuario) {
        usuario.setNome(normalizarTexto(usuario.getNome()));
        usuario.setUsername(normalizarUsername(usuario.getUsername()));
        usuario.setEmail(normalizarEmail(usuario.getEmail()));
    }

    private String normalizarUsername(String username) {
        String valor = normalizarTexto(username);
        if (!StringUtils.hasText(valor)) {
            throw new RegraNegocioException("Username e obrigatorio");
        }
        return valor.toLowerCase();
    }

    private String normalizarEmail(String email) {
        String valor = normalizarTexto(email);
        if (!StringUtils.hasText(valor)) {
            throw new RegraNegocioException("E-mail e obrigatorio");
        }
        return valor.toLowerCase();
    }

    private String normalizarTexto(String valor) {
        return valor == null ? null : valor.trim();
    }

    private Set<Role> normalizarRoles(Set<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return EnumSet.of(Role.COLABORADOR);
        }
        return EnumSet.copyOf(roles);
    }
}
