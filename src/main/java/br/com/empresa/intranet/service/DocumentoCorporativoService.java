package br.com.empresa.intranet.service;

import br.com.empresa.intranet.entity.DocumentoCorporativo;
import br.com.empresa.intranet.entity.Usuario;
import br.com.empresa.intranet.entity.enums.Role;
import java.util.Set;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentoCorporativoService {

    Page<DocumentoCorporativo> pesquisar(String termo, Pageable pageable);

    Page<DocumentoCorporativo> pesquisarAcessiveis(String termo, Usuario usuario, Pageable pageable);

    DocumentoCorporativo buscarAtivoPorId(Long id);

    DocumentoCorporativo criar(
            String titulo,
            String descricao,
            String categoria,
            MultipartFile arquivo,
            Set<Role> rolesPermitidas,
            Usuario enviadoPor
    );

    DocumentoCorporativo atualizarMetadados(
            Long id,
            String titulo,
            String descricao,
            String categoria,
            Set<Role> rolesPermitidas
    );

    Resource carregarArquivo(DocumentoCorporativo documento);

    boolean usuarioPodeAcessar(DocumentoCorporativo documento, Usuario usuario);

    void excluirLogicamente(Long id);
}
