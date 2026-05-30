package br.com.empresa.intranet.controller;

import br.com.empresa.intranet.dto.request.LinkRapidoForm;
import br.com.empresa.intranet.entity.LinkRapido;
import br.com.empresa.intranet.exception.RegraNegocioException;
import br.com.empresa.intranet.service.LinkRapidoService;
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
@RequestMapping("/admin/links-rapidos")
@PreAuthorize("hasRole('ADMIN')")
public class LinkRapidoAdminController {

    private final LinkRapidoService linkRapidoService;

    public LinkRapidoAdminController(LinkRapidoService linkRapidoService) {
        this.linkRapidoService = linkRapidoService;
    }

    @GetMapping
    public String listar(@PageableDefault(size = 15) Pageable pageable, Model model) {
        model.addAttribute("pagina", linkRapidoService.listar(pageable));
        return "admin/links-rapidos/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("linkRapidoForm", new LinkRapidoForm());
        model.addAttribute("modoEdicao", false);
        return "admin/links-rapidos/form";
    }

    @PostMapping
    public String criar(
            @Valid @ModelAttribute("linkRapidoForm") LinkRapidoForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("modoEdicao", false);
            return "admin/links-rapidos/form";
        }

        try {
            linkRapidoService.criar(form.toEntity());
            redirectAttributes.addFlashAttribute("sucesso", "Link rapido cadastrado com sucesso.");
            return "redirect:/admin/links-rapidos";
        } catch (RegraNegocioException ex) {
            bindingResult.reject("linkRapido", ex.getMessage());
            model.addAttribute("modoEdicao", false);
            return "admin/links-rapidos/form";
        }
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        LinkRapido linkRapido = linkRapidoService.buscarPorId(id);
        model.addAttribute("linkRapido", linkRapido);
        model.addAttribute("linkRapidoForm", LinkRapidoForm.fromEntity(linkRapido));
        model.addAttribute("modoEdicao", true);
        return "admin/links-rapidos/form";
    }

    @PostMapping("/{id}")
    public String atualizar(
            @PathVariable Long id,
            @Valid @ModelAttribute("linkRapidoForm") LinkRapidoForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("linkRapido", linkRapidoService.buscarPorId(id));
            model.addAttribute("modoEdicao", true);
            return "admin/links-rapidos/form";
        }

        try {
            linkRapidoService.atualizar(id, form.toEntity());
            redirectAttributes.addFlashAttribute("sucesso", "Link rapido atualizado com sucesso.");
            return "redirect:/admin/links-rapidos";
        } catch (RegraNegocioException ex) {
            bindingResult.reject("linkRapido", ex.getMessage());
            model.addAttribute("linkRapido", linkRapidoService.buscarPorId(id));
            model.addAttribute("modoEdicao", true);
            return "admin/links-rapidos/form";
        }
    }

    @PostMapping("/{id}/status")
    public String alterarStatus(
            @PathVariable Long id,
            @RequestParam boolean ativo,
            RedirectAttributes redirectAttributes
    ) {
        linkRapidoService.alterarStatus(id, ativo);
        redirectAttributes.addFlashAttribute("sucesso", "Status do link rapido atualizado.");
        return "redirect:/admin/links-rapidos";
    }
}
