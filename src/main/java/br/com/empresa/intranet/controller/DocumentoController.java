package br.com.empresa.intranet.controller;

import br.com.empresa.intranet.dto.request.DocumentoForm;
import br.com.empresa.intranet.entity.DocumentoCorporativo;
import br.com.empresa.intranet.entity.Usuario;
import br.com.empresa.intranet.entity.enums.Role;
import br.com.empresa.intranet.exception.RegraNegocioException;
import br.com.empresa.intranet.security.AuthenticatedUserService;
import br.com.empresa.intranet.service.DocumentoCorporativoService;
import jakarta.validation.Valid;
import java.nio.charset.StandardCharsets;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/documentos")
public class DocumentoController {

    private final DocumentoCorporativoService documentoService;
    private final AuthenticatedUserService authenticatedUserService;

    public DocumentoController(
            DocumentoCorporativoService documentoService,
            AuthenticatedUserService authenticatedUserService
    ) {
        this.documentoService = documentoService;
        this.authenticatedUserService = authenticatedUserService;
    }

    @ModelAttribute("roles")
    public Role[] roles() {
        return Role.values();
    }

    @GetMapping
    public String listar(
            @RequestParam(required = false) String termo,
            @PageableDefault(size = 12) Pageable pageable,
            Model model
    ) {
        Usuario usuario = authenticatedUserService.getUsuarioAutenticado();
        model.addAttribute("pagina", documentoService.pesquisarAcessiveis(termo, usuario, pageable));
        model.addAttribute("termo", termo);
        return "documentos/lista";
    }

    @GetMapping("/novo")
    @PreAuthorize("hasAnyRole('ADMIN', 'RH')")
    public String novo(Model model) {
        model.addAttribute("documentoForm", new DocumentoForm());
        model.addAttribute("modoEdicao", false);
        return "documentos/form";
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('ADMIN', 'RH')")
    public String upload(
            @Valid @ModelAttribute("documentoForm") DocumentoForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("modoEdicao", false);
            return "documentos/form";
        }

        try {
            Usuario usuario = authenticatedUserService.getUsuarioAutenticado();
            documentoService.criar(
                    form.getTitulo(),
                    form.getDescricao(),
                    form.getCategoria(),
                    form.getArquivo(),
                    form.getRolesPermitidas(),
                    usuario
            );
            redirectAttributes.addFlashAttribute("sucesso", "Documento enviado com sucesso.");
            return "redirect:/documentos";
        } catch (RegraNegocioException ex) {
            bindingResult.reject("documento", ex.getMessage());
            model.addAttribute("modoEdicao", false);
            return "documentos/form";
        }
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable Long id) {
        DocumentoCorporativo documento = documentoService.buscarAtivoPorId(id);
        Usuario usuario = authenticatedUserService.getUsuarioAutenticado();

        if (!documentoService.usuarioPodeAcessar(documento, usuario)) {
            throw new AccessDeniedException("Voce nao tem permissao para acessar este documento");
        }

        Resource arquivo = documentoService.carregarArquivo(documento);
        return ResponseEntity.ok()
                .contentType(parseMediaType(documento.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition(documento).toString())
                .body(arquivo);
    }

    @GetMapping("/{id}/editar")
    @PreAuthorize("hasAnyRole('ADMIN', 'RH')")
    public String editar(@PathVariable Long id, Model model) {
        DocumentoCorporativo documento = documentoService.buscarAtivoPorId(id);
        model.addAttribute("documento", documento);
        model.addAttribute("documentoForm", DocumentoForm.fromEntity(documento));
        model.addAttribute("modoEdicao", true);
        return "documentos/form";
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RH')")
    public String atualizar(
            @PathVariable Long id,
            @Valid @ModelAttribute("documentoForm") DocumentoForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("documento", documentoService.buscarAtivoPorId(id));
            model.addAttribute("modoEdicao", true);
            return "documentos/form";
        }

        try {
            documentoService.atualizarMetadados(
                    id,
                    form.getTitulo(),
                    form.getDescricao(),
                    form.getCategoria(),
                    form.getRolesPermitidas()
            );
            redirectAttributes.addFlashAttribute("sucesso", "Documento atualizado com sucesso.");
            return "redirect:/documentos";
        } catch (RegraNegocioException ex) {
            bindingResult.reject("documento", ex.getMessage());
            model.addAttribute("documento", documentoService.buscarAtivoPorId(id));
            model.addAttribute("modoEdicao", true);
            return "documentos/form";
        }
    }

    @PostMapping("/{id}/excluir")
    @PreAuthorize("hasAnyRole('ADMIN', 'RH')")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        documentoService.excluirLogicamente(id);
        redirectAttributes.addFlashAttribute("sucesso", "Documento removido com sucesso.");
        return "redirect:/documentos";
    }

    private ContentDisposition contentDisposition(DocumentoCorporativo documento) {
        return ContentDisposition.attachment()
                .filename(documento.getNomeArquivoOriginal(), StandardCharsets.UTF_8)
                .build();
    }

    private MediaType parseMediaType(String contentType) {
        try {
            return MediaType.parseMediaType(contentType);
        } catch (RuntimeException ex) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
