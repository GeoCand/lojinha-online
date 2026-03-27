package com.lojinha;

import com.lojinha.model.Pagamento;
import com.lojinha.model.Pedido;
import com.lojinha.service.ClienteService;
import com.lojinha.service.PagamentoService;
import com.lojinha.service.PedidoService;
import com.lojinha.service.ProdutoService;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    
    private static final ClienteService clienteService = new ClienteService();
    private static final ProdutoService produtoService = new ProdutoService();
    private static final PedidoService pedidoService = new PedidoService();
    private static final PagamentoService pagamentoService = new PagamentoService();
    private static final Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║     LOJINHA ONLINE - SIMULAÇÃO        ║");
        System.out.println("║     Arquitetura Cliente-Servidor      ║");
        System.out.println("╚════════════════════════════════════════╝");
        
        boolean executando = true;
        
        while (executando) {
            exibirMenuPrincipal();
            int opcao = lerInteiro("Escolha uma opção: ");
            
            switch (opcao) {
                case 1:
                    listarClientes();
                    break;
                case 2:
                    listarProdutos();
                    break;
                case 3:
                    criarNovoPedido();
                    break;
                case 4:
                    adicionarItemAoPedido();
                    break;
                case 5:
                    visualizarPedido();
                    break;
                case 6:
                    processarPagamento();
                    break;
                case 7:
                    exibirStatusGateway();
                    break;
                case 0:
                    executando = false;
                    System.out.println("\nEncerrando o sistema. Obrigado!");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        }
        
        scanner.close();
    }
    
    private static void exibirMenuPrincipal() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║              MENU PRINCIPAL            ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║  1 - Listar Clientes                  ║");
        System.out.println("║  2 - Listar Produtos                  ║");
        System.out.println("║  3 - Criar Novo Pedido                ║");
        System.out.println("║  4 - Adicionar Item ao Pedido         ║");
        System.out.println("║  5 - Visualizar Pedido                ║");
        System.out.println("║  6 - Processar Pagamento              ║");
        System.out.println("║  7 - Status do Gateway de Pagamento   ║");
        System.out.println("║  0 - Sair                             ║");
        System.out.println("╚════════════════════════════════════════╝");
    }
    
    private static void listarClientes() {
        System.out.println("\n--- LISTA DE CLIENTES ---");
        clienteService.exibirClientes();
    }
    
    private static void listarProdutos() {
        System.out.println("\n--- LISTA DE PRODUTOS ---");
        produtoService.exibirProdutos();
    }
    
    private static void criarNovoPedido() {
        System.out.println("\n--- CRIAR NOVO PEDIDO ---");
        
        // Listar clientes disponíveis
        clienteService.exibirClientes();
        
        String cpf = lerString("Digite o CPF do cliente: ");
        
        try {
            Pedido pedido = pedidoService.criarPedido(cpf);
            System.out.println("\n Pedido criado com sucesso!");
            System.out.println("ID do Pedido: " + pedido.getId());
            System.out.println("Cliente: " + pedido.getCliente().getNome());
        } catch (IllegalArgumentException e) {
            System.out.println("\n Erro: " + e.getMessage());
        }
    }
    
    private static void adicionarItemAoPedido() {
        System.out.println("\n--- ADICIONAR ITEM AO PEDIDO ---");
        
        int pedidoId = lerInteiro("Digite o ID do pedido: ");
        
        // Listar produtos disponíveis
        produtoService.exibirProdutos();
        
        Long codigoProduto = lerLong("Digite o código de barras do produto: ");
        int quantidade = lerInteiro("Digite a quantidade: ");
        
        try {
            Pedido pedido = pedidoService.adicionarItemAoPedido(pedidoId, codigoProduto, quantidade);
            System.out.println("\n Item adicionado com sucesso!");
            System.out.printf("Subtotal atual do pedido: R$%.2f%n", pedido.getValorTotal());
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("\n Erro: " + e.getMessage());
        }
    }
    
    private static void visualizarPedido() {
        System.out.println("\n--- VISUALIZAR PEDIDO ---");
        
        int pedidoId = lerInteiro("Digite o ID do pedido: ");
        pedidoService.exibirPedido(pedidoId);
    }
    
    private static void processarPagamento() {
        System.out.println("\n--- PROCESSAR PAGAMENTO ---");
        
        int pedidoId = lerInteiro("Digite o ID do pedido: ");
        
        // Verificar se pedido existe
        Optional<Pedido> pedidoOpt = pedidoService.buscarPedido(pedidoId);
        if (pedidoOpt.isEmpty()) {
            System.out.println(" Pedido não encontrado!");
            return;
        }
        
        Pedido pedido = pedidoOpt.get();
        
        System.out.println("\nPedido #" + pedido.getId());
        System.out.println("Cliente: " + pedido.getCliente().getNome());
        System.out.printf("Valor total: R$%.2f%n", pedido.getValorTotal());
        System.out.println("Status atual: " + pedido.getStatus());
        
        if (pedido.getItens().isEmpty()) {
            System.out.println(" Pedido sem itens. Adicione itens antes de processar pagamento.");
            return;
        }
        
        if (pedido.isPago()) {
            System.out.println(" Este pedido já foi pago!");
            return;
        }
        
        System.out.println("\nOpções de pagamento:");
        System.out.println("1 - Cartão de Crédito");
        System.out.println("2 - Cartão de Débito");
        System.out.println("3 - PIX");
        System.out.println("4 - Boleto");
        
        int opcaoPagamento = lerInteiro("Escolha a forma de pagamento: ");
        
        Pagamento.TipoPagamento tipo;
        switch (opcaoPagamento) {
            case 1:
                tipo = Pagamento.TipoPagamento.CARTAO_CREDITO;
                break;
            case 2:
                tipo = Pagamento.TipoPagamento.CARTAO_DEBITO;
                break;
            case 3:
                tipo = Pagamento.TipoPagamento.PIX;
                break;
            case 4:
                tipo = Pagamento.TipoPagamento.BOLETO;
                break;
            default:
                System.out.println("Opção inválida!");
                return;
        }
        
        try {
            Pagamento pagamento = pagamentoService.processarPagamentoPedido(pedidoId, tipo, pedido.getCliente().getCpf());
            
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║         RESULTADO DO PAGAMENTO         ║");
            System.out.println("╠════════════════════════════════════════╣");
            System.out.println("║ Pedido: " + pagamento.getPedido().getId());
            System.out.println("║ Valor: R$" + pagamento.getValor());
            System.out.println("║ Tipo: " + pagamento.getTipoPagamento().getDescricao());
            System.out.println("║ Status: " + pagamento.getStatus().getDescricao());
            if (pagamento.getTransacaoId() != null) {
                System.out.println("║ Transação: " + pagamento.getTransacaoId());
            }
            System.out.println("╚════════════════════════════════════════╝");
            
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("\n Erro ao processar pagamento: " + e.getMessage());
        }
    }
    
    private static void exibirStatusGateway() {
        pagamentoService.exibirStatusGateway();
    }
    
    // Métodos auxiliares para leitura
    private static String lerString(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine().trim();
    }
    
    private static int lerInteiro(String mensagem) {
        System.out.print(mensagem);
        while (!scanner.hasNextInt()) {
            System.out.print("Valor inválido. " + mensagem);
            scanner.next();
        }
        int valor = scanner.nextInt();
        scanner.nextLine(); // limpar buffer
        return valor;
    }
    
    private static Long lerLong(String mensagem) {
        System.out.print(mensagem);
        while (!scanner.hasNextLong()) {
            System.out.print("Valor inválido. " + mensagem);
            scanner.next();
        }
        long valor = scanner.nextLong();
        scanner.nextLine(); // limpar buffer
        return valor;
    }
}