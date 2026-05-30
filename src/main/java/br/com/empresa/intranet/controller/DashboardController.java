package br.com.empresa.intranet.controller;

import br.com.empresa.intranet.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public String index(Model model) {
        model.addAttribute("recados", dashboardService.listarRecadosVisiveis());
        model.addAttribute("linksRapidos", dashboardService.listarLinksRapidosAtivos());
        return "dashboard/index";
    }
}
