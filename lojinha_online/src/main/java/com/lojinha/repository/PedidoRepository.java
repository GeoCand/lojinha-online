package com.lojinha.repository;

import com.lojinha.model.Pedido;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class PedidoRepository {
    private static PedidoRepository instance;
    private final Map<Integer, Pedido> pedidos;
    private final AtomicInteger idGenerator;

    private PedidoRepository() {
        this.pedidos = new HashMap<>();
        this.idGenerator = new AtomicInteger(1);
    }

    public static PedidoRepository getInstance() {
        if (instance == null) {
            instance = new PedidoRepository();
        }
        return instance;
    }

    public Pedido save(Pedido pedido) {
        if (pedido.getId() == null) {
            pedido.setId(idGenerator.getAndIncrement());
        }
        pedidos.put(pedido.getId(), pedido);
        return pedido;
    }

    public Optional<Pedido> findById(Integer id) {
        return Optional.ofNullable(pedidos.get(id));
    }

    public List<Pedido> findAll() {
        return pedidos.values().stream().collect(Collectors.toList());
    }

    public List<Pedido> findByClienteCpf(String cpf) {
        return pedidos.values().stream()
                .filter(p -> p.getCliente().getCpf().equals(cpf))
                .collect(Collectors.toList());
    }

    public Pedido update(Pedido pedido) {
        pedidos.put(pedido.getId(), pedido);
        return pedido;
    }

    public boolean existsById(Integer id) {
        return pedidos.containsKey(id);
    }
}