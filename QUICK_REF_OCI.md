# ⚡ QUICK REFERENCE - OCI Deploy em 1 Página

## 📍 VOCÊ ESTÁ AQUI
```
[ ] Preparação ← COMECE
[ ] Teste Local
[ ] Build & Push
[ ] OCI Setup
[ ] Deploy K8s
[ ] Validação
[ ] ✅ Sucesso!
```

---

## 🎯 Quick Access Links

| Preciso | Arquivo | Atalho |
|---------|---------|--------|
| **Começar** | LEITURA_RECOMENDADA.md | 1º |
| **Entender** | OCI_DEPLOYMENT_SUMMARY.md | 2º |
| **Ver Diagrama** | ARQUITETURA_DEPLOY.md | 3º |
| **Preparar** | PRE_DEPLOYMENT_CHECKLIST.md | 4º |
| **Deploy** | DEPLOY_OCI.md | 5º ⭐ |
| **Erro?** | TROUBLESHOOTING_OCI.md | Debug |
| **Comando?** | KUBECTL_CHEATSHEET.md | Busca |

---

## 🚀 Passos Principais (3 horas)

### 1️⃣ PREPARAR (0-30min)
```bash
# Pré-requisitos
✓ Java 17, Maven, Docker instalados
✓ .env criado (cp .env.example .env)
✓ Secrets gerados (openssl rand -base64 32)
✓ Conta OCI ativa
✓ OCI CLI configurado
```

### 2️⃣ TESTE LOCAL (30-60min)
```bash
docker-compose up -d
sleep 30
curl http://localhost:8080/api/health  # ✓ UP?
curl http://localhost:3000              # ✓ HTML?
docker-compose down
```

### 3️⃣ BUILD DOCKER (60-70min)
```bash
docker build -t financas-backend:latest pessoais/
docker tag financas-backend:latest REGISTRY/NAME:latest
docker push REGISTRY/NAME:latest
```

### 4️⃣ KUBERNETES DEPLOY (70-150min)
```bash
# Secrets
kubectl create secret generic financas-secrets \
  --from-literal=db-username=postgres \
  --from-literal=db-password=SENHA \
  --from-literal=jwt-secret=CHAVE \
  -n financas

# Apply
kubectl apply -f kubernetes.yaml

# Aguardar
kubectl get pods -n financas -w
```

### 5️⃣ VALIDAR (150-180min)
```bash
# Tudo rodando?
kubectl get all -n financas

# IP público?
kubectl get svc -n financas | grep LoadBalancer

# Teste
curl EXTERNAL_IP:80/api/health
```

---

## 📋 Variáveis Essenciais

```bash
# Gerar secrets (use output no .env)
openssl rand -base64 32      # JWT_SECRET
openssl rand -hex 16         # DB Password (adicionie 16+ chars)

# Em produção, NUNCA commitar .env no git!
echo ".env" >> .gitignore
```

---

## 🆘 SOS Commands

```bash
# Status geral
kubectl get all -n financas

# Logs do backend
kubectl logs -f deployment/backend -n financas

# Exec no container
kubectl exec -it deployment/backend -n financas -- sh

# Restart
kubectl rollout restart deployment/backend -n financas

# Port-forward (testar localmente)
kubectl port-forward svc/backend-service 8080:8080 -n financas

# Ver metricas
kubectl top pods -n financas

# Descrever pod com problema
kubectl describe pod NOME -n financas
```

---

## ⚠️ Problemas Comuns

| Erro | Solução |
|------|---------|
| Docker login fail | Verificar auth token no OCI Console |
| Pod CrashLoopBackOff | `kubectl logs POD -n financas` (veja TROUBLESHOOTING) |
| LoadBalancer Pending | Aguardar ~5 min (subnet pública?) |
| DB Connection Refused | Verificar security groups, endpoint URL |
| CORS erro | Atualizar `app.cors.allowed-origins` |

**Mais problemas?** → Veja `TROUBLESHOOTING_OCI.md` (10 soluções)

---

## 📊 Arquivos Importantes

```
Backend Config:
  └─ application-prod.properties (variáveis env)

Frontend Config:
  └─ settings-production.json (URLs prod)

K8s Manifest:
  └─ kubernetes.yaml (TODO em um arquivo!)

Environment:
  └─ .env (NUNCA commitar!)

Docker:
  ├─ Dockerfile
  ├─ docker-compose.yml
  └─ nginx.conf
```

---

## ✅ Success Checklist

```bash
# Aplicar cada:
[ ] kubectl get pods -n financas        # Todos "Running"?
[ ] kubectl get svc -n financas         # LoadBalancer tem IP?
[ ] curl backend-IP:8080/api/health     # {"status":"UP"}?
[ ] curl frontend-IP:80                 # HTML da home?
[ ] kubectl logs deployment/backend     # Sem erros críticos?
[ ] kubectl describe pod POD            # Tudo OK?

# Se TODOS ✓ → SUCESSO! 🎉
```

---

## 📞 Referências Rápidas

```bash
# Kubernetes cheat sheet
https://kubernetes.io/docs/reference/kubectl/

# OCI CLI reference
oci --help | grep [topico]

# Spring Boot Actuator endpoints
/api/health                  # Health check
/api/actuator/metrics        # Métricas
/api/actuator/env            # Configurações
```

---

## 🔄 Ciclo de Atualização (após 1º deploy)

```bash
# 1. Fazer mudanças locais
# 2. Testar: docker-compose up
# 3. Build nova imagem
docker build -t financas-backend:v1.1 pessoais/
docker tag financas ...:latest
docker push ...

# 4. Atualizar deployment
kubectl set image deployment/backend \
  backend=REGISTRY/NAME:latest -n financas

# 5. Monitorar rollout
kubectl rollout status deployment/backend -n financas -w
```

---

## ⏱️ Tempos Esperados

| Fase | Tempo |
|------|-------|
| Ler docs | 40 min |
| Setup local | 15 min |
| Docker local | 20 min |
| OCI setup | 30 min |
| Build/push | 10 min |
| K8s deploy | 15 min |
| Validação | 20 min |
| **TOTAL** | **3h** |

---

## 🎓 Primeiro Comando: DEPLOY_OCI.md

Abra agora: **DEPLOY_OCI.md** Passo 1

Siga cada linha com cuidado.

Em dúvida? Consulte **TROUBLESHOOTING_OCI.md**

---

## 📱 Rápido Status Check (Cola em terminal)

```bash
# Copie e cole para ver status geral:
echo "=== PODS ===" && kubectl get pods -n financas && \
echo "" && echo "=== SERVICES ===" && kubectl get svc -n financas && \
echo "" && echo "=== HPA ===" && kubectl get hpa -n financas && \
echo "" && echo "=== EVENTS ===" && kubectl get events -n financas --sort-by='.lastTimestamp' | tail -5
```

---

**PRONTO? Abra `DEPLOY_OCI.md` e comece! 🚀**

Boa sorte! 💪

