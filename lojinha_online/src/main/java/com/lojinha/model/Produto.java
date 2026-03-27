package com.lojinha.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Produto {
    private Long codigoBarras;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Integer estoque;
    private String categoria;

    public Produto(Long codigoBarras, String nome, BigDecimal preco, Integer estoque) {
        this.codigoBarras = codigoBarras;
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
    }

    public Produto(Long codigoBarras, String nome, String descricao, BigDecimal preco, Integer estoque, String categoria) {
        this(codigoBarras, nome, preco, estoque);
        this.descricao = descricao;
        this.categoria = categoria;
    }

    // Getters e Setters
    public Long getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(Long codigoBarras) { this.codigoBarras = codigoBarras; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public Integer getEstoque() { return estoque; }
    public void setEstoque(Integer estoque) { this.estoque = estoque; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public boolean temEstoque(int quantidade) {
        return estoque >= quantidade;
    }

    public void diminuirEstoque(int quantidade) {
        if (temEstoque(quantidade)) {
            this.estoque -= quantidade;
        } else {
            throw new IllegalStateException("Estoque insuficiente para o produto: " + nome);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return Objects.equals(codigoBarras, produto.codigoBarras);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigoBarras);
    }

    @Override
    public String toString() {
        return String.format("Produto{codigo=%d, nome='%s', preco=R$%.2f, estoque=%d}", 
                codigoBarras, nome, preco, estoque);
    }
}