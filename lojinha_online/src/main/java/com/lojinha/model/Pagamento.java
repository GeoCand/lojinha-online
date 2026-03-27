// com/lojinha/model/Pagamento.java
package com.lojinha.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Pagamento {
    private Integer id;
    private Pedido pedido;
    private Cliente cliente;
    private TipoPagamento tipoPagamento;
    private BigDecimal valor;
    private StatusPagamento status;
    private LocalDateTime dataPagamento;
    private String transacaoId;

    public enum TipoPagamento {
        CARTAO_CREDITO("Cartão de Crédito"),
        CARTAO_DEBITO("Cartão de Débito"),
        PIX("PIX"),
        BOLETO("Boleto");

        private String descricao;

        TipoPagamento(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    public enum StatusPagamento {
        AGUARDANDO("Aguardando"),
        APROVADO("Aprovado"),
        RECUSADO("Recusado"),
        CANCELADO("Cancelado");

        private String descricao;

        StatusPagamento(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    public Pagamento(Pedido pedido, Cliente cliente, TipoPagamento tipoPagamento) {
        this.pedido = pedido;
        this.cliente = cliente;
        this.tipoPagamento = tipoPagamento;
        this.valor = pedido.getValorTotal();
        this.status = StatusPagamento.AGUARDANDO;
    }

    public Pagamento(Integer id, Pedido pedido, Cliente cliente, TipoPagamento tipoPagamento, 
                     BigDecimal valor, StatusPagamento status, LocalDateTime dataPagamento, String transacaoId) {
        this.id = id;
        this.pedido = pedido;
        this.cliente = cliente;
        this.tipoPagamento = tipoPagamento;
        this.valor = valor;
        this.status = status;
        this.dataPagamento = dataPagamento;
        this.transacaoId = transacaoId;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public TipoPagamento getTipoPagamento() { return tipoPagamento; }
    public void setTipoPagamento(TipoPagamento tipoPagamento) { this.tipoPagamento = tipoPagamento; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public StatusPagamento getStatus() { return status; }
    public void setStatus(StatusPagamento status) { this.status = status; }

    public LocalDateTime getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDateTime dataPagamento) { this.dataPagamento = dataPagamento; }

    public String getTransacaoId() { return transacaoId; }
    public void setTransacaoId(String transacaoId) { this.transacaoId = transacaoId; }

    public boolean isAprovado() {
        return status == StatusPagamento.APROVADO;
    }

    @Override
    public String toString() {
        return String.format("Pagamento{pedido=%d, tipo=%s, valor=R$%.2f, status=%s, transacao=%s}",
                pedido.getId(), tipoPagamento.getDescricao(), valor, status.getDescricao(), 
                transacaoId != null ? transacaoId : "N/A");
    }
}