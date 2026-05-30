package br.com.empresa.intranet.service.impl;

import br.com.empresa.intranet.entity.LinkRapido;
import br.com.empresa.intranet.entity.Recado;
import br.com.empresa.intranet.service.DashboardService;
import br.com.empresa.intranet.service.LinkRapidoService;
import br.com.empresa.intranet.service.RecadoService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final RecadoService recadoService;
    private final LinkRapidoService linkRapidoService;

    public DashboardServiceImpl(RecadoService recadoService, LinkRapidoService linkRapidoService) {
        this.recadoService = recadoService;
        this.linkRapidoService = linkRapidoService;
    }

    @Override
    public List<Recado> listarRecadosVisiveis() {
        return recadoService.listarVisiveis();
    }

    @Override
    public List<LinkRapido> listarLinksRapidosAtivos() {
        return linkRapidoService.listarAtivos();
    }
}
