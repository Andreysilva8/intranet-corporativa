package br.com.empresa.intranet.repository;

import br.com.empresa.intranet.entity.Recado;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecadoRepository extends JpaRepository<Recado, Long> {

    @Query("""
            select r
            from Recado r
            where r.ativo = true
              and r.dataPublicacao <= :agora
              and (r.dataExpiracao is null or r.dataExpiracao >= :agora)
            order by r.destaque desc, r.dataPublicacao desc
            """)
    List<Recado> buscarRecadosVisiveis(@Param("agora") LocalDateTime agora);
}
