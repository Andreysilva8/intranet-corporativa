package br.com.empresa.intranet.service;

import br.com.empresa.intranet.entity.Usuario;
import br.com.empresa.intranet.entity.enums.Role;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsuarioService {

    Page<Usuario> listar(Pageable pageable);

    Usuario buscarPorId(Long id);

    Usuario buscarPorUsername(String username);

    Optional<Usuario> buscarPorUsernameOptional(String username);

    Usuario criar(Usuario usuario, String senhaEmTexto, Set<Role> roles);

    Usuario atualizar(Long id, Usuario dadosAtualizados, String novaSenhaEmTexto, Set<Role> roles);

    void alterarStatus(Long id, boolean ativo);
}
