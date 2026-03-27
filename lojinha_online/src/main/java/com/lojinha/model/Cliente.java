package com.lojinha.model;

import java.time.LocalDate;
import java.util.Objects;

public class Cliente {
    private String cpf;
    private String nome;
    private String email;
    private String telefone;
    private LocalDate dataCadastro;

    // Construtor completo
    public Cliente(String cpf, String nome, String email, String telefone) {
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.dataCadastro = LocalDate.now();
    }

    // Construtor simplificado
    public Cliente(String cpf, String nome, String email) {
        this(cpf, nome, email, null);
    }

    // Getters e Setters
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public LocalDate getDataCadastro() { return dataCadastro; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(cpf, cliente.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpf);
    }

    @Override
    public String toString() {
        return String.format("Cliente{cpf='%s', nome='%s', email='%s'}", cpf, nome, email);
    }
}