package br.com.empresa.intranet.service;

import br.com.empresa.intranet.entity.LinkRapido;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LinkRapidoService {

    Page<LinkRapido> listar(Pageable pageable);

    List<LinkRapido> listarAtivos();

    LinkRapido buscarPorId(Long id);

    LinkRapido criar(LinkRapido linkRapido);

    LinkRapido atualizar(Long id, LinkRapido dadosAtualizados);

    void alterarStatus(Long id, boolean ativo);
}
