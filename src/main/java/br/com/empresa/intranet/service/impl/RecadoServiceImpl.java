package br.com.empresa.intranet.service.impl;

import br.com.empresa.intranet.entity.Recado;
import br.com.empresa.intranet.exception.RecursoNaoEncontradoException;
import br.com.empresa.intranet.exception.RegraNegocioException;
import br.com.empresa.intranet.repository.RecadoRepository;
import br.com.empresa.intranet.service.RecadoService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional(readOnly = true)
public class RecadoServiceImpl implements RecadoService {

    private final RecadoRepository recadoRepository;

    public RecadoServiceImpl(RecadoRepository recadoRepository) {
        this.recadoRepository = recadoRepository;
    }

    @Override
    public Page<Recado> listar(Pageable pageable) {
        return recadoRepository.findAll(pageable);
    }

    @Override
    public List<Recado> listarVisiveis() {
        return recadoRepository.buscarRecadosVisiveis(LocalDateTime.now());
    }

    @Override
    public Recado buscarPorId(Long id) {
        return recadoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Recado nao encontrado"));
    }

    @Override
    @Transactional
    public Recado criar(Recado recado) {
        validarRecado(recado);
        normalizarRecado(recado);
        return recadoRepository.save(recado);
    }

    @Override
    @Transactional
    public Recado atualizar(Long id, Recado dadosAtualizados) {
        Recado recado = buscarPorId(id);
        validarRecado(dadosAtualizados);
        normalizarRecado(dadosAtualizados);

        recado.setTitulo(dadosAtualizados.getTitulo());
        recado.setMensagem(dadosAtualizados.getMensagem());
        recado.setDataPublicacao(dadosAtualizados.getDataPublicacao());
        recado.setDataExpiracao(dadosAtualizados.getDataExpiracao());
        recado.setDestaque(dadosAtualizados.isDestaque());
        recado.setAtivo(dadosAtualizados.isAtivo());

        if (dadosAtualizados.getAutor() != null) {
            recado.setAutor(dadosAtualizados.getAutor());
        }

        return recadoRepository.save(recado);
    }

    @Override
    @Transactional
    public void alterarStatus(Long id, boolean ativo) {
        Recado recado = buscarPorId(id);
        recado.setAtivo(ativo);
        recadoRepository.save(recado);
    }

    private void validarRecado(Recado recado) {
        if (!StringUtils.hasText(recado.getTitulo())) {
            throw new RegraNegocioException("Titulo do recado e obrigatorio");
        }

        if (!StringUtils.hasText(recado.getMensagem())) {
            throw new RegraNegocioException("Mensagem do recado e obrigatoria");
        }

        if (recado.getAutor() == null) {
            throw new RegraNegocioException("Autor do recado e obrigatorio");
        }

        if (recado.getDataPublicacao() != null
                && recado.getDataExpiracao() != null
                && recado.getDataExpiracao().isBefore(recado.getDataPublicacao())) {
            throw new RegraNegocioException("Data de expiracao nao pode ser anterior a publicacao");
        }
    }

    private void normalizarRecado(Recado recado) {
        recado.setTitulo(recado.getTitulo().trim());
        recado.setMensagem(recado.getMensagem().trim());
    }
}
