package br.com.empresa.intranet.controller;

import br.com.empresa.intranet.dto.request.RecadoForm;
import br.com.empresa.intranet.entity.Recado;
import br.com.empresa.intranet.entity.Usuario;
import br.com.empresa.intranet.exception.RegraNegocioException;
import br.com.empresa.intranet.security.AuthenticatedUserService;
import br.com.empresa.intranet.service.RecadoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
@RequestMapping("/rh/recados")
@PreAuthorize("hasAnyRole('ADMIN', 'RH')")
public class RecadoAdminController {

    private final RecadoService recadoService;
    private final AuthenticatedUserService authenticatedUserService;

    public RecadoAdminController(
            RecadoService recadoService,
            AuthenticatedUserService authenticatedUserService
    ) {
        this.recadoService = recadoService;
        this.authenticatedUserService = authenticatedUserService;
    }

    @GetMapping
    public String listar(@PageableDefault(size = 15) Pageable pageable, Model model) {
        model.addAttribute("pagina", recadoService.listar(pageable));
        return "rh/recados/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("recadoForm", new RecadoForm());
        model.addAttribute("modoEdicao", false);
        return "rh/recados/form";
    }

    @PostMapping
    public String criar(
            @Valid @ModelAttribute("recadoForm") RecadoForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("modoEdicao", false);
            return "rh/recados/form";
        }

        try {
            Usuario autor = authenticatedUserService.getUsuarioAutenticado();
            recadoService.criar(form.toEntity(autor));
            redirectAttributes.addFlashAttribute("sucesso", "Recado publicado com sucesso.");
            return "redirect:/rh/recados";
        } catch (RegraNegocioException ex) {
            bindingResult.reject("recado", ex.getMessage());
            model.addAttribute("modoEdicao", false);
            return "rh/recados/form";
        }
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Recado recado = recadoService.buscarPorId(id);
        model.addAttribute("recado", recado);
        model.addAttribute("recadoForm", RecadoForm.fromEntity(recado));
        model.addAttribute("modoEdicao", true);
        return "rh/recados/form";
    }

    @PostMapping("/{id}")
    public String atualizar(
            @PathVariable Long id,
            @Valid @ModelAttribute("recadoForm") RecadoForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("recado", recadoService.buscarPorId(id));
            model.addAttribute("modoEdicao", true);
            return "rh/recados/form";
        }

        try {
            Usuario autor = authenticatedUserService.getUsuarioAutenticado();
            recadoService.atualizar(id, form.toEntity(autor));
            redirectAttributes.addFlashAttribute("sucesso", "Recado atualizado com sucesso.");
            return "redirect:/rh/recados";
        } catch (RegraNegocioException ex) {
            bindingResult.reject("recado", ex.getMessage());
            model.addAttribute("recado", recadoService.buscarPorId(id));
            model.addAttribute("modoEdicao", true);
            return "rh/recados/form";
        }
    }

    @PostMapping("/{id}/status")
    public String alterarStatus(
            @PathVariable Long id,
            @RequestParam boolean ativo,
            RedirectAttributes redirectAttributes
    ) {
        recadoService.alterarStatus(id, ativo);
        redirectAttributes.addFlashAttribute("sucesso", "Status do recado atualizado.");
        return "redirect:/rh/recados";
    }
}
