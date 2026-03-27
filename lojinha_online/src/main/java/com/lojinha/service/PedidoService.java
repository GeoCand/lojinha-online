package com.lojinha.service;

import com.lojinha.model.*;
import com.lojinha.repository.PedidoRepository;

import java.util.Optional;

public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final ProdutoService produtoService;
    private final ClienteService clienteService;

    public PedidoService() {
        this.pedidoRepository = PedidoRepository.getInstance();
        this.produtoService = new ProdutoService();
        this.clienteService = new ClienteService();
    }

    public Pedido criarPedido(String cpfCliente) {
        Optional<Cliente> clienteOpt = clienteService.buscarClientePorCpf(cpfCliente);
        
        if (clienteOpt.isEmpty()) {
            throw new IllegalArgumentException("Cliente não encontrado com CPF: " + cpfCliente);
        }

        Pedido pedido = new Pedido(clienteOpt.get());
        return pedidoRepository.save(pedido);
    }

    public Pedido adicionarItemAoPedido(Integer pedidoId, Long codigoProduto, int quantidade) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(pedidoId);
        Optional<Produto> produtoOpt = produtoService.buscarProdutoPorCodigo(codigoProduto);

        if (pedidoOpt.isEmpty()) {
            throw new IllegalArgumentException("Pedido não encontrado com ID: " + pedidoId);
        }

        if (produtoOpt.isEmpty()) {
            throw new IllegalArgumentException("Produto não encontrado com código: " + codigoProduto);
        }

        Pedido pedido = pedidoOpt.get();
        Produto produto = produtoOpt.get();

        if (pedido.isPago() || pedido.isCancelado()) {
            throw new IllegalStateException("Não é possível adicionar itens a um pedido " + 
                    (pedido.isPago() ? "já pago" : "cancelado"));
        }

        if (!produto.temEstoque(quantidade)) {
            throw new IllegalStateException("Estoque insuficiente para o produto: " + produto.getNome());
        }

        ItemPedido item = new ItemPedido(produto, quantidade);
        pedido.adicionarItem(item);

        pedido.setStatus(Pedido.StatusPedido.AGUARDANDO_PAGAMENTO.getDescricao());
        
        return pedidoRepository.update(pedido);
    }

    public Optional<Pedido> buscarPedido(Integer id) {
        return pedidoRepository.findById(id);
    }

    public void exibirPedido(Integer id) {
        Optional<Pedido> pedidoOpt = buscarPedido(id);
        if (pedidoOpt.isPresent()) {
            System.out.println("\n=== DETALHES DO PEDIDO ===");
            System.out.println(pedidoOpt.get());
            System.out.println("==========================\n");
        } else {
            System.out.println("Pedido não encontrado!\n");
        }
    }

    public Pedido atualizarStatusPedido(Integer id, String novoStatus) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);
        if (pedidoOpt.isEmpty()) {
            throw new IllegalArgumentException("Pedido não encontrado");
        }
        Pedido pedido = pedidoOpt.get();
        pedido.setStatus(novoStatus);
        return pedidoRepository.update(pedido);
    }
}