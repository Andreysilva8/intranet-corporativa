package br.com.empresa.intranet.service.impl;

import br.com.empresa.intranet.entity.DocumentoCorporativo;
import br.com.empresa.intranet.entity.Usuario;
import br.com.empresa.intranet.entity.enums.Role;
import br.com.empresa.intranet.exception.RecursoNaoEncontradoException;
import br.com.empresa.intranet.exception.RegraNegocioException;
import br.com.empresa.intranet.repository.DocumentoCorporativoRepository;
import br.com.empresa.intranet.service.DocumentoCorporativoService;
import br.com.empresa.intranet.service.DocumentoStorageService;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class DocumentoCorporativoServiceImpl implements DocumentoCorporativoService {

    private static final String CONTENT_TYPE_PADRAO = "application/octet-stream";

    private final DocumentoCorporativoRepository documentoRepository;
    private final DocumentoStorageService storageService;

    public DocumentoCorporativoServiceImpl(
            DocumentoCorporativoRepository documentoRepository,
            DocumentoStorageService storageService
    ) {
        this.documentoRepository = documentoRepository;
        this.storageService = storageService;
    }

    @Override
    public Page<DocumentoCorporativo> pesquisar(String termo, Pageable pageable) {
        return documentoRepository.pesquisarAtivos(normalizarTermoPesquisa(termo), pageable);
    }

    @Override
    public Page<DocumentoCorporativo> pesquisarAcessiveis(String termo, Usuario usuario, Pageable pageable) {
        if (usuario == null) {
            return Page.empty(pageable);
        }

        boolean admin = usuario.getRoles().contains(Role.ADMIN);
        return documentoRepository.pesquisarAtivosAcessiveis(
                normalizarTermoPesquisa(termo),
                usuario.getRoles(),
                admin,
                pageable
        );
    }

    @Override
    public DocumentoCorporativo buscarAtivoPorId(Long id) {
        return documentoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Documento nao encontrado"));
    }

    @Override
    @Transactional
    public DocumentoCorporativo criar(
            String titulo,
            String descricao,
            String categoria,
            MultipartFile arquivo,
            Set<Role> rolesPermitidas,
            Usuario enviadoPor
    ) {
        validarUpload(titulo, categoria, arquivo, enviadoPor);

        String nomeArquivoArmazenado = storageService.armazenar(arquivo);

        try {
            DocumentoCorporativo documento = new DocumentoCorporativo(
                    normalizarTextoObrigatorio(titulo, "Titulo e obrigatorio"),
                    normalizarTextoObrigatorio(categoria, "Categoria e obrigatoria"),
                    limparNomeOriginal(arquivo),
                    nomeArquivoArmazenado,
                    normalizarContentType(arquivo.getContentType()),
                    arquivo.getSize(),
                    enviadoPor
            );
            documento.setDescricao(normalizarTextoOpcional(descricao));
            documento.setRolesPermitidas(normalizarRoles(rolesPermitidas));

            return documentoRepository.save(documento);
        } catch (RuntimeException ex) {
            storageService.excluir(nomeArquivoArmazenado);
            throw ex;
        }
    }

    @Override
    @Transactional
    public DocumentoCorporativo atualizarMetadados(
            Long id,
            String titulo,
            String descricao,
            String categoria,
            Set<Role> rolesPermitidas
    ) {
        DocumentoCorporativo documento = buscarAtivoPorId(id);

        documento.setTitulo(normalizarTextoObrigatorio(titulo, "Titulo e obrigatorio"));
        documento.setDescricao(normalizarTextoOpcional(descricao));
        documento.setCategoria(normalizarTextoObrigatorio(categoria, "Categoria e obrigatoria"));
        documento.setRolesPermitidas(normalizarRoles(rolesPermitidas));

        return documentoRepository.save(documento);
    }

    @Override
    public Resource carregarArquivo(DocumentoCorporativo documento) {
        return storageService.carregarComoResource(documento.getNomeArquivoArmazenado());
    }

    @Override
    public boolean usuarioPodeAcessar(DocumentoCorporativo documento, Usuario usuario) {
        if (documento == null || usuario == null || !documento.isAtivo() || !usuario.isAtivo()) {
            return false;
        }

        if (usuario.getRoles().contains(Role.ADMIN)) {
            return true;
        }

        Set<Role> rolesPermitidas = documento.getRolesPermitidas();
        if (rolesPermitidas == null || rolesPermitidas.isEmpty()) {
            return true;
        }

        return usuario.getRoles().stream().anyMatch(rolesPermitidas::contains);
    }

    @Override
    @Transactional
    public void excluirLogicamente(Long id) {
        DocumentoCorporativo documento = buscarAtivoPorId(id);
        documento.setAtivo(false);
        documentoRepository.save(documento);
    }

    private void validarUpload(String titulo, String categoria, MultipartFile arquivo, Usuario enviadoPor) {
        normalizarTextoObrigatorio(titulo, "Titulo e obrigatorio");
        normalizarTextoObrigatorio(categoria, "Categoria e obrigatoria");

        if (arquivo == null || arquivo.isEmpty()) {
            throw new RegraNegocioException("Arquivo e obrigatorio");
        }

        if (enviadoPor == null) {
            throw new RegraNegocioException("Usuario responsavel pelo upload e obrigatorio");
        }
    }

    private String limparNomeOriginal(MultipartFile arquivo) {
        String nomeOriginal = Objects.requireNonNullElse(arquivo.getOriginalFilename(), "documento");
        String nomeLimpo = StringUtils.cleanPath(nomeOriginal);

        if (!StringUtils.hasText(nomeLimpo) || nomeLimpo.contains("..")) {
            throw new RegraNegocioException("Nome de arquivo invalido");
        }

        return nomeLimpo;
    }

    private String normalizarContentType(String contentType) {
        return StringUtils.hasText(contentType) ? contentType.trim() : CONTENT_TYPE_PADRAO;
    }

    private Set<Role> normalizarRoles(Set<Role> rolesPermitidas) {
        if (rolesPermitidas == null || rolesPermitidas.isEmpty()) {
            return EnumSet.allOf(Role.class);
        }
        return EnumSet.copyOf(rolesPermitidas);
    }

    private String normalizarTermoPesquisa(String termo) {
        return StringUtils.hasText(termo) ? termo.trim() : null;
    }

    private String normalizarTextoObrigatorio(String valor, String mensagemErro) {
        String texto = normalizarTextoOpcional(valor);
        if (!StringUtils.hasText(texto)) {
            throw new RegraNegocioException(mensagemErro);
        }
        return texto;
    }

    private String normalizarTextoOpcional(String valor) {
        return valor == null ? null : valor.trim();
    }
}
