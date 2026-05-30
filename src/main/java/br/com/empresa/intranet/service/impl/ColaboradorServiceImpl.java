package br.com.empresa.intranet.service.impl;

import br.com.empresa.intranet.entity.Colaborador;
import br.com.empresa.intranet.exception.RecursoNaoEncontradoException;
import br.com.empresa.intranet.exception.RegraNegocioException;
import br.com.empresa.intranet.repository.ColaboradorRepository;
import br.com.empresa.intranet.service.ColaboradorService;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional(readOnly = true)
public class ColaboradorServiceImpl implements ColaboradorService {

    private final ColaboradorRepository colaboradorRepository;

    public ColaboradorServiceImpl(ColaboradorRepository colaboradorRepository) {
        this.colaboradorRepository = colaboradorRepository;
    }

    @Override
    public Page<Colaborador> listar(Pageable pageable) {
        return colaboradorRepository.findAll(pageable);
    }

    @Override
    public Page<Colaborador> pesquisar(String termo, Pageable pageable) {
        return colaboradorRepository.pesquisarAtivos(normalizarTermoPesquisa(termo), pageable);
    }

    @Override
    public Colaborador buscarPorId(Long id) {
        return colaboradorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Colaborador nao encontrado"));
    }

    @Override
    @Transactional
    public Colaborador criar(Colaborador colaborador) {
        normalizarColaborador(colaborador);
        validarEmailUnico(null, colaborador.getEmailCorporativo());
        return colaboradorRepository.save(colaborador);
    }

    @Override
    @Transactional
    public Colaborador atualizar(Long id, Colaborador dadosAtualizados) {
        Colaborador colaborador = buscarPorId(id);
        normalizarColaborador(dadosAtualizados);
        validarEmailUnico(id, dadosAtualizados.getEmailCorporativo());

        colaborador.setNomeCompleto(dadosAtualizados.getNomeCompleto());
        colaborador.setEmailCorporativo(dadosAtualizados.getEmailCorporativo());
        colaborador.setTelefone(dadosAtualizados.getTelefone());
        colaborador.setRamal(dadosAtualizados.getRamal());
        colaborador.setDepartamento(dadosAtualizados.getDepartamento());
        colaborador.setCargo(dadosAtualizados.getCargo());
        colaborador.setLocalidade(dadosAtualizados.getLocalidade());
        colaborador.setFotoUrl(dadosAtualizados.getFotoUrl());
        colaborador.setBio(dadosAtualizados.getBio());
        colaborador.setAtivo(dadosAtualizados.isAtivo());

        if (dadosAtualizados.getUsuario() != null) {
            colaborador.setUsuario(dadosAtualizados.getUsuario());
        }

        return colaboradorRepository.save(colaborador);
    }

    @Override
    @Transactional
    public void alterarStatus(Long id, boolean ativo) {
        Colaborador colaborador = buscarPorId(id);
        colaborador.setAtivo(ativo);
        colaboradorRepository.save(colaborador);
    }

    private void validarEmailUnico(Long idAtual, String emailCorporativo) {
        colaboradorRepository.findByEmailCorporativoIgnoreCase(emailCorporativo)
                .filter(colaborador -> !Objects.equals(colaborador.getId(), idAtual))
                .ifPresent(colaborador -> {
                    throw new RegraNegocioException("E-mail corporativo ja cadastrado");
                });
    }

    private void normalizarColaborador(Colaborador colaborador) {
        colaborador.setNomeCompleto(normalizarTextoObrigatorio(colaborador.getNomeCompleto(), "Nome e obrigatorio"));
        colaborador.setEmailCorporativo(normalizarEmail(colaborador.getEmailCorporativo()));
        colaborador.setTelefone(normalizarTextoOpcional(colaborador.getTelefone()));
        colaborador.setRamal(normalizarTextoOpcional(colaborador.getRamal()));
        colaborador.setDepartamento(normalizarTextoObrigatorio(colaborador.getDepartamento(), "Departamento e obrigatorio"));
        colaborador.setCargo(normalizarTextoObrigatorio(colaborador.getCargo(), "Cargo e obrigatorio"));
        colaborador.setLocalidade(normalizarTextoOpcional(colaborador.getLocalidade()));
        colaborador.setFotoUrl(normalizarTextoOpcional(colaborador.getFotoUrl()));
        colaborador.setBio(normalizarTextoOpcional(colaborador.getBio()));
    }

    private String normalizarEmail(String email) {
        return normalizarTextoObrigatorio(email, "E-mail corporativo e obrigatorio").toLowerCase();
    }

    private String normalizarTermoPesquisa(String termo) {
        return StringUtils.hasText(termo) ? termo.trim() : null;
    }

    private String normalizarTextoObrigatorio(String valor, String mensagemErro) {
        String texto = normalizarTextoOpcional(valor);
        if (!StringUtils.hasText(texto)) {
            throw new RegraNegocioException(mensagemErro);
        }
        return texto;
    }

    private String normalizarTextoOpcional(String valor) {
        return valor == null ? null : valor.trim();
    }
}
