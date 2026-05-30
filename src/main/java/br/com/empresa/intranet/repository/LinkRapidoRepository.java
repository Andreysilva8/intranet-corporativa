package br.com.empresa.intranet.repository;

import br.com.empresa.intranet.entity.LinkRapido;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRapidoRepository extends JpaRepository<LinkRapido, Long> {

    List<LinkRapido> findByAtivoTrueOrderByOrdemExibicaoAscTituloAsc();
}
