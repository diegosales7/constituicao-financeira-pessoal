# Sistema de Ativos Financeiros - Documentação

## Visão Geral
O novo sistema de ativos permite que você cadastre, edite, visualize e delete seus investimentos organizados por categoria e subcategoria.

## Categorias e Subcategorias Disponíveis

### 1. Renda Fixa
Investimentos com rendimento previsível e menor risco:
- **Tesouro Direto**: Títulos emitidos pelo Governo Federal
- **CDB**: Certificado de Depósito Bancário emitido por instituições financeiras

### 2. Renda Variável
Investimentos com maior potencial de ganho e variabilidade:
- **ETF**: Exchange Traded Fund - Fundos negociados em bolsa que acompanham índices
- **Fundo Imobiliário**: Fundos que investem em imóveis e geram renda através de aluguel

## Funcionalidades

### Adicionar um Ativo
1. Clique em "+ Adicionar Ativo"
2. Selecione a categoria (Renda Fixa ou Renda Variável)
3. Escolha a subcategoria
4. Preencha o valor do aporte
5. Selecione a data do investimento
6. (Opcional) Adicione uma descrição
7. Clique em "Salvar Ativo"

### Visualizar Ativos
Os ativos são organizados em duas seções:
- **Renda Fixa**: Mostra todos os investimentos em títulos de baixo risco
- **Renda Variável**: Mostra todos os fundos e ETFs

Cada ativo exibe:
- Tipo (subcategoria)
- Categoria
- Data do investimento
- Valor investido
- Descrição (se houver)

### Editar um Ativo
1. Clique no botão "Editar" no cartão do ativo
2. Modify os dados desejados
3. Clique em "Salvar Ativo"

### Deletar um Ativo
1. Clique no botão "Deletar" no cartão do ativo
2. Confirme a exclusão

### Resumo de Investimentos
Na seção "Resumo de Investimentos", você verá:
- **Total Investido**: Soma de todos os seus ativos
- **Renda Fixa**: Total investido em renda fixa
- **Renda Variável**: Total investido em renda variável

## Integração com Simulação
Após configurar seus ativos, quando você simular uma renda na seção de Constituição Financeira, o sistema pode recomendar alocações baseadas em:
- Percentuais configurados (investimento, reserva, impostos)
- Seus ativos cadastrados
- Seu histórico de investimentos

## Endpoints da API

### Criar Ativo
```
POST /ativos/criar
Authorization: Bearer {token}
Content-Type: application/json

{
  "category": "RENDA_FIXA",
  "subcategory": "TESOURO_DIRETO",
  "investmentValue": 1000.00,
  "investmentDate": "2026-04-26T00:00:00",
  "description": "Investimento em Tesouro SELIC"
}
```

### Listar Meus Ativos
```
GET /ativos/meus-ativos
Authorization: Bearer {token}
```

### Listar Ativos por Categoria
```
GET /ativos/por-categoria?category=RENDA_FIXA
Authorization: Bearer {token}
```

### Atualizar Ativo
```
PUT /ativos/atualizar/{assetId}
Authorization: Bearer {token}
Content-Type: application/json

{
  "category": "RENDA_FIXA",
  "subcategory": "CDB",
  "investmentValue": 1500.00,
  "investmentDate": "2026-04-26T00:00:00",
  "description": "CDB com rendimento progressivo"
}
```

### Deletar Ativo
```
DELETE /ativos/deletar/{assetId}
Authorization: Bearer {token}
```

## Fluxo Recomendado do Usuário

1. **Login**: Acesse com seu email e senha
2. **Preencher Perfil Pessoal**: Na página de Perfil, preencha seus dados pessoais
3. **Configurar Constituição**: Configure os percentuais de investimento, reserva e impostos
4. **Cadastrar Ativos**: Vá para a página de Ativos e cadastre seus investimentos existentes
5. **Simular Renda**: Volte para o Perfil e faça simulações de renda para ver como seria distribuído seu dinheiro
6. **Gerenciar Ativos**: Mude para a página de Ativos a qualquer momento para atualizar ou adicionar novos investimentos

## Notas Importantes

- Todos os valores devem ser em Reais (R$)
- A data do investimento não pode ser no futuro
- Você pode editar ou deletar seus ativos a qualquer momento
- Os ativos são pessoais e privados para sua conta

