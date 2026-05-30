package br.com.empresa.intranet.service.impl;

import br.com.empresa.intranet.entity.LinkRapido;
import br.com.empresa.intranet.exception.RecursoNaoEncontradoException;
import br.com.empresa.intranet.exception.RegraNegocioException;
import br.com.empresa.intranet.repository.LinkRapidoRepository;
import br.com.empresa.intranet.service.LinkRapidoService;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional(readOnly = true)
public class LinkRapidoServiceImpl implements LinkRapidoService {

    private final LinkRapidoRepository linkRapidoRepository;

    public LinkRapidoServiceImpl(LinkRapidoRepository linkRapidoRepository) {
        this.linkRapidoRepository = linkRapidoRepository;
    }

    @Override
    public Page<LinkRapido> listar(Pageable pageable) {
        return linkRapidoRepository.findAll(pageable);
    }

    @Override
    public List<LinkRapido> listarAtivos() {
        return linkRapidoRepository.findByAtivoTrueOrderByOrdemExibicaoAscTituloAsc();
    }

    @Override
    public LinkRapido buscarPorId(Long id) {
        return linkRapidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Link rapido nao encontrado"));
    }

    @Override
    @Transactional
    public LinkRapido criar(LinkRapido linkRapido) {
        validarLink(linkRapido);
        normalizarLink(linkRapido);
        return linkRapidoRepository.save(linkRapido);
    }

    @Override
    @Transactional
    public LinkRapido atualizar(Long id, LinkRapido dadosAtualizados) {
        LinkRapido linkRapido = buscarPorId(id);
        validarLink(dadosAtualizados);
        normalizarLink(dadosAtualizados);

        linkRapido.setTitulo(dadosAtualizados.getTitulo());
        linkRapido.setUrl(dadosAtualizados.getUrl());
        linkRapido.setIcone(dadosAtualizados.getIcone());
        linkRapido.setCategoria(dadosAtualizados.getCategoria());
        linkRapido.setOrdemExibicao(dadosAtualizados.getOrdemExibicao());
        linkRapido.setAtivo(dadosAtualizados.isAtivo());

        return linkRapidoRepository.save(linkRapido);
    }

    @Override
    @Transactional
    public void alterarStatus(Long id, boolean ativo) {
        LinkRapido linkRapido = buscarPorId(id);
        linkRapido.setAtivo(ativo);
        linkRapidoRepository.save(linkRapido);
    }

    private void validarLink(LinkRapido linkRapido) {
        if (!StringUtils.hasText(linkRapido.getTitulo())) {
            throw new RegraNegocioException("Titulo do link e obrigatorio");
        }

        if (!StringUtils.hasText(linkRapido.getUrl())) {
            throw new RegraNegocioException("URL do link e obrigatoria");
        }
    }

    private void normalizarLink(LinkRapido linkRapido) {
        linkRapido.setTitulo(linkRapido.getTitulo().trim());
        linkRapido.setUrl(linkRapido.getUrl().trim());
        linkRapido.setIcone(normalizarTextoOpcional(linkRapido.getIcone()));
        linkRapido.setCategoria(normalizarTextoOpcional(linkRapido.getCategoria()));

        if (linkRapido.getOrdemExibicao() == null) {
            linkRapido.setOrdemExibicao(0);
        }
    }

    private String normalizarTextoOpcional(String valor) {
        return valor == null ? null : valor.trim();
    }
}
