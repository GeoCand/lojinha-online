package com.lojinha.service;

import com.lojinha.model.Cliente;
import com.lojinha.repository.ClienteRepository;

import java.util.List;
import java.util.Optional;

public class ClienteService {
    private final ClienteRepository clienteRepository;

    public ClienteService() {
        this.clienteRepository = ClienteRepository.getInstance();
    }

    public Optional<Cliente> buscarClientePorCpf(String cpf) {
        return clienteRepository.findByCpf(cpf);
    }

    public List<Cliente> listarTodosClientes() {
        return clienteRepository.findAll();
    }

    public boolean clienteExiste(String cpf) {
        return clienteRepository.existsByCpf(cpf);
    }

    public void exibirClientes() {
        System.out.println("\n=== CLIENTES CADASTRADOS ===");
        clienteRepository.findAll().forEach(cliente -> 
            System.out.printf("CPF: %s | Nome: %s | Email: %s%n",
                    cliente.getCpf(), cliente.getNome(), cliente.getEmail())
        );
        System.out.println("============================\n");
    }
}
