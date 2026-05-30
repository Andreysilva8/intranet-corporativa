package br.com.empresa.intranet.repository;

import br.com.empresa.intranet.entity.DocumentoCorporativo;
import br.com.empresa.intranet.entity.enums.Role;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DocumentoCorporativoRepository extends JpaRepository<DocumentoCorporativo, Long> {

    Optional<DocumentoCorporativo> findByIdAndAtivoTrue(Long id);

    Optional<DocumentoCorporativo> findByNomeArquivoArmazenado(String nomeArquivoArmazenado);

    @Query("""
            select d
            from DocumentoCorporativo d
            where d.ativo = true
              and (
                    :termo is null
                    or :termo = ''
                    or lower(d.titulo) like lower(concat('%', :termo, '%'))
                    or lower(d.categoria) like lower(concat('%', :termo, '%'))
                    or lower(d.nomeArquivoOriginal) like lower(concat('%', :termo, '%'))
              )
            order by d.criadoEm desc
            """)
    Page<DocumentoCorporativo> pesquisarAtivos(@Param("termo") String termo, Pageable pageable);

    @Query(
            value = """
                    select distinct d
                    from DocumentoCorporativo d
                    left join d.rolesPermitidas rolePermitida
                    where d.ativo = true
                      and (
                            :termo is null
                            or :termo = ''
                            or lower(d.titulo) like lower(concat('%', :termo, '%'))
                            or lower(d.categoria) like lower(concat('%', :termo, '%'))
                            or lower(d.nomeArquivoOriginal) like lower(concat('%', :termo, '%'))
                      )
                      and (
                            :admin = true
                            or rolePermitida in :rolesUsuario
                      )
                    order by d.criadoEm desc
                    """,
            countQuery = """
                    select count(distinct d)
                    from DocumentoCorporativo d
                    left join d.rolesPermitidas rolePermitida
                    where d.ativo = true
                      and (
                            :termo is null
                            or :termo = ''
                            or lower(d.titulo) like lower(concat('%', :termo, '%'))
                            or lower(d.categoria) like lower(concat('%', :termo, '%'))
                            or lower(d.nomeArquivoOriginal) like lower(concat('%', :termo, '%'))
                      )
                      and (
                            :admin = true
                            or rolePermitida in :rolesUsuario
                      )
                    """
    )
    Page<DocumentoCorporativo> pesquisarAtivosAcessiveis(
            @Param("termo") String termo,
            @Param("rolesUsuario") Set<Role> rolesUsuario,
            @Param("admin") boolean admin,
            Pageable pageable
    );
}
