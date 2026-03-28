# Lojinha Online - Sistema de E-commerce

## Visão Geral

Este projeto é uma simulação de um sistema de e-commerce desenvolvido em Java, seguindo uma arquitetura cliente-servidor monolítica. O sistema implementa as principais funcionalidades de uma loja online, incluindo cadastro de clientes, catálogo de produtos, gestão de pedidos e processamento de pagamentos.

---

## Estrutura do Projeto

O código foi organizado seguindo os princípios básicos de Orientação a Objetos e está estruturado da seguinte forma:
Classes de Modelo: Entidades básicas que representam o domínio do problema (Cliente, Produto, Pedido, ItemPedido e Pagamento). Elas guardam o estado e as informações dos objetos.
Classe Principal (LojinhaSimulation): Atua como o núcleo da simulação, contendo o método main. Ela instancia os objetos e simula o fluxo do cliente navegando, escolhendo produtos, fechando o pedido e chamando o pagamento.
Integração Externa (GatewayPagamentoSingleton): Classe que simula a comunicação com o sistema externo do banco para aprovação da compra.


```
lojinha-online/
│
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── lojinha/
│                   │
│                   ├── Main.java                          # Classe principal (ponto de entrada)
│                   │
│                   ├── model/                             # Camada de Modelo (Entidades)
│                   │   ├── Cliente.java                   # Entidade Cliente
│                   │   ├── Produto.java                   # Entidade Produto
│                   │   ├── Pedido.java                    # Entidade Pedido
│                   │   ├── ItemPedido.java                # Entidade Item do Pedido
│                   │   └── Pagamento.java                 # Entidade Pagamento
│                   │
│                   ├── service/                           # Camada de Serviço (Regras de Negócio)
│                   │   ├── ClienteService.java            # Regras de negócio para clientes
│                   │   ├── ProdutoService.java            # Regras de negócio para produtos
│                   │   ├── PedidoService.java             # Regras de negócio para pedidos
│                   │   └── PagamentoService.java          # Regras de negócio para pagamentos
│                   │
│                   ├── repository/                        # Camada de Repositório (Acesso a Dados)
│                   │   ├── ClienteRepository.java         # Armazenamento de clientes
│                   │   ├── ProdutoRepository.java         # Armazenamento de produtos
│                   │   └── PedidoRepository.java          # Armazenamento de pedidos
│                   │
│                   └── external/                          # Camada de Integração Externa
│                       └── GatewayPagamentoSingleton.java # Gateway de pagamento (Singleton)
│
└──Diagrama-de-atividades.drawio                           # Diagrama de atividades explicando o sistema
│
└──DER(Lojinha)                                            # Diagrama Entidade relacionamento da loja
│
└── README.md                                              # Este arquivo
```

---

## Principais Decisões Arquiteturais

### 1. Arquitetura Cliente-Servidor Monolítica

O sistema foi desenvolvido como uma aplicação monolítica, onde todas as camadas (apresentação, negócio e dados) estão contidas em um único artefato.

**Justificativa:**
- **Simplicidade**: Ideal para o escopo do projeto, facilitando a compreensão e manutenção
- **Baixo Acoplamento entre Camadas**: A separação em packages (`model`, `service`, `repository`) mantém as responsabilidades bem definidas
- **Facilidade de Testes**: A estrutura modular permite testar cada camada isoladamente
- **Desempenho**: Comunicação interna entre camadas sem overhead de rede

### 2. Separação de Camadas

| Camada | Responsabilidade | Exemplo |
|--------|------------------|---------|
| **Model** | Representação dos dados e regras básicas de validação | `Cliente`, `Produto`, `Pedido` |
| **Service** | Regras de negócio e orquestração de operações | `PedidoService.criarPedido()` |
| **Repository** | Persistência e recuperação de dados (simulação) | `ClienteRepository.getInstance()` |
| **External** | Integração com sistemas externos | `GatewayPagamentoSingleton` |

### 3. Padrão Repository

Cada entidade possui um repositório responsável pelo armazenamento e recuperação de dados.

**Justificativa:**
- **Abstração do Armazenamento**: Facilita futuras migrações para banco de dados real
- **Centralização da Lógica de Persistência**: Toda manipulação de dados ocorre em um único local
- **Testabilidade**: Permite fácil substituição por mocks em testes

### 4. Padrão Service

A lógica de negócio foi encapsulada em classes Service.

**Justificativa:**
- **Separação de Responsabilidades**: A camada de apresentação (Main) não contém regras de negócio
- **Reutilização**: Serviços podem ser chamados por diferentes pontos da aplicação
- **Manutenibilidade**: Mudanças nas regras de negócio afetam apenas as classes Service

---

## Padrão Singleton - Implementação e Justificativa

O Padrão de Projeto Singleton foi aplicado exclusivamente na classe GatewayPagamentoSingleton.
Como foi aplicado: A classe possui um atributo estático e privado dela mesma. O construtor foi definido como private (para que nenhuma outra classe possa usar new GatewayPagamentoSingleton()). O único meio de acessar a classe é chamando o método público e estático getInstance(), que cria a instância na primeira vez e apenas a retorna nas chamadas seguintes.
Justificativa do uso: O gateway de pagamento simula um recurso externo e crítico. Em um sistema real, abrir múltiplas conexões simultâneas com uma API de banco desperdiça recursos de rede e memória, além de gerar risco de transações duplicadas. O uso do Singleton garante que exista apenas uma instância gerenciando essa comunicação durante toda a execução do programa, centralizando o tráfego de pagamentos de forma segura e econômica.

### Onde o Singleton foi Aplicado

O padrão Singleton foi implementado na classe `GatewayPagamentoSingleton`, localizada no package `com.lojinha.external`.

### Estrutura da Classe Singleton

```java
public class GatewayPagamentoSingleton {
    
    private static volatile GatewayPagamentoSingleton instance;
    
    private GatewayPagamentoSingleton() {
        // Construtor privado - impede instanciação externa
        // Inicializa conexão com serviço externo
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
        // Processamento do pagamento via serviço externo
    }
}
```

### Por que Singleton neste Contexto?

#### 1. Recurso Compartilhado Único

O gateway de pagamento representa uma **conexão com um serviço externo** (API de pagamento, adquirente, etc.). Este tipo de recurso deve ser acessado de forma controlada e centralizada para evitar:

- **Conflitos de transação**: Múltiplas instâncias poderiam gerar inconsistências
- **Uso excessivo de recursos**: Conexões duplicadas consomem memória e recursos de rede
- **Problemas de concorrência**: Dificuldade em coordenar transações simultâneas

#### 2. Ponto Único de Configuração

O Singleton centraliza configurações críticas como:
- URL do serviço de pagamento
- Chaves de autenticação
- Timeouts e políticas de retentativa
- Logs e monitoramento

```java
// Exemplo de centralização
private GatewayPagamentoSingleton() {
    // Configurações centralizadas
    this.apiUrl = "https://api.gateway.com/v1/payments";
    this.apiKey = System.getenv("GATEWAY_API_KEY");
    this.timeout = 30000; // 30 segundos
    this.maxRetries = 3;
}
```

#### 3. Controle de Estado Global

A instância única mantém estatísticas globais do gateway:

```java
private int totalTransacoes;
private int transacoesAprovadas;
private int transacoesRecusadas;

public void exibirEstatisticas() {
    // Acesso centralizado às estatísticas
}
```

#### 4. Economia de Recursos

Sem o Singleton:
```
[Thread 1] new Gateway() → Abre conexão TCP
[Thread 2] new Gateway() → Abre outra conexão TCP
[Thread 3] new Gateway() → Abre mais uma conexão TCP
Total: 3 conexões simultâneas
```

Com o Singleton:
```
[Thread 1] Gateway.getInstance() → Cria única conexão
[Thread 2] Gateway.getInstance() → Reutiliza conexão existente
[Thread 3] Gateway.getInstance() → Reutiliza conexão existente
Total: 1 conexão
```

#### 5. Thread-Safety

A implementação utiliza **double-checked locking** para garantir thread-safety em ambientes concorrentes:

```java
public static GatewayPagamentoSingleton getInstance() {
    if (instance == null) {                    // 1ª verificação (sem lock)
        synchronized (GatewayPagamentoSingleton.class) {
            if (instance == null) {            // 2ª verificação (com lock)
                instance = new GatewayPagamentoSingleton();
            }
        }
    }
    return instance;
}
```

Isso garante que mesmo com múltiplas threads acessando simultaneamente, apenas uma instância será criada.

### Alternativas Consideradas (e Por que Foram Rejeitadas)

| Alternativa | Motivo da Rejeição |
|-------------|-------------------|
| **Instância por Requisição** | Criaria múltiplas conexões, desperdiçando recursos |
| **Métodos Estáticos** | Dificulta testes unitários (mock) e manutenção de estado |
| **Injeção de Dependência** | Overkill para o escopo do projeto, mas seria a evolução natural |

---

## Fluxo de Processamento de Pagamento com Singleton

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    FLUXO DE PROCESSAMENTO DE PAGAMENTO                      │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  Main (UI)                                                                  │
│     │                                                                       │
│     │ processarPagamento()                                                  │
│     ▼                                                                       │
│  ┌─────────────────┐                                                        │
│  │ PagamentoService│                                                        │
│  └────────┬────────┘                                                        │
│           │                                                                 │
│           │ gateway = GatewayPagamentoSingleton.getInstance()               │
│           ▼                                                                 │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │              GATEWAY (SINGLETON - ÚNICA INSTÂNCIA)                  │    │
│  │  ┌─────────────────────────────────────────────────────────────┐    │    │
│  │  │ Estado Global:                                              │    │    │
│  │  │ • totalTransacoes = 42                                      │    │    │
│  │  │ • transacoesAprovadas = 38                                  │    │    │
│  │  │ • transacoesRecusadas = 4                                   │    │    │
│  │  │ • apiUrl configurado                                        │    │    │
│  │  │ • timeout definido                                          │    │    │
│  │  └─────────────────────────────────────────────────────────────┘    │    │
│  │                              │                                      │    │
│  │                              │ processarPagamento()                 │    │
│  │                              ▼                                      │    │
│  │              [SERVIÇO EXTERNO DE PAGAMENTO]                         │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Como Executar

### Pré-requisitos
- Java 11 ou superior
- JDK instalado

### Compilação

```bash
# Navegar até o diretório src
cd src

# Compilar todos os arquivos
javac -d ../out main/java/com/lojinha/*.java \
      main/java/com/lojinha/model/*.java \
      main/java/com/lojinha/service/*.java \
      main/java/com/lojinha/repository/*.java \
      main/java/com/lojinha/external/*.java
```

### Execução

```bash
# Executar a classe principal
java -cp ../out com.lojinha.Main
```

### Menu da Aplicação

```
╔════════════════════════════════════════╗
║              MENU PRINCIPAL            ║
╠════════════════════════════════════════╣
║  1 - Listar Clientes                   ║
║  2 - Listar Produtos                   ║
║  3 - Criar Novo Pedido                 ║
║  4 - Adicionar Item ao Pedido          ║
║  5 - Visualizar Pedido                 ║
║  6 - Processar Pagamento               ║
║  7 - Status do Gateway de Pagamento    ║
║  0 - Sair                              ║
╚════════════════════════════════════════╝
```

---

## Demonstração do Singleton em Ação

Ao executar a aplicação, você pode observar o comportamento do Singleton:

### Inicialização
```
[Gateway] Inicializando conexão com serviço de pagamento...
[Gateway] Conexão estabelecida com sucesso!
```
*Nota: Esta mensagem aparece apenas UMA vez, independentemente de quantas vezes o gateway for acessado.*

### Durante o Processamento
```
=== ESTATÍSTICAS DO GATEWAY ===
Total de transações: 5
Transações aprovadas: 4
Transações recusadas: 1
Taxa de aprovação: 80.00%
================================
```
*As estatísticas são acumuladas globalmente pela instância única.*

---

## Evoluções Futuras

Caso o projeto fosse expandido para produção, possíveis evoluções seriam:

| Componente | Evolução Proposta |
|------------|-------------------|
| **Gateway** | Implementar interface para permitir múltiplos gateways (PagSeguro, Stripe) |
| **Persistência** | Substituir Repository por JPA/Hibernate com banco de dados real |
| **Singleton** | Migrar para injeção de dependência (Spring Framework) |
| **API** | Adicionar camada REST com Spring Boot |

---

## Considerações Finais

O uso do padrão Singleton no `GatewayPagamentoSingleton` é uma escolha arquitetural adequada para este contexto específico, onde:

1. **Apenas uma conexão** com o serviço externo é necessária
2. **Estado global** precisa ser mantido (estatísticas, logs)
3. **Recursos compartilhados** (conexão de rede) devem ser otimizados
4. **Configuração centralizada** facilita manutenção

A implementação thread-safe garante que mesmo em cenários de múltiplas requisições simultâneas (simulando um ambiente web), a integridade do gateway seja mantida.
