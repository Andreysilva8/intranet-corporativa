package br.com.empresa.intranet.controller;

import br.com.empresa.intranet.service.ColaboradorService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ColaboradorController {

    private final ColaboradorService colaboradorService;

    public ColaboradorController(ColaboradorService colaboradorService) {
        this.colaboradorService = colaboradorService;
    }

    @GetMapping("/colaboradores")
    public String listar(
            @RequestParam(required = false) String termo,
            @PageableDefault(size = 12) Pageable pageable,
            Model model
    ) {
        model.addAttribute("pagina", colaboradorService.pesquisar(termo, pageable));
        model.addAttribute("termo", termo);
        return "colaboradores/lista";
    }

    @GetMapping("/colaboradores/{id}")
    public String perfil(@PathVariable Long id, Model model) {
        model.addAttribute("colaborador", colaboradorService.buscarPorId(id));
        return "colaboradores/perfil";
    }
}
