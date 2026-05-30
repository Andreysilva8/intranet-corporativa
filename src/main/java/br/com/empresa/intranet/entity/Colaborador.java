package br.com.empresa.intranet.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "colaboradores",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_colaboradores_email_corporativo", columnNames = "email_corporativo"),
                @UniqueConstraint(name = "uk_colaboradores_usuario", columnNames = "usuario_id")
        },
        indexes = {
                @Index(name = "idx_colaboradores_nome", columnList = "nome_completo"),
                @Index(name = "idx_colaboradores_departamento", columnList = "departamento"),
                @Index(name = "idx_colaboradores_cargo", columnList = "cargo"),
                @Index(name = "idx_colaboradores_ativo", columnList = "ativo")
        }
)
public class Colaborador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_completo", nullable = false, length = 140)
    @NotBlank
    @Size(max = 140)
    private String nomeCompleto;

    @Column(name = "email_corporativo", nullable = false, length = 140)
    @Email
    @NotBlank
    @Size(max = 140)
    private String emailCorporativo;

    @Column(length = 30)
    @Size(max = 30)
    private String telefone;

    @Column(length = 20)
    @Size(max = 20)
    private String ramal;

    @Column(nullable = false, length = 120)
    @NotBlank
    @Size(max = 120)
    private String departamento;

    @Column(nullable = false, length = 120)
    @NotBlank
    @Size(max = 120)
    private String cargo;

    @Column(length = 120)
    @Size(max = 120)
    private String localidade;

    @Column(name = "foto_url", length = 255)
    @Size(max = 255)
    private String fotoUrl;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(nullable = false)
    private boolean ativo = true;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    protected Colaborador() {
    }

    public Colaborador(String nomeCompleto, String emailCorporativo, String departamento, String cargo) {
        this.nomeCompleto = nomeCompleto;
        this.emailCorporativo = emailCorporativo;
        this.departamento = departamento;
        this.cargo = cargo;
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

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getEmailCorporativo() {
        return emailCorporativo;
    }

    public void setEmailCorporativo(String emailCorporativo) {
        this.emailCorporativo = emailCorporativo;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getRamal() {
        return ramal;
    }

    public void setRamal(String ramal) {
        this.ramal = ramal;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }
}
