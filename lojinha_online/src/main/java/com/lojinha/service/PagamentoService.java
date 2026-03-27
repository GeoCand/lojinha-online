package com.lojinha.service;

import com.lojinha.external.GatewayPagamentoSingleton;
import com.lojinha.model.Pagamento;
import com.lojinha.model.Pagamento.StatusPagamento;
import com.lojinha.model.Pedido;
import com.lojinha.model.Pedido.StatusPedido;

import java.util.Optional;

public class PagamentoService {
    private final GatewayPagamentoSingleton gateway;
    private final PedidoService pedidoService;
    private final ProdutoService produtoService;

    public PagamentoService() {
        this.gateway = GatewayPagamentoSingleton.getInstance();
        this.pedidoService = new PedidoService();
        this.produtoService = new ProdutoService();
    }

    public Pagamento processarPagamentoPedido(Integer pedidoId, Pagamento.TipoPagamento tipoPagamento, String cpfCliente) {
        Optional<Pedido> pedidoOpt = pedidoService.buscarPedido(pedidoId);
        
        if (pedidoOpt.isEmpty()) {
            throw new IllegalArgumentException("Pedido não encontrado: " + pedidoId);
        }
        
        Pedido pedido = pedidoOpt.get();
        
        if (!pedido.getCliente().getCpf().equals(cpfCliente)) {
            throw new IllegalArgumentException("Pedido não pertence ao cliente informado");
        }
        
        if (pedido.isPago()) {
            throw new IllegalStateException("Pedido já foi pago!");
        }
        
        if (pedido.isCancelado()) {
            throw new IllegalStateException("Pedido cancelado, não é possível realizar pagamento");
        }
        
        if (pedido.getItens().isEmpty()) {
            throw new IllegalStateException("Pedido sem itens, não é possível realizar pagamento");
        }
        
        Pagamento pagamento = new Pagamento(pedido, pedido.getCliente(), tipoPagamento);
        
        pagamento = gateway.processarPagamento(pagamento);
        
        if (pagamento.isAprovado()) {
            pedido.setStatus(StatusPedido.PAGO.getDescricao());
            
            pedido.getItens().forEach(item -> 
                produtoService.diminuirEstoque(
                    item.getProduto().getCodigoBarras(), 
                    item.getQuantidade()
                )
            );
            
            pedidoService.atualizarStatusPedido(pedidoId, StatusPedido.PAGO.getDescricao());
            System.out.println(" Pagamento aprovado! Pedido #" + pedidoId + " confirmado.");
        } else {
            pedido.setStatus(StatusPedido.PENDENTE.getDescricao());
            pedidoService.atualizarStatusPedido(pedidoId, StatusPedido.PENDENTE.getDescricao());
            System.out.println(" Pagamento recusado! Pedido #" + pedidoId + " continua pendente.");
        }
        
        return pagamento;
    }
    
    public void cancelarPagamento(Integer pagamentoId) {
        System.out.println("Pagamento #" + pagamentoId + " cancelado.");
    }
    
    public void exibirStatusGateway() {
        System.out.println("\n=== STATUS DO GATEWAY DE PAGAMENTO ===");
        System.out.println("Disponível: " + (gateway.isDisponivel() ? "Sim" : "Não"));
        gateway.exibirEstatisticas();
    }
}