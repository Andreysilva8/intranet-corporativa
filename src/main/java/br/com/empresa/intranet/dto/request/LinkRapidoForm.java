package br.com.empresa.intranet.dto.request;

import br.com.empresa.intranet.entity.LinkRapido;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LinkRapidoForm {

    @NotBlank
    @Size(max = 120)
    private String titulo;

    @NotBlank
    @Size(max = 500)
    private String url;

    @Size(max = 80)
    private String icone;

    @Size(max = 80)
    private String categoria;

    private Integer ordemExibicao = 0;

    private boolean ativo = true;

    public LinkRapido toEntity() {
        LinkRapido linkRapido = new LinkRapido(titulo, url);
        linkRapido.setIcone(icone);
        linkRapido.setCategoria(categoria);
        linkRapido.setOrdemExibicao(ordemExibicao);
        linkRapido.setAtivo(ativo);
        return linkRapido;
    }

    public static LinkRapidoForm fromEntity(LinkRapido linkRapido) {
        LinkRapidoForm form = new LinkRapidoForm();
        form.setTitulo(linkRapido.getTitulo());
        form.setUrl(linkRapido.getUrl());
        form.setIcone(linkRapido.getIcone());
        form.setCategoria(linkRapido.getCategoria());
        form.setOrdemExibicao(linkRapido.getOrdemExibicao());
        form.setAtivo(linkRapido.isAtivo());
        return form;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Integer getOrdemExibicao() {
        return ordemExibicao;
    }

    public void setOrdemExibicao(Integer ordemExibicao) {
        this.ordemExibicao = ordemExibicao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
