package br.com.empresa.intranet.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "links_rapidos",
        indexes = {
                @Index(name = "idx_links_rapidos_ativo", columnList = "ativo"),
                @Index(name = "idx_links_rapidos_categoria", columnList = "categoria"),
                @Index(name = "idx_links_rapidos_ordem", columnList = "ordem_exibicao")
        }
)
public class LinkRapido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    @NotBlank
    @Size(max = 120)
    private String titulo;

    @Column(nullable = false, length = 500)
    @NotBlank
    @Size(max = 500)
    private String url;

    @Column(length = 80)
    @Size(max = 80)
    private String icone;

    @Column(length = 80)
    @Size(max = 80)
    private String categoria;

    @Column(name = "ordem_exibicao", nullable = false)
    private Integer ordemExibicao = 0;

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    protected LinkRapido() {
    }

    public LinkRapido(String titulo, String url) {
        this.titulo = titulo;
        this.url = url;
    }

    @PrePersist
    void prePersist() {
        criadoEm = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        atualizadoEm = LocalDateTime.now();
    }

    public Long getId() {
        return id;
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

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }
}
