package com.lojinha.repository;

import com.lojinha.model.Cliente;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClienteRepository {
    private static ClienteRepository instance;
    private final Map<String, Cliente> clientes;

    private ClienteRepository() {
        this.clientes = new HashMap<>();
        inicializarDados();
    }

    public static ClienteRepository getInstance() {
        if (instance == null) {
            instance = new ClienteRepository();
        }
        return instance;
    }

    private void inicializarDados() {
        // Dados estáticos de clientes conforme requisito
        Cliente cliente1 = new Cliente("12345678901", "João Silva", "joao@email.com", "(11) 99999-1234");
        Cliente cliente2 = new Cliente("98765432100", "Maria Santos", "maria@email.com", "(11) 98888-5678");
        Cliente cliente3 = new Cliente("11122233344", "Pedro Oliveira", "pedro@email.com", "(11) 97777-9012");
        Cliente cliente4 = new Cliente("55566677788", "Ana Costa", "ana@email.com", "(11) 96666-3456");

        clientes.put(cliente1.getCpf(), cliente1);
        clientes.put(cliente2.getCpf(), cliente2);
        clientes.put(cliente3.getCpf(), cliente3);
        clientes.put(cliente4.getCpf(), cliente4);
    }

    public Optional<Cliente> findByCpf(String cpf) {
        return Optional.ofNullable(clientes.get(cpf));
    }

    public List<Cliente> findAll() {
        return clientes.values().stream().collect(Collectors.toList());
    }

    public Cliente save(Cliente cliente) {
        clientes.put(cliente.getCpf(), cliente);
        return cliente;
    }

    public boolean existsByCpf(String cpf) {
        return clientes.containsKey(cpf);
    }

    public int count() {
        return clientes.size();
    }
}