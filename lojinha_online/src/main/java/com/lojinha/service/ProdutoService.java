package com.lojinha.service;

import com.lojinha.model.Produto;
import com.lojinha.repository.ProdutoRepository;

import java.util.List;
import java.util.Optional;

public class ProdutoService {
    private final ProdutoRepository produtoRepository;

    public ProdutoService() {
        this.produtoRepository = ProdutoRepository.getInstance();
    }

    public Optional<Produto> buscarProdutoPorCodigo(Long codigoBarras) {
        return produtoRepository.findByCodigoBarras(codigoBarras);
    }

    public List<Produto> listarTodosProdutos() {
        return produtoRepository.findAll();
    }

    public List<Produto> listarPorCategoria(String categoria) {
        return produtoRepository.findByCategoria(categoria);
    }

    public void exibirProdutos() {
        System.out.println("\n=== PRODUTOS DISPONÍVEIS ===");
        System.out.printf("%-15s %-25s %-10s %-10s%n", "Código Barras", "Nome", "Preço", "Estoque");
        System.out.println("-".repeat(70));
        produtoRepository.findAll().forEach(produto -> 
            System.out.printf("%-15d %-25s R$%-9.2f %-10d%n",
                    produto.getCodigoBarras(), 
                    produto.getNome(), 
                    produto.getPreco(),
                    produto.getEstoque())
        );
        System.out.println("=============================\n");
    }

    public boolean validarEstoque(Long codigoBarras, int quantidade) {
        Optional<Produto> produtoOpt = buscarProdutoPorCodigo(codigoBarras);
        return produtoOpt.map(produto -> produto.temEstoque(quantidade)).orElse(false);
    }

    public void diminuirEstoque(Long codigoBarras, int quantidade) {
        buscarProdutoPorCodigo(codigoBarras).ifPresent(produto -> 
            produto.diminuirEstoque(quantidade)
        );
    }
}