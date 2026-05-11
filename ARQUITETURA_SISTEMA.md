# 🏗️ ARQUITETURA DO SISTEMA - VISÃO GERAL

## 📊 Fluxo de Dados

```
┌─────────────────────────────────────────────────────────────────┐
│                        FRONTEND (HTML/CSS/JS)                   │
├─┬──────────────────────────────────────────────────────────────┤
│ │                                                               │
│ │  index.html          dashboard.html      perfil.html        │
│ │  (Login)             (15 Dashboard)       (Perfil +          │
│ │    │                    │                 Constituição)       │
│ │    └────────────────────┴─────────────────┬────────────────┐  │
│ │                                          ativos.html       │  │
│ │                               (Gerenciamento de Ativos)    │  │
│ └──────────────────────────────────────────────────────────────┘
│                          ↓ (API Calls via Fetch)
├─────────────────────────────────────────────────────────────────┤
│                    SPRING BOOT BACKEND (PORT 10000)            │
├──────────────────────────────────────────────────────────────
│                      CONTROLLERS                               │
├────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌──────────────────┐  ┌──────────────────┐                    │
│  │  AuthController  │  │PerfilFinanceiro │                    │
│  │                  │  │    Controller    │                    │
│  │ /auth/login      │  │                  │                    │
│  │ /auth/register   │  │ /perfil/setup    │                    │
│  │ /auth/logout     │  │ /perfil/simular  │                    │
│  └──────────────────┴──┴──────────────────┘                    │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │           AssetController                                │  │
│  │                                                          │  │
│  │  /ativos/criar          (POST)                          │  │
│  │  /ativos/meus-ativos    (GET)                           │  │
│  │  /ativos/por-categoria  (GET)                           │  │
│  │  /ativos/atualizar/{id} (PUT)                           │  │
│  │  /ativos/deletar/{id}   (DELETE)                        │  │
│  └──────────────────────────────────────────────────────────┘  │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │    MotivationalPhraseController (NOVO)                  │  │
│  │                                                          │  │
│  │  /frases/aleatoria             (GET)                    │  │
│  │  /frases/categoria/{category}  (GET)                    │  │
│  │  /frases/categorias            (GET)                    │  │
│  └──────────────────────────────────────────────────────────┘  │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │      ExchangeRateController (NOVO - API EXTERNA)        │  │
│  │                                                          │  │
│  │  /cambio/taxas                    (GET)                 │  │
│  │  /cambio/converter                (GET)                 │  │
│  │  /cambio/moedas-suportadas        (GET)                 │  │
│  │                                                          │  │
│  │  🔗 API Externa: exchangerate-api.com                   │  │
│  └──────────────────────────────────────────────────────────┘  │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │   FinancialAnalysisController (NOVO)                    │  │
│  │                                                          │  │
│  │  /analise/saude-financeira   (GET - AUTENTICADO)       │  │
│  └──────────────────────────────────────────────────────────┘  │
│                                                                 │
├─────────────────────────────────────────────────────────────────┤
│                       SERVICES (Lógica)                         │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  AuthService              AssetService                         │
│  FinancialConstitution    MotivationalPhraseService           │
│  UserService              ExchangeRateService (API)           │
│  FinancialAnalysisService                                      │
│                                                                 │
├─────────────────────────────────────────────────────────────────┤
│                   REPOSITORIES (Acesso a Dados)                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  UserRepository           AssetRepository                      │
│  FinancialProfileRepository                                    │
│  AccountRepository                                             │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
                          ↓ (SQL Queries)
                    POSTGRESQL DATABASE
                    (localhost:5432)
```

---

## 🗄️ Estrutura do Banco de Dados

```
financas_pessoais
│
├── users
│   ├── id (PK)
│   ├── email (UNIQUE)
│   ├── password
│   ├── role
│   ├── first_name
│   ├── last_name
│   ├── age
│   ├── patrimonio
│   ├── profissao
│   ├── cidade
│   ├── estado
│   ├── meta_anual
│   └── renda_mensal
│
├── financial_profile
│   ├── id (PK)
│   ├── user_id (FK)
│   ├── investment_percent
│   ├── reserve_percent
│   ├── tax_percent
│   ├── trigger1
│   ├── trigger2
│   ├── emergency_mode
│   ├── created_at
│   └── updated_at
│
├── assets
│   ├── id (PK)
│   ├── user_id (FK)
│   ├── category (RENDA_FIXA | RENDA_VARIAVEL)
│   ├── subcategory (TESOURO_DIRETO | CDB | ETF | FUNDO_IMOBILIARIO)
│   ├── investment_value
│   ├── investment_date
│   ├── description
│   ├── created_at
│   └── updated_at
│
└── accounts (antiga, ainda existe)
    ├── id (PK)
    ├── user_id (FK)
    └── saldo
```

---

## 🔐 Fluxo de Autenticação

```
┌──────────────────┐
│  Usuário acessa  │
│   index.html     │
└────────┬─────────┘
         │
         ↓
┌──────────────────────┐
│  Login / Registro    │
│  (email + senha)     │
└────────┬─────────────┘
         │
         ↓
┌──────────────────────────┐
│ AuthController           │
│ /auth/login ou /register │
└────────┬─────────────────┘
         │
         ↓
┌──────────────────────────┐
│ AuthService              │
│ - Valida credenciais     │
│ - Cria/verifica usuário  │
│ - Gera JWT Token         │
└────────┬─────────────────┘
         │
         ↓
┌──────────────────────────┐
│ Token armazenado         │
│ localStorage.setItem     │
│ ("token")                │
└────────┬─────────────────┘
         │
         ↓
┌──────────────────────────┐
│ Redireciona para         │
│ dashboard.html           │
└────────┬─────────────────┘
         │
         ↓
┌──────────────────────────┐
│ Token em cada request    │
│ Authorization: Bearer    │
└────────┬─────────────────┘
         │
         ↓
┌──────────────────────────┐
│ JwtAuthFilter valida     │
│ - Extrai token           │
│ - Valida assinatura      │
│ - Define usuário         │
└──────────────────────────┘
```

---

## 📱 Jornada do Usuário

```
┌───────────────┐
│  1. LOGIN     │
│ ou REGISTRO   │
└───────┬───────┘
        │
        ↓
┌──────────────────────┐
│ 2. DASHBOARD         │
│ - Frase inspiradora  │
│ - Cotações de moedas │
│ - Saúde financeira   │
└───────┬──────────────┘
        │
        ↓
┌──────────────────────────┐
│ 3. PERFIL                │
│ - Dados pessoais         │
│ - Dados financeiros      │
│ - Constituição (setup)   │
└───────┬──────────────────┘
        │
        ↓
┌──────────────────────────┐
│ 4. ATIVOS                │
│ - Cadastrar investimentos│
│ - Visualizar por tipo    │
│ - Editar/Deletar         │
└───────┬──────────────────┘
        │
        ↓
┌──────────────────────────┐
│ 5. SIMULAR               │
│ - Inserir renda          │
│ - Ver distribuição       │
│ - Comparar com ativos    │
└───────┬──────────────────┘
        │
        ↓
┌──────────────────────────┐
│ 6. ANÁLISE               │
│ - Score financeiro       │
│ - Recomendações         │
│ - Próximo marco          │
└──────────────────────────┘
```

---

## 🔄 Ciclo de Dados de um Ativo

```
┌──────────────────────────────────┐
│  Usuário em ativos.html          │
│  Clica em "+ Adicionar Ativo"    │
└──────────┬───────────────────────┘
           │
           ↓
┌──────────────────────────────────┐
│  Preenche formulário             │
│  - Categoria                     │
│  - Subcategoria                  │
│  - Valor                         │
│  - Data                          │
│  - Descrição (opcional)          │
└──────────┬───────────────────────┘
           │
           ↓
┌──────────────────────────────────┐
│  Clica "Salvar Ativo"            │
│  POST /ativos/criar              │
└──────────┬───────────────────────┘
           │
           ↓
┌──────────────────────────────────┐
│  AssetController recebe          │
│  - Valida autenticação (JWT)     │
│  - Extrai usuário do token       │
└──────────┬───────────────────────┘
           │
           ↓
┌──────────────────────────────────┐
│  AssetService.createAsset()      │
│  - Cria objeto Asset             │
│  - Define timestamps             │
│  - Salva no DB                   │
└──────────┬───────────────────────┘
           │
           ↓
┌──────────────────────────────────┐
│  AssetRepository.save()          │
│  INSERT INTO assets ...          │
└──────────┬───────────────────────┘
           │
           ↓
┌──────────────────────────────────┐
│  Retorna AssetResponse (JSON)    │
│  HTTP 200 OK                     │
└──────────┬───────────────────────┘
           │
           ↓
┌──────────────────────────────────┐
│  Frontend recebe resposta        │
│  Mostra mensagem de sucesso      │
│  Recarrega lista de ativos       │
│  GET /ativos/meus-ativos         │
└──────────┬───────────────────────┘
           │
           ↓
┌──────────────────────────────────┐
│  AssetService.getAssetsByUser()  │
│  SELECT * FROM assets WHERE...   │
└──────────┬───────────────────────┘
           │
           ↓
┌──────────────────────────────────┐
│  Frontend exibe ativos em cards  │
│  Organize por categoria          │
│  Calcula totais                  │
└──────────────────────────────────┘
```

---

## 🌐 API Externa - Câmbio

```
┌────────────────────────────────┐
│ Dashboard.html carrega         │
│ Clica em "Cotações de Moedas"  │
└────────┬───────────────────────┘
         │
         ↓
┌────────────────────────────────┐
│ Chama GET /cambio/taxas        │
└────────┬───────────────────────┘
         │
         ↓
┌────────────────────────────────┐
│ ExchangeRateController         │
│ Chama ExchangeRateService      │
└────────┬───────────────────────┘
         │
         ↓
┌────────────────────────────────┐
│ ExchangeRateService.           │
│ getExchangeRates()             │
│                                │
│ try {                          │
│   Chama API externa:           │
│   https://exchangerate-api.    │
│   com/v4/latest/BRL            │
│ } catch {                      │
│   Retorna valores padrão       │
│ }                              │
└────────┬───────────────────────┘
         │
         ↓
┌────────────────────────────────┐
│ ExchangeRateResponse com taxas │
│ (7 moedas principais)          │
└────────┬───────────────────────┘
         │
         ↓
┌────────────────────────────────┐
│ Frontend exibe em cards        │
│ Mostra taxas de câmbio         │
│                                │
│ Usuario pode converter:        │
│ R$ 1000 → USD 200              │
└────────────────────────────────┘
```

---

## 📊 Análise Financeira

```
┌─────────────────────────────────┐
│ Dashboard.html carrega          │
│ Clica em "Saúde Financeira"     │
└────────┬────────────────────────┘
         │
         ↓
┌─────────────────────────────────┐
│ FinancialAnalysisController     │
│ GET /analise/saude-financeira   │
└────────┬────────────────────────┘
         │
         ↓
┌──────────────────────────────────────────┐
│ FinancialAnalysisService.                │
│ analyzeFinancialHealth(user)             │
│                                          │
│ 1. Busca dados do usuário               │
│    - patrimonio                         │
│    - rendaMensal                        │
│    - metaAnual                          │
│                                          │
│ 2. Busca investimentos                  │
│    SELECT FROM assets WHERE user_id     │
│                                          │
│ 3. Calcula métricas                     │
│    - Taxa de poupança                   │
│    - Fundo de emergência                │
│    - Score de saúde (0-100)             │
│                                          │
│ 4. Gera recomendações                   │
│    - Baseadas em dados reais             │
│    - Personalizadas                     │
│                                          │
│ 5. Define próximo marco                 │
│    - Próximo objetivo financeiro        │
└──────────┬───────────────────────────────┘
           │
           ↓
┌──────────────────────────────────┐
│ FinancialAnalysisResponse        │
│ - totalPatrimony: 50000          │
│ - totalAssets: 15000             │
│ - monthlyIncome: 5000            │
│ - healthScore: "BOA"             │
│ - recommendations: [...]         │
│ - nextMilestone: "..."           │
└──────────┬───────────────────────┘
           │
           ↓
┌──────────────────────────────────┐
│ Frontend exibe:                  │
│ - Badge com score                │
│ - Métricas em cards              │
│ - Lista de recomendações         │
│ - Próximo marco personalizado    │
└──────────────────────────────────┘
```

---

## ✨ Destaques Técnicos

### Security
- ✅ JWT para autenticação stateless
- ✅ BCrypt para hash de senhas
- ✅ CORS configurado
- ✅ JwtAuthFilter validando todo request autenticado

### Performance
- ✅ BigDecimal para precisão monetária
- ✅ Lazy loading em relacionamentos
- ✅ Índices no banco para queries rápidas
- ✅ Cache de frases em memória

### Reliability
- ✅ Fallback para API de câmbio
- ✅ Tratamento de exceções
- ✅ Validações em duas camadas (backend/frontend)
- ✅ Transações ACID no banco

### Usability
- ✅ Interface intuitiva e responsiva
- ✅ Mensagens de erro claras
- ✅ Alerts de sucesso
- ✅ Carregamento assíncrono

---

**Sistema completamente funcional e pronto para produção!** 🚀

