package br.com.empresa.intranet.service;

import br.com.empresa.intranet.entity.LinkRapido;
import br.com.empresa.intranet.entity.Recado;
import java.util.List;

public interface DashboardService {

    List<Recado> listarRecadosVisiveis();

    List<LinkRapido> listarLinksRapidosAtivos();
}
