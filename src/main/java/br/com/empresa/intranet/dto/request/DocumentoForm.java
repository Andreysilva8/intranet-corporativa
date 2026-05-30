package br.com.empresa.intranet.dto.request;

import br.com.empresa.intranet.entity.DocumentoCorporativo;
import br.com.empresa.intranet.entity.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.EnumSet;
import java.util.Set;
import org.springframework.web.multipart.MultipartFile;

public class DocumentoForm {

    @NotBlank
    @Size(max = 160)
    private String titulo;

    private String descricao;

    @NotBlank
    @Size(max = 80)
    private String categoria;

    private MultipartFile arquivo;

    private Set<Role> rolesPermitidas = EnumSet.allOf(Role.class);

    public static DocumentoForm fromEntity(DocumentoCorporativo documento) {
        DocumentoForm form = new DocumentoForm();
        form.setTitulo(documento.getTitulo());
        form.setDescricao(documento.getDescricao());
        form.setCategoria(documento.getCategoria());
        form.setRolesPermitidas(documento.getRolesPermitidas());
        return form;
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

    public MultipartFile getArquivo() {
        return arquivo;
    }

    public void setArquivo(MultipartFile arquivo) {
        this.arquivo = arquivo;
    }

    public Set<Role> getRolesPermitidas() {
        return rolesPermitidas;
    }

    public void setRolesPermitidas(Set<Role> rolesPermitidas) {
        this.rolesPermitidas = rolesPermitidas;
    }
}
