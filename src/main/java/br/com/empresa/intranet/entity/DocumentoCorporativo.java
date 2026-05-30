package br.com.empresa.intranet.entity;

import br.com.empresa.intranet.entity.enums.Role;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "documentos_corporativos",
        indexes = {
                @Index(name = "idx_documentos_titulo", columnList = "titulo"),
                @Index(name = "idx_documentos_categoria", columnList = "categoria"),
                @Index(name = "idx_documentos_ativo", columnList = "ativo"),
                @Index(name = "idx_documentos_criado_em", columnList = "criado_em")
        }
)
public class DocumentoCorporativo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 160)
    @NotBlank
    @Size(max = 160)
    private String titulo;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false, length = 80)
    @NotBlank
    @Size(max = 80)
    private String categoria;

    @Column(name = "nome_arquivo_original", nullable = false, length = 255)
    @NotBlank
    @Size(max = 255)
    private String nomeArquivoOriginal;

    @Column(name = "nome_arquivo_armazenado", nullable = false, unique = true, length = 255)
    @NotBlank
    @Size(max = 255)
    private String nomeArquivoArmazenado;

    @Column(name = "content_type", nullable = false, length = 120)
    @NotBlank
    @Size(max = 120)
    private String contentType;

    @Column(name = "tamanho_bytes", nullable = false)
    @NotNull
    @PositiveOrZero
    private Long tamanhoBytes;

    @Column(nullable = false)
    private boolean ativo = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enviado_por_id", nullable = false)
    private Usuario enviadoPor;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "documento_roles_permitidas",
            joinColumns = @JoinColumn(name = "documento_id"),
            uniqueConstraints = @UniqueConstraint(
                    name = "uk_documento_roles_documento_role",
                    columnNames = {"documento_id", "role"}
            )
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 30)
    private Set<Role> rolesPermitidas = new HashSet<>();

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    protected DocumentoCorporativo() {
    }

    public DocumentoCorporativo(
            String titulo,
            String categoria,
            String nomeArquivoOriginal,
            String nomeArquivoArmazenado,
            String contentType,
            Long tamanhoBytes,
            Usuario enviadoPor
    ) {
        this.titulo = titulo;
        this.categoria = categoria;
        this.nomeArquivoOriginal = nomeArquivoOriginal;
        this.nomeArquivoArmazenado = nomeArquivoArmazenado;
        this.contentType = contentType;
        this.tamanhoBytes = tamanhoBytes;
        this.enviadoPor = enviadoPor;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getNomeArquivoOriginal() {
        return nomeArquivoOriginal;
    }

    public void setNomeArquivoOriginal(String nomeArquivoOriginal) {
        this.nomeArquivoOriginal = nomeArquivoOriginal;
    }

    public String getNomeArquivoArmazenado() {
        return nomeArquivoArmazenado;
    }

    public void setNomeArquivoArmazenado(String nomeArquivoArmazenado) {
        this.nomeArquivoArmazenado = nomeArquivoArmazenado;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getTamanhoBytes() {
        return tamanhoBytes;
    }

    public void setTamanhoBytes(Long tamanhoBytes) {
        this.tamanhoBytes = tamanhoBytes;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Usuario getEnviadoPor() {
        return enviadoPor;
    }

    public void setEnviadoPor(Usuario enviadoPor) {
        this.enviadoPor = enviadoPor;
    }

    public Set<Role> getRolesPermitidas() {
        return rolesPermitidas;
    }

    public void setRolesPermitidas(Set<Role> rolesPermitidas) {
        this.rolesPermitidas = rolesPermitidas;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }
}
