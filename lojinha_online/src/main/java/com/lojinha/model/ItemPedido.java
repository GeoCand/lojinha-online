package com.lojinha.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ItemPedido {
    private Integer id;
    private Produto produto;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal subtotal;

    public ItemPedido(Produto produto, Integer quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = produto.getPreco();
        this.subtotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade))
                .setScale(2, RoundingMode.HALF_EVEN);
    }

    public ItemPedido(Integer id, Produto produto, Integer quantidade, BigDecimal precoUnitario) {
        this.id = id;
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.subtotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade))
                .setScale(2, RoundingMode.HALF_EVEN);
    }

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { 
        this.quantidade = quantidade;
        recalcularSubtotal();
    }

    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(BigDecimal precoUnitario) { 
        this.precoUnitario = precoUnitario;
        recalcularSubtotal();
    }

    public BigDecimal getSubtotal() { return subtotal; }

    private void recalcularSubtotal() {
        this.subtotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade))
                .setScale(2, RoundingMode.HALF_EVEN);
    }

    @Override
    public String toString() {
        return String.format("  Item: %s | Qtd: %d | Unit: R$%.2f | Subtotal: R$%.2f",
                produto.getNome(), quantidade, precoUnitario, subtotal);
    }
}