# ✅ Checklist Pré-Deploy OCI

## 📋 Verificações Locais

### 🖥️ Ambiente de Desenvolvimento
- [ ] Java 17 instalado: `java -version`
- [ ] Maven 3.9+ instalado: `mvn -version`
- [ ] Docker instalado: `docker --version`
- [ ] Docker Compose instalado: `docker-compose --version`
- [ ] Git instalado: `git --version`
- [ ] Projeto clonado/extraído em um diretório

### 🔧 Projeto Backend
- [ ] `pom.xml` contém dependências corretas
- [ ] `CorsConfig.java` lê de properties de ambiente
- [ ] `application-prod.properties` criado
- [ ] `Dockerfile` existe em `pessoais/`
- [ ] `.dockerignore` existe

### 🎨 Projeto Frontend
- [ ] `settings-production.json` foi criado
- [ ] Todos os arquivos HTML existem
- [ ] CSS e JS carregam corretamente
- [ ] `nginx.conf` existe

### 🐘 Database
- [ ] PostgreSQL está rodando localmente (docker-compose)
- [ ] Migrations executadas
- [ ] Dados de teste inseridos (dados.sql)
- [ ] Conexão testada: `psql -h localhost -U postgres -d financas_pessoais`

### 🧪 Testes Locais
```bash
# Executar no projeto root: "Constituição Financeira Pessoal"

# 1. Build local
docker-compose up -d
# Aguarde ~30 segundos

# 2. Testar backend
curl http://localhost:8080/api/health
# Esperado: {"status":"UP"}

# 3. Testar frontend
curl -s http://localhost:3000 | head -20
# Deve retornar HTML do index.html

# 4. Testar database
docker-compose exec postgres psql -U postgres -c "SELECT version();"

# 5. Limpar
docker-compose down
```

---

## ☁️ Verificações OCI

### 📝 Conta & Setup
- [ ] Conta Oracle Cloud Infrastructure criada
- [ ] Free Tier ativo (se aplicável)
- [ ] OCI CLI instalado: `oci --version`
- [ ] OCI CLI configurado: `oci setup config`
- [ ] Acesso ao Console: https://console.oracle.com

### 🏢 Compartimento & Recursos
- [ ] Compartimento criado ou identificado
- [ ] Anotado o OCID do compartimento
- [ ] VCN criada com subnets públicas/privadas
- [ ] Internet Gateway conectado
- [ ] Route tables configuradas

### 🐳 Container Registry (OCIR)
- [ ] Registry acessível em: `seu-region.ocir.io`
- [ ] Namespace OCI anotado: `oci os ns get --query data`
- [ ] Auth Token gerado em OCI Console
- [ ] Docker login testado: 
  ```bash
  docker login -u sua-namespace/seu-usuario seu-registry.br.ocir.io
  ```

### ☸️ Kubernetes (OKE)
- [ ] Cluster OKE criado ou planejado
- [ ] Kubeconfig baixado: `oci ce cluster create-kubeconfig`
- [ ] Conexão testada: `kubectl cluster-info`
- [ ] Namespace `financas` pode ser criado
- [ ] Resource quotas suficientes

### 🗄️ Database
- [ ] PostgreSQL 16 será usado
- [ ] **Opção A (Recomendada):** OCI PostgreSQL Database Service
  - [ ] Service criado
  - [ ] Endpoint anotado
  - [ ] Firewall rules configuradas
  - [ ] Database `financas_pessoais` criada
  - [ ] usuário `postgres` e senha alterada
  
- **OU Opção B:** PostgreSQL em container (StatefulSet)
  - [ ] Storage provider disponível (OCI Block Volume)
  - [ ] PVC será criada automaticamente do manifest

### 🔐 Vault & Secrets
- [ ] OCI Vault criado com Master Key
- [ ] Secrets criados:
  - [ ] `db-password`
  - [ ] `jwt-secret` (32+ chars aleatório)
  - [ ] `api-keys` (se houver APIs externas)
- [ ] Policies configuradas para acesso

### 🌐 Networking
- [ ] Network Security Groups criados
- [ ] Rules para:
  - [ ] Port 8080 (backend interno)
  - [ ] Port 3000 (frontend interno)
  - [ ] Port 5432 (database)
  - [ ] Port 80/443 (LoadBalancer)
- [ ] VCN peering configurado (se necessário)

### 📊 Monitoring & Logs
- [ ] OCI Logging criado
- [ ] OCI Monitoring habilitado
- [ ] Namespaces de métricas definidos
- [ ] (Opcional) Alarmes pré-configuradas

---

## 📝 Variáveis de Ambiente

### ✏️ Preencher `.env.example`

```bash
# Copiar
cp .env.example .env

# Editar com seus valores:
```

```ini
# ===== DESENVOLVIMENTO =====
DB_URL=jdbc:postgresql://postgres:5432/financas_pessoais
DB_USERNAME=postgres
DB_PASSWORD=postgres123
JWT_SECRET=seu-segredo-de-teste-32-caracteres
CORS_ORIGINS=localhost:5500,localhost:63342

# ===== PRODUÇÃO (OCI) =====
DB_URL=jdbc:postgresql://seu-db-endpoint:5432/financas_pessoais
DB_USERNAME=postgres
DB_PASSWORD=MUDAR-PARA-SENHA-FORTE-32-CHARS
JWT_SECRET=GERAR-ALEATORIO-COM-OPENSSL-RAND
CORS_ORIGINS=https://seu-dominio.com,https://www.seu-dominio.com

# ===== OCI REGISTRY =====
OCI_REGISTRY_URL=seu-region.ocir.io
OCI_REGISTRY_USERNAME=seu-namespace/seu-usuario
OCI_REGISTRY_PASSWORD=seu-auth-token-completo

# ===== OKE KUBERNETES =====
OKE_CLUSTER_ID=seu-cluster-ocid
OKE_CLUSTER_NAME=financas-pessoais-cluster

# ===== OCI =====
OCI_COMPARTMENT_ID=seu-compartment-ocid
OCI_REGION=us-phoenix-1  # Alterar conforme sua região
```

---

## 🔑 Geração de Secrets

```bash
# Gerar JWT Secret (32+ caracteres)
openssl rand -base64 32

# Gerar Password forte (32 caracteres)
openssl rand -hex 16

# Salvar resultado em .env, não committar no Git!
```

---

## 📦 Arquivos Criados - Validação

Verificar se todos os arquivos abaixo existem:

```
✓ C:/.../.env.example
✓ C:/.../Dockerfile
✓ C:/.../docker-compose.yml
✓ C:/.../nginx.conf
✓ C:/.../kubernetes.yaml
✓ C:/.../DEPLOY_OCI.md
✓ C:/.../ARQUITETURA_DEPLOY.md
✓ C:/.../TROUBLESHOOTING_OCI.md
✓ C:/.../OCI_DEPLOYMENT_SUMMARY.md
✓ C:/.../KUBECTL_CHEATSHEET.md
✓ C:/.../deploy-oci.sh
✓ C:/.../deploy-oci.bat
✓ C:/.../deployment-checklist.md (este arquivo)
✓ C:/.../pessoais/src/main/resources/application-prod.properties
✓ C:/.../financas-frontend/settings-production.json
✓ C:/.../pessoais/pom.xml (atualizado com actuator)
✓ C:/.../pessoais/src/main/.../config/CorsConfig.java (atualizado)
```

---

## 🚀 Checklist de Deploy

### Fase 1: Build Local
```bash
cd "Constituição Financeira Pessoal"

# [ ] Verificar pré-requisitos
docker --version && docker-compose --version && maven --version

# [ ] Build Docker local
docker build -t financas-backend:local pessoais/

# [ ] Usar docker-compose para teste completo
docker-compose up -d
sleep 30

# [ ] Testar endpoints
curl http://localhost:8080/api/health
curl http://localhost:3000

# [ ] Verificar logs
docker-compose logs backend | tail -50

# [ ] Limpar se success
docker-compose down
```

### Fase 2: Setup OCI
```bash
# [ ] Login na OCI
oci session authenticate

# [ ] Verificar acesso
oci iam user list

# [ ] Setup OCI CLI
oci setup config

# [ ] Testar conectividade
oci os ns get --query data
```

### Fase 3: Build e Push Docker
```bash
# [ ] Carregar variáveis
export $(cat .env | grep -v '^#' | xargs)

# [ ] Build
docker build -t financas-backend:latest pessoais/

# [ ] Tag
docker tag financas-backend:latest ${OCI_REGISTRY_URL}/financas-backend:latest

# [ ] Login registry
docker login -u ${OCI_REGISTRY_USERNAME} ${OCI_REGISTRY_URL}

# [ ] Push
docker push ${OCI_REGISTRY_URL}/financas-backend:latest

# [ ] Verificar no registry
oci artifacts container images list --repository-name financas
```

### Fase 4: Preparar Kubernetes
```bash
# [ ] Conectar ao cluster OKE
oci ce cluster create-kubeconfig --cluster-id ${OKE_CLUSTER_ID}

# [ ] Validar conexão
kubectl cluster-info

# [ ] Criar namespace
kubectl create namespace financas

# [ ] Criar secrets
kubectl create secret generic financas-secrets \
  --from-literal=db-username=postgres \
  --from-literal=db-password=${DB_PASSWORD} \
  --from-literal=jwt-secret=${JWT_SECRET} \
  -n financas

# [ ] Validar secrets
kubectl get secrets -n financas
```

### Fase 5: Aplicar Manifesto Kubernetes
```bash
# [ ] Validar manifesto
kubectl apply -f kubernetes.yaml --dry-run=client -o yaml > manifest-validated.yaml

# [ ] Revisar manifesto validado
cat manifest-validated.yaml

# [ ] Aplicar
kubectl apply -f kubernetes.yaml

# [ ] Aguardar deployment
kubectl rollout status deployment/backend -n financas --timeout=5m

# [ ] Verificar pods
kubectl get pods -n financas -w

# [ ] Verificar services
kubectl get svc -n financas
```

### Fase 6: Validação Pós-Deploy
```bash
# [ ] Pods rodando
kubectl get pods -n financas | grep Running

# [ ] LoadBalancer tem IP
EXTERNAL_IP=$(kubectl get svc frontend-service -n financas -o jsonpath='{.status.loadBalancer.ingress[0].ip}')
echo "LoadBalancer IP: $EXTERNAL_IP"

# [ ] Testar backend
kubectl exec deployment/backend -n financas -- curl http://localhost:8080/api/health

# [ ] Testar database
kubectl exec deployment/backend -n financas -- psql -h postgres-service -U postgres -c "SELECT 1"

# [ ] Ver logs
kubectl logs -f deployment/backend -n financas

# [ ] Verificar metrics (após 5 min)
kubectl top pods -n financas
```

---

## 🆘 Troubleshooting

Se algo falhar, consulte:

1. **`TROUBLESHOOTING_OCI.md`** - Problemas conhecidos
2. **`KUBECTL_CHEATSHEET.md`** - Comandos úteis
3. **`DEPLOY_OCI.md`** - Detalhes de cada etapa
4. **Logs do pod:**
   ```bash
   kubectl logs -f deployment/backend -n financas
   ```
5. **Describe pod:**
   ```bash
   kubectl describe pod seu-pod-name -n financas
   ```

---

## ⏱️ Tempo Estimado

| Fase | Tempo |
|------|-------|
| Setup Local | 5 min |
| Teste Docker | 5 min |
| Setup OCI | 10 min |
| Build & Push | 5 min |
| OKE Setup | 15 min (paralelo) |
| Kubernetes Deploy | 5 min |
| Validação | 10 min |
| **TOTAL SEM OKE** | **~40 min** |
| **TOTAL COM OKE** | **~2-3 horas** |

OKE leva mais tempo na primeira criação (15-20 min), mas deploy após é rápido.

---

## 📞 Próxima Etapa

Quando este checklist estiver 100% completo:

1. Iniciar com `DEPLOY_OCI.md` (Passo 1)
2. Seguir instrções passo a passo
3. Consultar `TROUBLESHOOTING_OCI.md` se houver erros
4. Usar `KUBECTL_CHEATSHEET.md` para comandos

---

## 🎉 Success Criteria

Deploy é considerado **bem-sucedido** quando:

✅ Todos os pods estão em estado `Running`
✅ LoadBalancer tem um IP público atribuído
✅ `curl http://ip-loadbalancer` retorna página frontend
✅ Backend healthcheck responde OK
✅ Database está acessível
✅ Logs não contêm erros críticos
✅ Prometheus/Monitoring coletando métricas

Parabéns! 🚀 Seu projeto está pronto na nuvem!

