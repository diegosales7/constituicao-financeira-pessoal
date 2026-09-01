# Constituição Financeira

Uma plataforma de gestão financeira para pessoas e empresas.

## Visão geral

A plataforma agora atende dois contextos principais:

- 👤 Constituição Financeira Pessoal
- 🏢 Constituição Financeira PJ

## Objetivo

Centralizar gestão financeira pessoal e empresarial em um único produto, preservando as regras da Constituição Financeira Pessoal e expandindo a base para governança financeira corporativa.

## Funcionalidades PF

- Dashboard pessoal
- Receitas, despesas e transações
- Cartões, ativos e reserva flex
- Fluxo de caixa pessoal
- Termômetro financeiro
- Estado de emergência
- Livro de pendências
- Constituição financeira pessoal

## Funcionalidades PJ

- Dashboard empresarial
- Empresa e usuários responsáveis
- Contas bancárias
- Tesouraria
- Fluxo de caixa e projeções
- Contas a pagar e receber
- Orçamento e planejamento
- Indicadores financeiros
- Estrutura de governança financeira PJ

## Arquitetura

- Frontend: HTML + CSS + JavaScript estático em `financas-frontend`
- Backend: Spring Boot + Spring Security + JWT + JPA em `constituicao-financeira-pessoal/pessoais`
- Banco: PostgreSQL
- Containerização: Docker + Docker Compose

## Tecnologias

- Java 17
- Spring Boot 4
- Spring Security
- JWT
- JPA / Hibernate
- PostgreSQL
- Nginx
- Docker

## Como executar localmente

1. Configure o arquivo `.env` na raiz com as variáveis do PostgreSQL e JWT.
2. Execute:

```bash
docker compose up -d --build
```

3. Acesse o frontend em `http://localhost` e a API em `http://localhost/api`.

## Configuração de ambiente

Exemplos e variáveis de ambiente estão em:

- `.env.example`
- `constituicao-financeira-pessoal/pessoais/src/main/resources/application-example.properties`

## Testes

Para validar o backend:

```bash
cd constituicao-financeira-pessoal/pessoais
./mvnw test
```

## Deploy OCI

O projeto continua compatível com deploy em VM OCI e Docker Compose. Consulte os arquivos:

- `DEPLOY_OCI.md`
- `DEPLOY_OCI_VM_SIMPLES.md`
- `COMANDOS_OCI_VM.md`

## Observação

A base PF da Constituição Financeira Pessoal foi preservada e a arquitetura foi ampliada para receber o contexto empresarial de forma segura e escalável.