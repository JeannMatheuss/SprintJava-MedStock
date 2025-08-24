# MedStock - Controle de Estoque de Materiais de Laboratório

![Java](https://img.shields.io/badge/Java-17-blue.svg)
![Maven](https://img.shields.io/badge/Maven-3.8-brightgreen.svg)
![JPA/Hibernate](https://img.shields.io/badge/JPA%2FHibernate-5.6-orange.svg)
![Swing](https://img.shields.io/badge/UI-Swing-red.svg)
![JUnit5/Mockito](https://img.shields.io/badge/Tests-JUnit5%2FMockito-green.svg)
![JaCoCo](https://img.shields.io/badge/Coverage-JaCoCo-yellow.svg)

MedStock é uma aplicação desktop desenvolvida em Java para o gerenciamento simplificado de estoque de insumos de laboratório. O projeto demonstra a aplicação de princípios de arquitetura limpa, separando as responsabilidades em camadas de domínio, aplicação, infraestrutura e apresentação.

## Funcionalidades

A aplicação possui uma interface gráfica simples para controlar o estoque de um item específico ("AstraZeneca") em um laboratório ("Laboratório Pfizer").

*   **Consulta Visual de Estoque:** A tela principal exibe a quantidade atual do item em estoque.
*   **Status por Cor:** O status do estoque é indicado por cores para rápida identificação:
    *   **Verde (OK):** Estoque seguro (acima de 20 unidades).
    *   **Amarelo (Preocupante):** Nível de atenção (entre 11 e 20 unidades).
    *   **Vermelho (Crítico):** Nível baixo (entre 1 e 10 unidades).
    *   **Preto (Extremo):** Estoque esgotado (0 unidades).
*   **Adição de Estoque:** Permite adicionar 10 unidades ao estoque, respeitando um limite máximo de 50.
*   **Remoção de Estoque:** Permite remover 10 unidades do estoque, respeitando um limite mínimo de 0.
*   **Persistência de Dados:** As alterações no estoque são salvas em um banco de dados Oracle via JPA/Hibernate.

## Arquitetura do Projeto

O projeto segue os princípios da Arquitetura Limpa, com uma clara separação de responsabilidades:

*   **`domain`**: O núcleo do negócio. Contém as entidades (ex: `Material`), as regras de negócio (ex: limites de estoque) e as interfaces dos repositórios. É totalmente independente de frameworks e do banco de dados.
*   **`application`**: Orquestra os casos de uso. O `EstoqueService` coordena as ações, utilizando as entidades do domínio e os repositórios para executar as operações.
*   **`infrastructure`**: Contém os detalhes de implementação.
    *   **`persistence`**: Implementações concretas dos repositórios usando JPA e Hibernate para se comunicar com o banco de dados Oracle.
    *   **`config`**: Configuração da conexão com o banco de dados (`JpaUtil`).
*   **`presentation`**: A camada de interface com o usuário, implementada com Java Swing (`EstoqueFrame`).

## Tecnologias Utilizadas

*   **Linguagem:** Java 17
*   **Gerenciador de Dependências:** Apache Maven
*   **Persistência:** JPA (Java Persistence API) com Hibernate
*   **Banco de Dados:** Oracle
*   **Interface Gráfica:** Java Swing
*   **Testes:** JUnit 5 e Mockito
*   **Cobertura de Testes:** JaCoCo

## Pré-requisitos

Para executar o projeto, você precisará ter instalado:

*   JDK 17 ou superior
*   Apache Maven
*   Acesso a um banco de dados Oracle (o projeto está configurado para o ambiente da FIAP, mas pode ser ajustado).

## Configuração do Ambiente

A aplicação se conecta ao banco de dados utilizando variáveis de ambiente para garantir a segurança das credenciais. Antes de executar, configure as seguintes variáveis no seu sistema ou na sua IDE:

*   `DB_HOST`: O endereço do servidor do banco de dados (ex: `oracle.fiap.com.br`).
*   `DB_NAME`: O nome do serviço ou SID do Oracle (ex: `ORCL`).
*   `DB_USER`: Seu nome de usuário do banco.
*   `DB_PASSWORD`: Sua senha.

**Exemplo de configuração no terminal (Linux/macOS):**
```bash
export DB_HOST='oracle.fiap.com.br'
export DB_NAME='ORCL'
export DB_USER='seu_rm'
export DB_PASSWORD='sua_senha'
```

## Como Executar

### 1. Executando a Aplicação Principal (com Interface Gráfica)

Use o Maven para compilar e executar a aplicação. O comando a seguir irá iniciar a interface gráfica Swing.

```bash
mvn compile exec:java -Dexec.mainClass="br.com.medstock.Main"
```
*Observação: Certifique-se de que as variáveis de ambiente estão configuradas antes de rodar este comando.*

### 2. Executando os Testes e Gerando o Relatório de Cobertura

O projeto possui uma suíte de testes unitários para a lógica de negócio e a camada de persistência. Para executar os testes e gerar um relatório de cobertura com o JaCoCo, utilize o comando `verify` do Maven.

```bash
mvn clean verify
```
Este comando irá:
1.  Limpar o projeto.
2.  Compilar todo o código.
3.  Executar todos os testes unitários.
4.  Gerar o relatório de cobertura do JaCoCo.

O relatório estará disponível no arquivo `target/site/jacoco/index.html`. Abra este arquivo em um navegador para visualizar quais partes do código foram cobertas pelos testes.

## Autor

*   **Pedro Sampaio**