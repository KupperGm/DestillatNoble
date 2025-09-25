package com.ifsp.DestillatNoble.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @Pattern(
        regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}",
        message = "CPF deve estar no formato XXX.XXX.XXX-XX"
    )
    private String cpf; // Usando CPF como chave primária

    @NotBlank(message = "Nome não pode estar vazio")    
    @Column(name="nome", nullable = false, length = 14)
    private String nome;

    @NotBlank(message = "Email não pode estar vazio")
    @Email(message = "Email deve ser válido")
    @Column(name="email", nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Senha não pode estar vazia")
    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
    @Column(name="senha", nullable = false, unique = false)
    private String senha;

    @NotNull(message = "Data de nascimento não pode estar vazia")
    @Column(name="nascimento", nullable = false)
    private LocalDate nascimento;

    public Usuario() {
    }

    public Usuario(String cpf, String nome, String email, String senha, LocalDate nascimento) {
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.nascimento = nascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public LocalDate getNascimento() {
        return nascimento;
    }

    public void setNascimento(LocalDate nascimento) {
        this.nascimento = nascimento;
    }

    

    
}
