# 🔐 Guia de Segurança e Segmentação de Dados por Usuário

## Visão Geral

O sistema **Constituição Financeira Pessoal** foi desenvolvido com segurança em mente, garantindo que **cada usuário tenha acesso exclusivamente aos seus próprios dados**. Nenhum usuário pode visualizar, modificar ou acessar dados de outro usuário.

---

## 🛡️ Camadas de Proteção

### 1. **Autenticação (Front-end)**

**Arquivo:** `js/auth.js`, `js/sessao.js`

```javascript
// Cada usuário tem seus dados armazenados localmente
localStorage.setItem("usuarioLogado", JSON.stringify({
  nome: resposta.nome,
  email: resposta.email,
  idade: resposta.idade
}));

// Proteção: páginas protegidas verificam se usuário está logado
function protegerPagina() {
  const usuario = obterUsuarioLogado();
  if (!usuario) {
    window.location.href = "index.html"; // Redireciona para login
  }
}
```

**Como funciona:**
- Ao fazer login/cadastro, o sistema armazena o usuário no `localStorage`
- Cada página protegida valida se há usuário logado
- Se não houver, redireciona para login
- Ao fazer logout, remove os dados do localStorage

---

### 2. **Autenticação (Back-end)**

**Arquivo:** `src/main/java/com/financas/pessoais/controller/AuthController.java`

```java
@PostMapping("/login")
public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
    AuthResponse authResponse = service.login(request);
    // Token JWT gerado e retornado
    addJwtToCookie(authResponse.getToken(), response);
    return ResponseEntity.ok(authResponse);
}
```

**Como funciona:**
- Login valida credenciais no banco de dados
- Sistema gera um **JWT Token** único por sessão
- Token é armazenado em cookie HTTP-only (seguro contra XSS)
- Token tem validade de 24 horas

---

### 3. **Segmentação de Dados por Endpoint**

**Arquivo:** Todos os controllers (ex: `TransacaoController.java`)

```java
@GetMapping
public ResponseEntity<List<TransacaoResponse>> listar(
    @AuthenticationPrincipal UserDetails userDetails  // ✅ Valida token JWT
) {
    User user = userService.findByEmail(userDetails.getUsername());
    return ResponseEntity.ok(transacaoService.listarTodas(user));  // ✅ Filtra por usuário
}
```

**Como funciona:**
1. **`@AuthenticationPrincipal`** valida que o token JWT é válido
2. Extrai o email do usuário autenticado
3. Busca o User correspondente no banco de dados
4. Todas as queries são filtradas por esse usuário
5. Um usuário logado como "joao@email.com" **não consegue acessar dados de maria@email.com**

---

### 4. **Banco de Dados (PostgreSQL)**

**Relacionamento de Chaves Estrangeiras:**

```sql
-- Tabela de usuários
CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  email VARCHAR(255) UNIQUE NOT NULL,
  nome VARCHAR(255) NOT NULL,
  ...
);

-- Tabela de transações (exemplo)
CREATE TABLE transacoes (
  id SERIAL PRIMARY KEY,
  usuario_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  descricao VARCHAR(255),
  valor DECIMAL,
  ...
);
```

**Como funciona:**
- Toda transação, receita, despesa, investimento, etc. é vinculada a um `usuario_id`
- Queries sempre filtram por `usuario_id` do usuário logado
- Um usuário não pode referenciar dados de outro usuário

---

## 🔑 Fluxo de Segurança (Passo a Passo)

### Cenário: João tenta acessar dados de Maria

```plaintext
1. João faz login
   ↓
2. Sistema gera JWT Token para João
   ↓
3. João tenta acessar: GET /transacoes
   ↓
4. Sistema valida token JWT
   ↓
5. Extrai email de João do token
   ↓
6. Query: SELECT * FROM transacoes WHERE usuario_id = :joao_id
   ↓
7. Retorna APENAS transações de João
   ↓
8. João não consegue acessar dados de Maria nem que tente manipular requests
```

---

## 🚨 Problemas que o Sistema **PREVINE**

### ❌ Acesso não autorizado
```
joao@email.com NÃO pode fazer:
- GET /transacoes de maria@email.com
- POST /investimentos para maria@email.com
- DELETE /despesas de maria@email.com
```

**Proteção:** Token JWT contém email do usuário

---

### ❌ Manipulação de requests
```
Mesmo que João edite o request HTTP para:
{
  "usuario_id": 5  // ID de Maria
  "valor": 1000
}

O servidor IGNORA esse campo e usa o usuário do token.
```

**Proteção:** Back-end extrai usuário do token, não do request

---

### ❌ Roubo de sessão
```
Alguém rouba a cookie de João
↓
Tenta usar em outro navegador
↓
Token expirou (24 horas) ou IP é diferente
```

**Proteção:** JWT com tempo de expiração curto

---

## 📋 Checklist de Segurança

- ✅ Cada usuário tem dados isolados no banco
- ✅ Endpoints validam autenticação via JWT
- ✅ Queries filtram automaticamente por usuário logado
- ✅ Senhas armazenadas com hash (bcrypt)
- ✅ Tokens JWT com expiração
- ✅ Cookies HTTP-only impedem XSS
- ✅ CORS configurado para front-end específico
- ✅ Logout remove cookie e invalida sessão
- ✅ Páginas protegidas verificam autenticação
- ✅ Onboarding não reutiliza dados entre usuários

---

## 🎯 Dados Segmentados por Usuário

Cada um desses dados está **100% isolado** por usuário:

| Dados | Armazenamento | Proteção |
|-------|---|---|
| Receitas | Tabela `receitas` | usuario_id |
| Despesas | Tabela `despesas` | usuario_id |
| Investimentos | Tabela `investimentos` | usuario_id |
| Cartão de Crédito | Tabela `cartao_credito` | usuario_id |
| Reserva Flex | Tabela `reserva_flex` | usuario_id |
| Movimentações | Tabela `movimentacoes` | usuario_id |
| Ocorrências | Tabela `ocorrencias` | usuario_id |
| Configurações | Tabela `configuracao` | usuario_id |
| Termômetro | Tabela `termometro` | usuario_id |

---

## 📱 Frontend: Armazenamento Seguro

O front-end armazena **APENAS** informações básicas do usuário:

```javascript
// Armazenado no localStorage
{
  "nome": "João Silva",
  "email": "joao@email.com",
  "idade": 30
}
```

**Dados sensíveis (receitas, despesas, etc.) NUNCA são armazenados localmente.**

---

## 🔄 Verificação em Tempo Real

### Ao cada requisição:

1. ✅ Valida JWT Token
2. ✅ Extrai email do usuário
3. ✅ Valida que email existe no banco
4. ✅ Filtra dados por usuário
5. ✅ Retorna APENAS dados daquele usuário

---

## 🛑 Logout e Limpeza

Quando usuário faz logout:

```javascript
function configurarSair() {
  logoutLinks.forEach(function(link) {
    link.addEventListener("click", function(event) {
      localStorage.removeItem("usuarioLogado"); // Remove dados locais
      window.location.href = "index.html";      // Redireciona para login
    });
  });
}
```

**Back-end:**
```java
@PostMapping("/logout")
public ResponseEntity<String> logout(HttpServletResponse response) {
    Cookie cookie = new Cookie("JWT_TOKEN", null);
    cookie.setMaxAge(0); // Remove o cookie
    response.addCookie(cookie);
    return ResponseEntity.ok("Logout realizado");
}
```

---

## 🔐 Boas Práticas Recomendadas

### Para Desenvolvedores

1. **Sempre use `@AuthenticationPrincipal`** para extrair usuário
2. **Nunca confie em IDs do request**, use o usuário do token
3. **Sempre filtre queries por usuário**
4. **Use relacionamentos com Foreign Keys** no banco
5. **Valide permissões** mesmo em dados privados

### Para Usuários

1. **Use senhas fortes** (mínimo 8 caracteres)
2. **Não compartilhe sua senha** com ninguém
3. **Faça logout quando terminar** de usar
4. **Use HTTPS em produção** para segurança total
5. **Verifique URL** antes de inserir dados sensíveis

---

## 🎓 Exemplo Completo: Uma Receita de João

```plaintext
1. João registra uma receita de R$ 5.000

   Front-end:
   POST /receitas HTTP/1.1
   Cookie: JWT_TOKEN=eyJhbGciOiJIUzI1NiIs...
   {
     "descricao": "Salário",
     "valor": 5000,
     "tipo": "FIXA"
   }

2. Back-end recebe a requisição

   @PostMapping("/receitas")
   public ResponseEntity<ReceitaResponse> criar(
       @AuthenticationPrincipal UserDetails userDetails,  // ← JWT Token
       @RequestBody CreateReceitaRequest request
   ) {
       // Extrai email de João do token
       User joao = userService.findByEmail(userDetails.getUsername());
       
       // Cria receita ASSOCIADA a João
       return ResponseEntity.ok(
           receitaService.criar(joao, request)
       );
   }

3. Banco de dados salva

   INSERT INTO receitas (usuario_id, descricao, valor, tipo)
   VALUES (1, 'Salário', 5000, 'FIXA');  ← usuario_id = 1 (João)

4. Maria (usuário_id = 2) tenta visualizar

   GET /receitas HTTP/1.1
   Cookie: JWT_TOKEN=eyJhbGciOiJIUzI1NiIi... (= Token de Maria)

5. Back-end filtra por Maria

   SELECT * FROM receitas WHERE usuario_id = 2;
   → Resultado vazio (receita de João não aparece)

6. Response para Maria

   {
     "receitas": []  // ← Vazio! Receita de João não é visível
   }
```

---

## 📞 Suporte

Para dúvidas sobre segurança:
- 📧 Email: seguranca@constituicaofinanceira.com
- 📚 Leia a documentação técnica
- 🔍 Verifique logs em caso de suspeita

---

**Resumo Final:** Cada usuário é 100% isolado em relação aos dados financeiros. O sistema utiliza JWT Tokens, validação de autenticação e filtros de banco de dados para garantir isso.

