package br.com.empresa.intranet.repository;

import br.com.empresa.intranet.entity.Colaborador;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {

    Optional<Colaborador> findByEmailCorporativoIgnoreCase(String emailCorporativo);

    @Query("""
            select c
            from Colaborador c
            where c.ativo = true
              and (
                    :termo is null
                    or :termo = ''
                    or lower(c.nomeCompleto) like lower(concat('%', :termo, '%'))
                    or lower(c.emailCorporativo) like lower(concat('%', :termo, '%'))
                    or lower(c.departamento) like lower(concat('%', :termo, '%'))
                    or lower(c.cargo) like lower(concat('%', :termo, '%'))
              )
            order by c.nomeCompleto asc
            """)
    Page<Colaborador> pesquisarAtivos(@Param("termo") String termo, Pageable pageable);
}
