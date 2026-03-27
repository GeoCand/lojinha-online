package com.lojinha.external;

import com.lojinha.model.Pagamento;
import com.lojinha.model.Pagamento.StatusPagamento;
import com.lojinha.model.Pagamento.TipoPagamento;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

public class GatewayPagamentoSingleton {
    
    private static volatile GatewayPagamentoSingleton instance;
    private final Random random;
    private int totalTransacoes;
    private int transacoesAprovadas;
    private int transacoesRecusadas;
    
    private GatewayPagamentoSingleton() {
        this.random = new Random();
        this.totalTransacoes = 0;
        this.transacoesAprovadas = 0;
        this.transacoesRecusadas = 0;
        
        System.out.println("[Gateway] Inicializando conexão com serviço de pagamento...");
        System.out.println("[Gateway] Conexão estabelecida com sucesso!");
    }
    
    public static GatewayPagamentoSingleton getInstance() {
        if (instance == null) {
            synchronized (GatewayPagamentoSingleton.class) {
                if (instance == null) {
                    instance = new GatewayPagamentoSingleton();
                }
            }
        }
        return instance;
    }
    
    public Pagamento processarPagamento(Pagamento pagamento) {
        System.out.println("\n[Gateway] Processando pagamento...");
        System.out.println("[Gateway] Pedido #" + pagamento.getPedido().getId());
        System.out.println("[Gateway] Valor: R$" + pagamento.getValor());
        System.out.println("[Gateway] Tipo: " + pagamento.getTipoPagamento().getDescricao());
      
        simularTempoProcessamento();
        
        boolean aprovado = simularValidacaoPagamento(pagamento);
        
        totalTransacoes++;
        
        if (aprovado) {
            transacoesAprovadas++;
            pagamento.setStatus(StatusPagamento.APROVADO);
            pagamento.setDataPagamento(LocalDateTime.now());
            pagamento.setTransacaoId(gerarTransacaoId());
            System.out.println("[Gateway] ✅ Pagamento APROVADO! Transação: " + pagamento.getTransacaoId());
        } else {
            transacoesRecusadas++;
            pagamento.setStatus(StatusPagamento.RECUSADO);
            System.out.println("[Gateway] ❌ Pagamento RECUSADO!");
        }
        
        System.out.println("[Gateway] Processamento finalizado\n");
        return pagamento;
    }

    private boolean simularValidacaoPagamento(Pagamento pagamento) {
        BigDecimal valor = pagamento.getValor();
        TipoPagamento tipo = pagamento.getTipoPagamento();

        double taxaAprovacao;
        switch (tipo) {
            case CARTAO_CREDITO:
                taxaAprovacao = 0.90;
                break;
            case PIX:
                taxaAprovacao = 0.95;
                break;
            case BOLETO:
                taxaAprovacao = 0.80;
                break;
            case CARTAO_DEBITO:
                taxaAprovacao = 0.85;
                break;
            default:
                taxaAprovacao = 0.70;
        }
        
        // Valores altos reduzem chance de aprovação
        if (valor.compareTo(new BigDecimal("5000")) > 0) {
            taxaAprovacao -= 0.20;
        } else if (valor.compareTo(new BigDecimal("3000")) > 0) {
            taxaAprovacao -= 0.10;
        }
        
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
    
        double resultado = random.nextDouble();
        return resultado <= taxaAprovacao;
    }
    

    private void simularTempoProcessamento() {
        try {
            int tempo = 100 + random.nextInt(400);
            Thread.sleep(tempo);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("[Gateway] Erro durante processamento: " + e.getMessage());
        }
    }
    
    private String gerarTransacaoId() {
        return "TXN_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public void exibirEstatisticas() {
        System.out.println("\n=== ESTATÍSTICAS DO GATEWAY ===");
        System.out.println("Total de transações: " + totalTransacoes);
        System.out.println("Transações aprovadas: " + transacoesAprovadas);
        System.out.println("Transações recusadas: " + transacoesRecusadas);
        if (totalTransacoes > 0) {
            double taxaAprovacao = (transacoesAprovadas * 100.0) / totalTransacoes;
            System.out.printf("Taxa de aprovação: %.2f%%%n", taxaAprovacao);
        }
        System.out.println("================================\n");
    }
    
    public boolean isDisponivel() {
        return true;
    }
}