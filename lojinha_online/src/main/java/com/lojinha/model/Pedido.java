package com.lojinha.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private Integer id;
    private Cliente cliente;
    private LocalDateTime dataPedido;
    private String status;
    private BigDecimal valorTotal;
    private List<ItemPedido> itens;

    public enum StatusPedido {
        PENDENTE("Pendente"),
        AGUARDANDO_PAGAMENTO("Aguardando Pagamento"),
        PAGO("Pago"),
        CANCELADO("Cancelado"),
        ENTREGUE("Entregue");

        private String descricao;

        StatusPedido(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    public Pedido(Cliente cliente) {
        this.cliente = cliente;
        this.dataPedido = LocalDateTime.now();
        this.status = StatusPedido.PENDENTE.getDescricao();
        this.valorTotal = BigDecimal.ZERO;
        this.itens = new ArrayList<>();
    }

    public Pedido(Integer id, Cliente cliente, LocalDateTime dataPedido, String status, BigDecimal valorTotal) {
        this.id = id;
        this.cliente = cliente;
        this.dataPedido = dataPedido;
        this.status = status;
        this.valorTotal = valorTotal;
        this.itens = new ArrayList<>();
    }

    public void adicionarItem(ItemPedido item) {
        itens.add(item);
        recalcularTotal();
    }

    public void removerItem(ItemPedido item) {
        itens.remove(item);
        recalcularTotal();
    }

    private void recalcularTotal() {
        valorTotal = itens.stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public LocalDateTime getDataPedido() { return dataPedido; }
    public void setDataPedido(LocalDateTime dataPedido) { this.dataPedido = dataPedido; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getValorTotal() { return valorTotal; }

    public List<ItemPedido> getItens() { return itens; }

    public boolean isPago() {
        return StatusPedido.PAGO.getDescricao().equals(status);
    }

    public boolean isCancelado() {
        return StatusPedido.CANCELADO.getDescricao().equals(status);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Pedido #%d\n", id));
        sb.append(String.format("Cliente: %s (CPF: %s)\n", cliente.getNome(), cliente.getCpf()));
        sb.append(String.format("Data: %s\n", dataPedido));
        sb.append(String.format("Status: %s\n", status));
        sb.append("Itens:\n");
        itens.forEach(item -> sb.append("  ").append(item).append("\n"));
        sb.append(String.format("Valor Total: R$%.2f\n", valorTotal));
        return sb.toString();
    }
}