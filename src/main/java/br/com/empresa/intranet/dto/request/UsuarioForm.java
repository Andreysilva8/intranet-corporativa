package br.com.empresa.intranet.dto.request;

import br.com.empresa.intranet.entity.Usuario;
import br.com.empresa.intranet.entity.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.EnumSet;
import java.util.Set;

public class UsuarioForm {

    @NotBlank
    @Size(max = 120)
    private String nome;

    @NotBlank
    @Size(max = 60)
    private String username;

    @Email
    @NotBlank
    @Size(max = 120)
    private String email;

    @Size(max = 255)
    private String senha;

    private boolean ativo = true;

    private Set<Role> roles = EnumSet.of(Role.COLABORADOR);

    public Usuario toEntity() {
        Usuario usuario = new Usuario(nome, username, email, senha);
        usuario.setAtivo(ativo);
        usuario.setRoles(roles);
        return usuario;
    }

    public static UsuarioForm fromEntity(Usuario usuario) {
        UsuarioForm form = new UsuarioForm();
        form.setNome(usuario.getNome());
        form.setUsername(usuario.getUsername());
        form.setEmail(usuario.getEmail());
        form.setAtivo(usuario.isAtivo());
        form.setRoles(usuario.getRoles());
        return form;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
