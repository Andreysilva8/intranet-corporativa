package br.com.empresa.intranet.dto.request;

import br.com.empresa.intranet.entity.Recado;
import br.com.empresa.intranet.entity.Usuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

public class RecadoForm {

    @NotBlank
    @Size(max = 160)
    private String titulo;

    @NotBlank
    private String mensagem;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dataPublicacao;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dataExpiracao;

    private boolean destaque;

    private boolean ativo = true;

    public Recado toEntity(Usuario autor) {
        Recado recado = new Recado(titulo, mensagem, autor);
        recado.setDataPublicacao(dataPublicacao);
        recado.setDataExpiracao(dataExpiracao);
        recado.setDestaque(destaque);
        recado.setAtivo(ativo);
        return recado;
    }

    public static RecadoForm fromEntity(Recado recado) {
        RecadoForm form = new RecadoForm();
        form.setTitulo(recado.getTitulo());
        form.setMensagem(recado.getMensagem());
        form.setDataPublicacao(recado.getDataPublicacao());
        form.setDataExpiracao(recado.getDataExpiracao());
        form.setDestaque(recado.isDestaque());
        form.setAtivo(recado.isAtivo());
        return form;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDateTime getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(LocalDateTime dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public LocalDateTime getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(LocalDateTime dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    public boolean isDestaque() {
        return destaque;
    }

    public void setDestaque(boolean destaque) {
        this.destaque = destaque;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
