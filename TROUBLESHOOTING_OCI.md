# Troubleshooting - Oracle Cloud Infrastructure Deploy

## 🔍 Problemas Comuns e Soluções

### 1. Erro ao fazer Login no OCI Registry

**Problema:**
```
Error response from daemon: Get "https://seu-registry.br.ocir.io/v2/": unauthorized
```

**Solução:**
```bash
# 1. Gerar novo Auth Token na OCI Console
#    → User Settings → Auth Tokens → Generate Token
#    → Copie o token

# 2. Fazer login (use todo o token como senha)
docker login -u seu-namespace/seu-usuario seu-registry.br.ocir.io
# Senha: seu-auth-token-completo

# 3. Verificar se o login funcionou
docker login -u seu-namespace/seu-usuario seu-registry.br.ocir.io
```

**Dica:** Se usar em scripts, salve o token em `.env`:
```ini
OCI_REGISTRY_PASSWORD=seu-token-completo
```

---

### 2. Database Connection Refused

**Problema:**
```
FATAL: Ident authentication failed for user "postgres"
```

**Solução:**

#### Se usando OCI PostgreSQL Database Service:
```bash
# 1. Verificar endpoint do banco
# OCI Console → Database → PostgreSQL → Seu DB System
# Anote: Private Endpoint IP e porta

# 2. Testar conectividade
psql -h 10.0.1.5 -U postgres -d financas_pessoais
# (use a senha após "CREATE")

# 3. Se estiver em Compute Instance diferente, ajustar Security Group
oci network security-group rules list \
  --security-group-id seu-sg-id

# 4. Adicionar regra para Compute Instance
oci network security-group rules add \
  --security-group-id seu-sg-id \
  --protocol tcp \
  --source-type NETWORK \
  --source 10.0.0.0/8 \
  --tcp-destination-port-range 5432
```

#### Se usando Docker local:
```bash
# Verificar se container do PostgreSQL está rodando
docker ps | grep postgres

# Se não estiver, iniciar
docker-compose up postgres -d

# Ver logs
docker logs financas-db
```

---

### 3. Application falha ao conectar no Database

**Problema:**
```
org.postgresql.util.PSQLException: Connection to localhost:5432 refused
```

**Solução:**

1. **Variáveis de ambiente incorretas:**
```bash
# Verificar .env
cat .env | grep DB_

# Usar endpoint correto do banco:
DB_URL=jdbc:postgresql://seu-db-host:5432/financas_pessoais
DB_USERNAME=postgres
DB_PASSWORD=sua-senha-correta
```

2. **Database não foi inicializado:**
```bash
# Se é a primeira vez, rodar script de inicialização
docker-compose exec postgres psql -U postgres -d financas_pessoais -f /docker-entrypoint-initdb.d/init.sql

# Ou manualmente criar database
docker-compose exec postgres createdb -U postgres financas_pessoais
docker-compose exec postgres psql -U postgres -d financas_pessoais < data.sql
```

3. **Pool de conexões esgotado:**
```properties
# Aumentar em application-prod.properties:
spring.datasource.hikari.maximum-pool-size=30
spring.datasource.hikari.minimum-idle=5
```

---

### 4. Imagem Docker não encontrada no Registry

**Problema:**
```
Error: image financas-backend:v1.0 not found
```

**Solução:**
```bash
# 1. Verificar se a imagem existe no registry
oci artifacts container images list \
  --repository-name financas

# 2. Se não existir, fazer push novamente
docker push seu-registry.br.ocir.io/seu-namespace/financas-backend:latest

# 3. No kubernetes.yaml, usar tag correta
image: seu-registry.br.ocir.io/seu-namespace/financas-backend:latest
```

---

### 5. Erro ao aplicar Kubernetes manifest

**Problema:**
```
error: the combination of 'kind' is invalid: -f=kubernetes.yaml does not match --filename=''
```

**Solução:**
```bash
# Usar caminho absoluto ou relativo correto
kubectl apply -f ./kubernetes.yaml

# Se ainda não funcionar, verificar sintaxe YAML
kubectl apply -f ./kubernetes.yaml --dry-run=client

# Ou dividir em arquivos menores
kubectl apply -f ./k8s/backend.yaml
kubectl apply -f ./k8s/frontend.yaml
kubectl apply -f ./k8s/services.yaml
```

---

### 6. Pod stuck em CrashLoopBackOff

**Problema:**
```
NAME                    READY   STATUS             RESTARTS   AGE
financas-backend-...    0/1     CrashLoopBackOff   5          2m30s
```

**Solução:**
```bash
# 1. Ver logs detalhados
kubectl logs -f deployment/financas-backend -n financas

# 2. Ver eventos
kubectl describe pod seu-pod-name -n financas

# 3. Problemas comuns:
#    - Database não acessível: verificar connection string
#    - JVM memory: aumentar JAVA_OPTS
#    - Porta já em uso: mudar spring.server.port
#    - Properties inválidas: rever application-prod.properties
```

**Aumentar recursos:**
```yaml
# kubernetes.yaml
resources:
  requests:
    memory: "1Gi"      # aumentar de 512Mi
    cpu: "500m"        # aumentar de 250m
  limits:
    memory: "2Gi"      # aumentar de 1Gi
    cpu: "1000m"       # aumentar de 500m
```

---

### 7. LoadBalancer fica em Pending

**Problema:**
```
NAME                        TYPE           CLUSTER-IP       EXTERNAL-IP   PORT(S)        AGE
financas-backend-service    LoadBalancer   10.96.100.24     <pending>     80:30123/TCP   5m
```

**Solução:**
```bash
# 1. Verificar se há subnets públicas suficientes
oci network subnet list --vcn-id seu-vcn-id

# 2. Configurar LoadBalancer corretamente
kubectl edit svc financas-backend-service -n financas

# Adicionar:
spec:
  type: LoadBalancer
  loadBalancerSourceRanges:
    - 0.0.0.0/0  # permitir acesso público

# 3. Aguardar: OCI leva ~5 minutos para provisionar
watch kubectl get svc -n financas

# 4. Quando tiver IP público:
curl http://<EXTERNAL-IP>:80/api/health
```

---

### 8. Certificado SSL/TLS não funciona

**Problema:**
```
ERR_SSL_PROTOCOL_ERROR
```

**Solução:**

1. **Usar OCI Load Balancer com SSL:**
```bash
oci lb load-balancer create \
  --display-name financas-lb \
  --compartment-id seu-compartment-id \
  --shape-name "100Mbps"

# Adicionar listener HTTPS
oci lb listener create \
  --load-balancer-id seu-lb-id \
  --default-backend-set-name financas-backend \
  --port 443 \
  --protocol HTTPS \
  --ssl-configuration file://ssl-config.json
```

2. **Gerar certificado (Let's Encrypt via cert-manager):**
```yaml
apiVersion: cert-manager.io/v1
kind: ClusterIssuer
metadata:
  name: letsencrypt-prod
spec:
  acme:
    server: https://acme-v02.api.letsencrypt.org/directory
    email: seu-email@exemplo.com
    privateKeySecretRef:
      name: letsencrypt-prod
    solvers:
    - http01:
        ingress:
          class: nginx
---
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: financas-ingress
  annotations:
    cert-manager.io/cluster-issuer: "letsencrypt-prod"
spec:
  tls:
  - hosts:
    - seu-dominio.com
    secretName: financas-tls
  rules:
  - host: seu-dominio.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: financas-backend-service
            port:
              number: 80
```

---

### 9. Dados não persistem após restart

**Problema:**
PostgreSQL data é perdida ao reiniciar container

**Solução:**

```yaml
# docker-compose.yml - confirmar que volume está correto
services:
  postgres:
    volumes:
      - postgres_data:/var/lib/postgresql/data  # usar volume nomeado

volumes:
  postgres_data:
    driver: local
```

**Para OKE com StatefulSet:**
```yaml
apiVersion: apps/v1
kind: StatefulSet
metadata:
  name: postgres
spec:
  serviceName: postgres
  replicas: 1
  selector:
    matchLabels:
      app: postgres
  template:
    metadata:
      labels:
        app: postgres
    spec:
      containers:
      - name: postgres
        image: postgres:16-alpine
        volumeMounts:
        - name: data
          mountPath: /var/lib/postgresql/data
  volumeClaimTemplates:
  - metadata:
      name: data
    spec:
      accessModes: ["ReadWriteOnce"]
      resources:
        requests:
          storage: 100Gi
```

---

### 10. Performance lenta em produção

**Problema:**
Requisições lentas mesmo com banda suficiente

**Solução:**

1. **Aumentar recursos:**
```bash
kubectl set resources deployment/financas-backend \
  --limits=cpu=1000m,memory=2Gi \
  --requests=cpu=500m,memory=1Gi \
  -n financas
```

2. **Aumentar replicas:**
```yaml
# kubernetes.yaml
spec:
  replicas: 3  # aumentar de 2
```

3. **Ativar caching:**
```properties
# application-prod.properties
spring.cache.type=redis
spring.redis.host=seu-redis-host
spring.redis.port=6379
```

4. **Verificar índices do banco:**
```sql
-- Conectar ao banco
psql -h seu-db-host -U postgres -d financas_pessoais

-- Ver índices
\d+ sua_tabela

-- Criar índices em colunas frequentemente consultadas
CREATE INDEX idx_usuario_id ON receita(usuario_id);
CREATE INDEX idx_data_receita ON receita(data_recebimento);
```

5. **Ativar HTTP/2 e compressão:**
```properties
# Já configurado em application-prod.properties
server.compression.enabled=true
server.http2.enabled=true
```

---

## 📊 Monitoramento na OCI

### Healthchecks
```bash
# Verificar se pod está saudável
kubectl get pods -n financas -o wide

# Ver logs de erro
kubectl logs -f deployment/financas-backend --tail=50 -n financas
```

### Métricas
```bash
# CPU e Memória
kubectl top pods -n financas

# Detailed metrics
kubectl describe node seu-node-name
```

### Alertas na OCI
- Ir a OCI Console → Monitoring → Alarms
- Configurar alertas para: CPU > 70%, Memory > 80%, Error Rate > 5%

---

## 🆘 Quando nada funciona

```bash
# 1. Resetar tudo (CUIDADO - deleta dados!)
kubectl delete namespace financas
kubectl create namespace financas

# 2. Recriar secrets
kubectl create secret generic financas-secrets \
  --from-literal=db-username=postgres \
  --from-literal=db-password=sua-senha \
  --from-literal=jwt-secret=sua-jwt-key \
  -n financas

# 3. Replicar manifesto
kubectl apply -f kubernetes.yaml

# 4. Monitorar progresso
watch kubectl get pods -n financas

# 5. Ver logs de startup
kubectl logs -f deployment/financas-backend -n financas --all-containers=true
```

---

## 📞 Recursos Úteis

- [OCI Troubleshooting Guide](https://docs.oracle.com/en-us/iaas/Content/troubleshooting.htm)
- [OKE Best Practices](https://docs.oracle.com/en-us/iaas/Content/ContEng/Concepts/contengoverviewapi.htm)
- [PostgreSQL on OCI](https://www.oracle.com/cloud/postgresql/)
- [Spring Boot on OCI](https://www.oracle.com/cloud/middlewares/java/java-spring-boot/)

