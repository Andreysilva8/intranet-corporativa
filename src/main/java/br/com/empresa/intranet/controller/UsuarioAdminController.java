package br.com.empresa.intranet.controller;

import br.com.empresa.intranet.dto.request.UsuarioForm;
import br.com.empresa.intranet.entity.Usuario;
import br.com.empresa.intranet.entity.enums.Role;
import br.com.empresa.intranet.exception.RegraNegocioException;
import br.com.empresa.intranet.service.UsuarioService;
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
@RequestMapping("/admin/usuarios")
@PreAuthorize("hasRole('ADMIN')")
public class UsuarioAdminController {

    private final UsuarioService usuarioService;

    public UsuarioAdminController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @ModelAttribute("roles")
    public Role[] roles() {
        return Role.values();
    }

    @GetMapping
    public String listar(@PageableDefault(size = 15) Pageable pageable, Model model) {
        model.addAttribute("pagina", usuarioService.listar(pageable));
        return "admin/usuarios/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("usuarioForm", new UsuarioForm());
        model.addAttribute("modoEdicao", false);
        return "admin/usuarios/form";
    }

    @PostMapping
    public String criar(
            @Valid @ModelAttribute("usuarioForm") UsuarioForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("modoEdicao", false);
            return "admin/usuarios/form";
        }

        try {
            usuarioService.criar(form.toEntity(), form.getSenha(), form.getRoles());
            redirectAttributes.addFlashAttribute("sucesso", "Usuario cadastrado com sucesso.");
            return "redirect:/admin/usuarios";
        } catch (RegraNegocioException ex) {
            bindingResult.reject("usuario", ex.getMessage());
            model.addAttribute("modoEdicao", false);
            return "admin/usuarios/form";
        }
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id);
        model.addAttribute("usuario", usuario);
        model.addAttribute("usuarioForm", UsuarioForm.fromEntity(usuario));
        model.addAttribute("modoEdicao", true);
        return "admin/usuarios/form";
    }

    @PostMapping("/{id}")
    public String atualizar(
            @PathVariable Long id,
            @Valid @ModelAttribute("usuarioForm") UsuarioForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("usuario", usuarioService.buscarPorId(id));
            model.addAttribute("modoEdicao", true);
            return "admin/usuarios/form";
        }

        try {
            usuarioService.atualizar(id, form.toEntity(), form.getSenha(), form.getRoles());
            redirectAttributes.addFlashAttribute("sucesso", "Usuario atualizado com sucesso.");
            return "redirect:/admin/usuarios";
        } catch (RegraNegocioException ex) {
            bindingResult.reject("usuario", ex.getMessage());
            model.addAttribute("usuario", usuarioService.buscarPorId(id));
            model.addAttribute("modoEdicao", true);
            return "admin/usuarios/form";
        }
    }

    @PostMapping("/{id}/status")
    public String alterarStatus(
            @PathVariable Long id,
            @RequestParam boolean ativo,
            RedirectAttributes redirectAttributes
    ) {
        usuarioService.alterarStatus(id, ativo);
        redirectAttributes.addFlashAttribute("sucesso", "Status do usuario atualizado.");
        return "redirect:/admin/usuarios";
    }
}
