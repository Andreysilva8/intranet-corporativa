package br.com.empresa.intranet.dto.request;

import br.com.empresa.intranet.entity.Colaborador;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ColaboradorForm {

    @NotBlank
    @Size(max = 140)
    private String nomeCompleto;

    @Email
    @NotBlank
    @Size(max = 140)
    private String emailCorporativo;

    @Size(max = 30)
    private String telefone;

    @Size(max = 20)
    private String ramal;

    @NotBlank
    @Size(max = 120)
    private String departamento;

    @NotBlank
    @Size(max = 120)
    private String cargo;

    @Size(max = 120)
    private String localidade;

    @Size(max = 255)
    private String fotoUrl;

    private String bio;

    private boolean ativo = true;

    public Colaborador toEntity() {
        Colaborador colaborador = new Colaborador(nomeCompleto, emailCorporativo, departamento, cargo);
        colaborador.setTelefone(telefone);
        colaborador.setRamal(ramal);
        colaborador.setLocalidade(localidade);
        colaborador.setFotoUrl(fotoUrl);
        colaborador.setBio(bio);
        colaborador.setAtivo(ativo);
        return colaborador;
    }

    public static ColaboradorForm fromEntity(Colaborador colaborador) {
        ColaboradorForm form = new ColaboradorForm();
        form.setNomeCompleto(colaborador.getNomeCompleto());
        form.setEmailCorporativo(colaborador.getEmailCorporativo());
        form.setTelefone(colaborador.getTelefone());
        form.setRamal(colaborador.getRamal());
        form.setDepartamento(colaborador.getDepartamento());
        form.setCargo(colaborador.getCargo());
        form.setLocalidade(colaborador.getLocalidade());
        form.setFotoUrl(colaborador.getFotoUrl());
        form.setBio(colaborador.getBio());
        form.setAtivo(colaborador.isAtivo());
        return form;
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
}
