# Contratos de código com JML

JML (Java Modeling Language) descreve formalmente o comportamento esperado do
código Java. Os contratos são escritos em comentários especiais e, por isso, não
alteram a execução nem impedem a compilação normal do projeto.

Este projeto usa JML nas entidades de domínio:

- `Order`: invariantes do pedido e contratos para criação, inclusão de item,
  alteração de status e atualização do total;
- `OrderItem`: invariantes e contrato de criação de um item válido;
- `ProcessedEvent`: invariantes e contrato de criação de um evento processado.

## Como ler um contrato

Trecho simplificado de `Order.create`:

```java
/*@
  @ public normal_behavior
  @   requires customerId != null;
  @   ensures \result != null;
  @   ensures \result.status == OrderStatus.CREATED;
  @ also
  @ public exceptional_behavior
  @   requires customerId == null;
  @   signals_only IllegalArgumentException;
  @*/
public static Order create(UUID customerId) { ... }
```

- `requires`: condição que deve valer antes da chamada;
- `ensures`: condição garantida depois de uma conclusão normal;
- `\result`: valor retornado pelo método;
- `\old(expressão)`: valor da expressão antes da execução;
- `exceptional_behavior`: descreve uma saída por exceção;
- `signals_only`: limita os tipos de exceção permitidos naquele caso;
- `invariant`: regra que todo objeto válido deve preservar;
- `spec_public`: permite mencionar um campo privado em um contrato público sem
  mudar sua visibilidade no Java.

O contrato acima diz que um identificador de cliente válido produz um pedido no
estado `CREATED`; um identificador nulo deve produzir
`IllegalArgumentException`.

## JML não substitui validação nem testes

O comentário JML especifica a regra. O `if` presente no método continua sendo a
proteção executada em produção. Testes verificam exemplos concretos; a checagem
estática do OpenJML tenta provar que todos os caminhos da implementação respeitam
o contrato.

## Compilação normal

O compilador Java trata as especificações como comentários:

```powershell
.\mvnw.cmd test
```

Não é necessário adicionar uma dependência JML à aplicação.

## Verificação com OpenJML

O OpenJML é distribuído separadamente e usa uma versão própria do JDK 21. Após
baixá-lo e extraí-lo, a checagem estática básica usa:

```powershell
C:\caminho\openjml\openjml.bat --esc arquivo.java
```

Em uma aplicação Spring/JPA, verificar diretamente todas as entidades também
exige disponibilizar no classpath as dependências do projeto e as especificações
das bibliotecas usadas (`BigDecimal`, coleções, JPA e código gerado pelo Lombok).
Por isso, a integração do OpenJML não foi acoplada ao ciclo Maven: os contratos
permanecem úteis e compiláveis, mas ativar a prova formal completa deve ser uma
etapa separada e incremental.

Uma boa estratégia é começar por classes Java puras, sem framework, e então
expandir a verificação conforme as dependências ganham especificações adequadas.

## Diferença para Javadoc

Javadoc explica uma API para pessoas e gera HTML. JML expressa propriedades
verificáveis por ferramentas. Os dois formatos podem coexistir:

```java
/**
 * Atualiza o total do pedido.
 */
//@ requires total != null && total.signum() >= 0;
//@ ensures this.totalAmount == total;
public void updateTotal(BigDecimal total) { ... }
```

## Referências

- Tutorial oficial: https://www.openjml.org/tutorial/
- Sintaxe JML: https://www.openjml.org/tutorial/Syntax
- Download do OpenJML: https://www.openjml.org/downloads/
