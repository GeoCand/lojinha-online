package com.lojinha.repository;

import com.lojinha.model.Produto;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProdutoRepository {
    private static ProdutoRepository instance;
    private final Map<Long, Produto> produtos;

    private ProdutoRepository() {
        this.produtos = new HashMap<>();
        inicializarDados();
    }

    public static ProdutoRepository getInstance() {
        if (instance == null) {
            instance = new ProdutoRepository();
        }
        return instance;
    }

    private void inicializarDados() {
        // Produtos estáticos
        Produto produto1 = new Produto(7891234567890L, "Notebook Dell", 
                "Notebook Dell Inspiron 15", new BigDecimal("3500.00"), 10, "Eletrônicos");
        Produto produto2 = new Produto(7891234567891L, "Mouse Logitech", 
                "Mouse sem fio Logitech M185", new BigDecimal("89.90"), 50, "Periféricos");
        Produto produto3 = new Produto(7891234567892L, "Teclado Mecânico", 
                "Teclado Mecânico RGB", new BigDecimal("299.99"), 30, "Periféricos");
        Produto produto4 = new Produto(7891234567893L, "Monitor 24\"", 
                "Monitor LED 24 polegadas", new BigDecimal("899.99"), 15, "Eletrônicos");
        Produto produto5 = new Produto(7891234567894L, "Cadeira Gamer", 
                "Cadeira Gamer Ergonômica", new BigDecimal("1299.00"), 5, "Móveis");
        Produto produto6 = new Produto(7891234567895L, "SSD 1TB", 
                "SSD NVMe 1TB", new BigDecimal("499.99"), 20, "Armazenamento");

        produtos.put(produto1.getCodigoBarras(), produto1);
        produtos.put(produto2.getCodigoBarras(), produto2);
        produtos.put(produto3.getCodigoBarras(), produto3);
        produtos.put(produto4.getCodigoBarras(), produto4);
        produtos.put(produto5.getCodigoBarras(), produto5);
        produtos.put(produto6.getCodigoBarras(), produto6);
    }

    public Optional<Produto> findByCodigoBarras(Long codigoBarras) {
        return Optional.ofNullable(produtos.get(codigoBarras));
    }

    public List<Produto> findAll() {
        return produtos.values().stream().collect(Collectors.toList());
    }

    public List<Produto> findByCategoria(String categoria) {
        return produtos.values().stream()
                .filter(p -> categoria.equals(p.getCategoria()))
                .collect(Collectors.toList());
    }

    public Produto save(Produto produto) {
        produtos.put(produto.getCodigoBarras(), produto);
        return produto;
    }

    public void updateEstoque(Long codigoBarras, int novaQuantidade) {
        Produto produto = produtos.get(codigoBarras);
        if (produto != null) {
            produto.setEstoque(novaQuantidade);
        }
    }
}