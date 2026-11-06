# CoreERP Backend Java

Serviço auxiliar em Java 21 para relatórios agregados do CoreERP, uma plataforma de gestão empresarial que pode ser adaptada a diferentes segmentos.

## Papel no produto

A API Node mantém o contrato operacional de clientes, produtos e pedidos. Este módulo Java consome esses recursos pela rede e entrega uma visão consolidada em `GET /reports/summary`. Assim, o projeto demonstra integração entre serviços em linguagens diferentes.

## Executar

Requer JDK 21 e Maven.

```bash
mvn test
mvn package
COREERP_API_URL=http://localhost:3000 java -jar target/coreerp-backend-java-0.1.0.jar
```

O serviço Java escuta na porta 8082. `COREERP_API_URL` aponta para a API principal.

## Rotas

- `GET /health`: estado do serviço Java.
- `GET /reports/summary`: totaliza clientes, produtos e pedidos consultando a API.

## Como explicar

O backend Java usa o `HttpClient` do JDK para chamar outro serviço, Jackson para ler JSON e um record para representar o resultado. Se a API estiver indisponível, responde com erro 502 em vez de inventar um resumo.

## Limites desta versão

A aplicação é uma base didática de integração: não possui autenticação, persistência nem cache. Use somente dados fictícios.
