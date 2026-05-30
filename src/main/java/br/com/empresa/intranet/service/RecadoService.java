package br.com.empresa.intranet.service;

import br.com.empresa.intranet.entity.Recado;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecadoService {

    Page<Recado> listar(Pageable pageable);

    List<Recado> listarVisiveis();

    Recado buscarPorId(Long id);

    Recado criar(Recado recado);

    Recado atualizar(Long id, Recado dadosAtualizados);

    void alterarStatus(Long id, boolean ativo);
}
