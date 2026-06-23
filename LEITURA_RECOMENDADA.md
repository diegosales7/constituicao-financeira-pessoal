# 📚 Guia de Leitura - Documentação OCI

## 🚀 Comece Aqui (Ordem Recomendada)

### 1️⃣ **OCI_DEPLOYMENT_SUMMARY.md** (5 min)
   - **O quê:** Resumo executivo de tudo que foi criado
   - **Por quê:** Visão 10.000 pés do projeto
   - **Ideal para:** Entender o escopo total rapidamente
   - **Próximo:** IR para #2

### 2️⃣ **ARQUITETURA_DEPLOY.md** (10 min)
   - **O quê:** Diagrama visual da arquitetura OCI
   - **Por quê:** Entender como os componentes se conectam
   - **Ideal para:** Visão geral técnica
   - **Próximo:** Sim, pule para #3

### 3️⃣ **PRE_DEPLOYMENT_CHECKLIST.md** (15 min)
   - **O quê:** Checklist antes de começar deploy
   - **Por quê:** Garantir que tudo está em ordem
   - **Ideal para:** Preparação e validação
   - **Próximo:** Sim, confirme todos os pontos

### 4️⃣ **DEPLOY_OCI.md** (30 min + implementação)
   - **O quê:** Guia passo-a-passo completo de deploy
   - **Por quê:** Instruções detalhadas para cada etapa
   - **Ideal para:** Executar o deploy
   - **Próximo:** Seguir exatamente cada passo
   - **⚠️ PRINCIPAL:** Este é o documento que você vai seguir

### 5️⃣ **KUBECTL_CHEATSHEET.md** (Consulta)
   - **O quê:** Referência rápida de comandos Kubernetes
   - **Por quê:** Comandos para gerenciar deploy
   - **Ideal para:** Lookup durante operações
   - **Próximo:** Use conforme necessário durante deploy

### 6️⃣ **TROUBLESHOOTING_OCI.md** (Conforme necessário)
   - **O quê:** Solução de 10 problemas mais comuns
   - **Por quê:** Debug quando algo der errado
   - **Ideal para:** Durante deploy se houver erros
   - **Próximo:** Leia a seção específica do problema

---

## 📖 Mapa Mental da Documentação

```
START
  │
  ├─→ OCI_DEPLOYMENT_SUMMARY.md (Visão Geral)
  │     │
  │     ├─→ "Quais arquivos foram criados?"
  │     ├─→ "Qual é a estrutura?"
  │     └─→ "Quanto tempo leva?"
  │
  ├─→ ARQUITETURA_DEPLOY.md (Diagrama)
  │     │
  │     ├─→ "Como os componentes se conectam?"
  │     ├─→ "Qual é a segurança?"
  │     └─→ "Como escalar?"
  │
  ├─→ PRE_DEPLOYMENT_CHECKLIST.md (Preparação)
  │     │
  │     ├─ Local (Docker, Java, Maven)
  │     ├─ OCI (Conta, Recursos)
  │     ├─ Secrets (Gerar chaves)
  │     └─ ✅ Pronto?
  │
  └─→ DEPLOY_OCI.md (EXECUTE AQUI!)
        │
        ├─ Passo 1: Ambiente Local
        ├─ Passo 2: Docker Local
        ├─ Passo 3: Recursos OCI
        ├─ Passo 4: Security
        ├─ Passo 5: Deploy Options
        │    ├─ Opção A: Compute Instance
        │    └─ Opção B: Kubernetes (OKE) ⭐ Recomendado
        ├─ Passo 6: CI/CD
        └─ 🎉 Sucesso!

        Se erro → TROUBLESHOOTING_OCI.md
        Comandos → KUBECTL_CHEATSHEET.md
```

---

## ⏰ Timeline de Leitura vs Implementação

| Documento | Leitura | Implementação | Total |
|-----------|---------|---------------|-------|
| OCI_DEPLOYMENT_SUMMARY | 5 min | - | 5 min |
| ARQUITETURA_DEPLOY | 10 min | - | 10 min |
| PRE_DEPLOYMENT_CHECKLIST | 15 min | 20-30 min | 35-45 min |
| DEPLOY_OCI | 30 min | 1-2 horas | 1.5-2.5 horas |
| KUBECTL_CHEATSHEET | 5 min (lookup) | Conforme necessário | - |
| TROUBLESHOOTING_OCI | 5 min/seção | Conforme necessário | - |
| **TOTAL** | ~65 min | ~2-3 horas | **2-3.5 horas** |

---

## 🎯 Casos de Uso Específicos

### "Quero entender o projeto rapidamente"
1. OCI_DEPLOYMENT_SUMMARY.md
2. ARQUITETURA_DEPLOY.md
3. **Pronto!** (~15 min)

### "Vou fazer o deploy agora"
1. PRE_DEPLOYMENT_CHECKLIST.md → verificar tudo
2. DEPLOY_OCI.md → seguir passo a passo
3. KUBECTL_CHEATSHEET.md → aberto como referência
4. TROUBLESHOOTING_OCI.md → se houver problema
5. **Pronto!** (~2-3 horas)

### "Algo deu errado"
1. TROUBLESHOOTING_OCI.md → encontre seu problema
2. KUBECTL_CHEATSHEET.md → execute comandos debug
3. DEPLOY_OCI.md → volta ao passo problemático
4. **Corrigido!**

### "Preciso gerenciar a aplicação em produção"
1. KUBECTL_CHEATSHEET.md → seu melhor amigo
2. ARQUITETURA_DEPLOY.md → referência
3. Comandos de: `kubectl`, `oci`, `docker`
4. **Operacional!**

### "Preciso adicionar features/mudar config"
1. ARQUITETURA_DEPLOY.md → entender sistema
2. DEPLOY_OCI.md → Passo 4+ → modificações
3. Atualizar kubernetes.yaml conforme necessário
4. **Deploy** → DEPLOY_OCI.md Passo 5-6

---

## 📊 Documentação por Arquivo

### Arquivos de Configuração
| Arquivo | Descrição | Tipo | Editar? |
|---------|-----------|------|---------|
| `application-prod.properties` | Backend config produção | Properties | ✅ User vars |
| `settings-production.json` | Frontend config produção | JSON | ✅ User vars |
| `.env.example` | Variáveis de ambiente | Shell | ✅ IMPORTANTE |
| `kubernetes.yaml` | Deployment Kubernetes | YAML | ⚠️ Cuidado |
| `docker-compose.yml` | Local development | Docker | ✅ OK |
| `nginx.conf` | Nginx reverso proxy | Conf | ⚠️ Cuidado |
| `Dockerfile` | Container image | Docker | ❌ Não |

### Arquivos de Documentação (Leia!)
| Arquivo | Tamanho | Leitura | Quando |
|---------|---------|--------|--------|
| OCI_DEPLOYMENT_SUMMARY | 4KB | 5 min | PRIMEIRO |
| ARQUITETURA_DEPLOY | 6KB | 10 min | 2º |
| PRE_DEPLOYMENT_CHECKLIST | 12KB | 15 min | ANTES deploy |
| DEPLOY_OCI | 15KB | 30 min | DURANTE deploy |
| KUBECTL_CHEATSHEET | 8KB | 5 min | Lookup |
| TROUBLESHOOTING_OCI | 10KB | 5 min/seção | Se erro |
| Este arquivo | 4KB | 5 min | Agora |

---

## 🔗 Relações Entre Documentos

```
PRE_DEPLOYMENT_CHECKLIST
    ↓ (Confirme tudo ✓)
DEPLOY_OCI.md
    ├─ Passo 1-2: Docker local
    ├─ Passo 3-4: OCI setup (veja ARQUITETURA_DEPLOY)
    ├─ Passo 5: Deploy (veja kubernetes.yaml)
    ├─ Passo 6: CI/CD
    └─ Se erro → TROUBLESHOOTING_OCI.md
        └─ Se precisa comando → KUBECTL_CHEATSHEET.md
```

---

## 📋 Quick Links por Tópico

### Setup Inicial
- `PRE_DEPLOYMENT_CHECKLIST.md` → Seção "Verificações Locais"
- `DEPLOY_OCI.md` → Passo 1 + Passo 3

### Docker & Containerização
- `DEPLOY_OCI.md` → Passo 2
- `docker-compose.yml` → Exemplo local
- `TROUBLESHOOTING_OCI.md` → Seção "Docker Commands"

### OCI Infrastructure
- `DEPLOY_OCI.md` → Passo 3
- `ARQUITETURA_DEPLOY.md` → Seção "Componentes"
- `PRE_DEPLOYMENT_CHECKLIST.md` → Seção "OCI"

### Kubernetes Deploy
- `DEPLOY_OCI.md` → Passo 6 (OKE)
- `kubernetes.yaml` → Manifesto referência
- `KUBECTL_CHEATSHEET.md` → Todos os comandos K8s
- `TROUBLESHOOTING_OCI.md` → Seção "Kubernetes"

### Segurança
- `ARQUITETURA_DEPLOY.md` → Seção "Segurança"
- `DEPLOY_OCI.md` → Passo 4
- `PRE_DEPLOYMENT_CHECKLIST.md` → Seção "Vault & Secrets"

### Monitoring & Troubleshooting
- `ARQUITETURA_DEPLOY.md` → Seção "Monitoring"
- `TROUBLESHOOTING_OCI.md` → Todas as 10 seções
- `KUBECTL_CHEATSHEET.md` → Seção "Monitoring"

### Scaling & Production
- `ARQUITETURA_DEPLOY.md` → Seção "Auto-scaling"
- `kubernetes.yaml` → HorizontalPodAutoscaler
- `TROUBLESHOOTING_OCI.md` → Seção "Performance"

---

## 🆘 Preciso de Ajuda

1. **Qual documento devo ler?**
   - Veja "Casos de Uso Específicos" acima

2. **Tenho um erro específico**
   - Vá para `TROUBLESHOOTING_OCI.md`
   - Procure por palavra-chave (ex: "connection refused")

3. **Preciso de um comando Kubernetes**
   - Vá para `KUBECTL_CHEATSHEET.md`
   - Procure a seção (ex: "Logs", "Network")

4. **Entendo o projeto, vou começar do zero**
   - Comece com: `PRE_DEPLOYMENT_CHECKLIST.md`
   - Depois: `DEPLOY_OCI.md`

5. **Preciso fazer uma mudança"
   - Leia: `DEPLOY_OCI.md` Passo 5-6
   - Edite: `kubernetes.yaml` ou properties
   - Deploy novamente: `DEPLOY_OCI.md` Passo 5

---

## 🎓 Learning Path Recomendado

```
Iniciante ("Nunca usei OCI/K8s")
├─ OCI_DEPLOYMENT_SUMMARY.md (entender conceitos)
├─ ARQUITETURA_DEPLOY.md (ver diagrama)
├─ PRE_DEPLOYMENT_CHECKLIST.md (preparar)
└─ DEPLOY_OCI.md (executar com paciência)

Intermediário ("Já fiz deploy antes")
├─ ARQUITETURA_DEPLOY.md (review rápido)
├─ PRE_DEPLOYMENT_CHECKLIST.md (validar)
├─ DEPLOY_OCI.md (acelerado)
└─ KUBECTL_CHEATSHEET.md (reference rápida)

Avançado ("Sou DevOps/SRE")
├─ kubernetes.yaml (validar)
├─ TROUBLESHOOTING_OCI.md (edge cases)
├─ DEPLOY_OCI.md (Passo 6 - CI/CD)
└─ Customizar conforme necessário
```

---

## ✅ Confirmação de Leitura

Se você chegou aqui, você deveria ter lido:

- [ ] OCI_DEPLOYMENT_SUMMARY.md
- [ ] ARQUITETURA_DEPLOY.md
- [ ] PRE_DEPLOYMENT_CHECKLIST.md (validou pré-requisitos?)
- [ ] Este arquivo (GUIDE_DOCS.md)

**Próximo passo:** Abrir `DEPLOY_OCI.md` e começar Passo 1!

---

## 📞 Referências Externas

| Tópico | Link |
|--------|------|
| OCI Docs | https://docs.oracle.com/en/cloud/ |
| OKE Guide | https://docs.oracle.com/en-us/iaas/container-engine-kubernetes/ |
| Kubernetes | https://kubernetes.io/docs/reference/ |
| Spring Boot | https://docs.spring.io/spring-boot/ |
| PostgreSQL | https://www.postgresql.org/docs/ |

---

Está pronto? Abra **`DEPLOY_OCI.md`** e comece! 🚀

