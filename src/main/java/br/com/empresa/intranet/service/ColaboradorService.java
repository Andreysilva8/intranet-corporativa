package br.com.empresa.intranet.service;

import br.com.empresa.intranet.entity.Colaborador;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ColaboradorService {

    Page<Colaborador> listar(Pageable pageable);

    Page<Colaborador> pesquisar(String termo, Pageable pageable);

    Colaborador buscarPorId(Long id);

    Colaborador criar(Colaborador colaborador);

    Colaborador atualizar(Long id, Colaborador dadosAtualizados);

    void alterarStatus(Long id, boolean ativo);
}
