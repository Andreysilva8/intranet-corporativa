package br.com.empresa.intranet.controller;

import br.com.empresa.intranet.dto.request.ColaboradorForm;
import br.com.empresa.intranet.entity.Colaborador;
import br.com.empresa.intranet.exception.RegraNegocioException;
import br.com.empresa.intranet.service.ColaboradorService;
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
@RequestMapping("/rh/colaboradores")
@PreAuthorize("hasAnyRole('ADMIN', 'RH')")
public class ColaboradorAdminController {

    private final ColaboradorService colaboradorService;

    public ColaboradorAdminController(ColaboradorService colaboradorService) {
        this.colaboradorService = colaboradorService;
    }

    @GetMapping
    public String listar(@PageableDefault(size = 15) Pageable pageable, Model model) {
        model.addAttribute("pagina", colaboradorService.listar(pageable));
        return "rh/colaboradores/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("colaboradorForm", new ColaboradorForm());
        model.addAttribute("modoEdicao", false);
        return "rh/colaboradores/form";
    }

    @PostMapping
    public String criar(
            @Valid @ModelAttribute("colaboradorForm") ColaboradorForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("modoEdicao", false);
            return "rh/colaboradores/form";
        }

        try {
            colaboradorService.criar(form.toEntity());
            redirectAttributes.addFlashAttribute("sucesso", "Colaborador cadastrado com sucesso.");
            return "redirect:/rh/colaboradores";
        } catch (RegraNegocioException ex) {
            bindingResult.reject("colaborador", ex.getMessage());
            model.addAttribute("modoEdicao", false);
            return "rh/colaboradores/form";
        }
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Colaborador colaborador = colaboradorService.buscarPorId(id);
        model.addAttribute("colaborador", colaborador);
        model.addAttribute("colaboradorForm", ColaboradorForm.fromEntity(colaborador));
        model.addAttribute("modoEdicao", true);
        return "rh/colaboradores/form";
    }

    @PostMapping("/{id}")
    public String atualizar(
            @PathVariable Long id,
            @Valid @ModelAttribute("colaboradorForm") ColaboradorForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("colaborador", colaboradorService.buscarPorId(id));
            model.addAttribute("modoEdicao", true);
            return "rh/colaboradores/form";
        }

        try {
            colaboradorService.atualizar(id, form.toEntity());
            redirectAttributes.addFlashAttribute("sucesso", "Colaborador atualizado com sucesso.");
            return "redirect:/rh/colaboradores";
        } catch (RegraNegocioException ex) {
            bindingResult.reject("colaborador", ex.getMessage());
            model.addAttribute("colaborador", colaboradorService.buscarPorId(id));
            model.addAttribute("modoEdicao", true);
            return "rh/colaboradores/form";
        }
    }

    @PostMapping("/{id}/status")
    public String alterarStatus(
            @PathVariable Long id,
            @RequestParam boolean ativo,
            RedirectAttributes redirectAttributes
    ) {
        colaboradorService.alterarStatus(id, ativo);
        redirectAttributes.addFlashAttribute("sucesso", "Status do colaborador atualizado.");
        return "redirect:/rh/colaboradores";
    }
}
