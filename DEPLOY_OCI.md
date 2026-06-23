# Guia de Deploy - Oracle Cloud Infrastructure (OCI)

## 📋 Pré-requisitos

- Conta na Oracle Cloud Infrastructure
- Docker e Docker Compose instalados localmente
- Git
- `oci-cli` instalado e configurado
- Maven 3.9+ e Java 17+

## 🚀 Passo 1: Preparar o Ambiente Local

### 1.1 Clonar o repositório e entrar no diretório

```bash
cd "Constituição Financeira Pessoal"
```

### 1.2 Criar arquivo `.env` com variáveis de ambiente

Copie o arquivo de exemplo:

```bash
cp .env.example .env
```

Edite o `.env` com suas variáveis:

```ini
# Database
DB_USERNAME=postgres
DB_PASSWORD=senha-super-segura-aqui
DB_URL=jdbc:postgresql://postgres:5432/financas_pessoais

# JWT
JWT_SECRET=sua-chave-secreta-aleatorias-com-32-chars-minimo

# CORS (adicione seu domínio de produção)
CORS_ORIGINS=http://localhost:3000,http://localhost:5500,https://seu-dominio-oci.com

# OCI (será preenchido após criar recursos)
OCI_REGISTRY_URL=seu-registry-oci.br.ocir.io/seu-namespace
OCI_REGISTRY_USERNAME=seu-usuario-oci
OCI_REGISTRY_PASSWORD=seu-token-oci
```

## 🐳 Passo 2: Testar Localmente com Docker

### 2.1 Build e start dos containers

```bash
docker-compose up -d
```

### 2.2 Verificar logs

```bash
# Backend
docker logs -f financas-backend

# Database
docker logs -f financas-db

# Frontend
docker logs -f financas-frontend
```

### 2.3 Testar endpoints

```bash
# Health check
curl http://localhost:8080/api/health

# Frontend
open http://localhost:3000
```

### 2.4 Parar containers

```bash
docker-compose down
```

## ☁️ Passo 3: Configurar Recursos na OCI

### 3.1 Criar Container Registry (OCIR)

```bash
# Login no OCIR
docker login -u $(oci os ns get --query data -r) oracle.com

# Quando pedir senha, forneça seu Auth Token (gere em OCI Console)
```

### 3.2 Build e push da imagem Docker

```bash
# Build
docker build -t financas-backend:latest ./pessoais

# Tag para OCI Registry
docker tag financas-backend:latest \
  seu-registry-oci.br.ocir.io/seu-namespace/financas-backend:latest

# Push
docker push seu-registry-oci.br.ocir.io/seu-namespace/financas-backend:latest
```

### 3.3 Criar instância PostgreSQL na OCI

Opções recomendadas:

#### Opção A: OCI PostgreSQL Database Service (Recomendado)

1. Acesse OCI Console → Database → PostgreSQL
2. Clique em "Create DB System"
   - **Name**: financas-pessoais-db
   - **PostgreSQL 16.x** (latest)
   - **System Type**: Primary
   - **Node count**: 1
   - **Shape**: VM.Standard.E4.Flex (1 OCPU mínimo)
   - **Storage**: 100GB
   - **Network**: Selecione sua VCN
   - **Username**: postgres
   - **Password**: (gere senha forte)
3. Anote o **endpoint privado** (ex: `10.0.1.5:5432`)

#### Opção B: Compute Instance com Docker

Se preferir, crie uma VM com Docker e PostgreSQL

### 3.4 Criar OKE (Kubernetes) Cluster (Opcional - para produção)

```bash
# Criar cluster OKE
oci ce cluster create \
  --cluster-name financas-pessoais-cluster \
  --vcn-id seu-vcn-id \
  --kubernetes-version v1.28.10
```

## 🔒 Passo 4: Configurar Segurança

### 4.1 Security Groups / Network Security Groups

Crie regra para:
- **Inbound**: Port 8080 (backend), 3000 (frontend)
- **Outbound**: PostgreSQL (port 5432 para DB instance)

### 4.2 Variáveis de Ambiente Secretas

Use OCI Vault para armazenar senhas em produção:

```bash
oci vault secret create \
  --compartment-id seu-compartment-id \
  --vault-id seu-vault-id \
  --secret-name jwt-secret \
  --secret-content-type "text/plain" \
  --secret-content-data "sua-chave-jwt-super-secreta"

oci vault secret create \
  --compartment-id seu-compartment-id \
  --vault-id seu-vault-id \
  --secret-name db-password \
  --secret-content-type "text/plain" \
  --secret-content-data "sua-senha-postgres"
```

## 🚀 Passo 5: Deploy no OCI (Opção 1 - Compute Instance)

### 5.1 Criar Compute Instance

```bash
oci compute instance launch \
  --availability-domain seu-ad \
  --display-name financas-backend \
  --image-id oracle-linux-8-ocid \
  --shape VM.Standard.E4.Flex
```

### 5.2 SSH na instância

```bash
ssh -i sua-chave.key opc@seu-ip-publico
```

### 5.3 Instalar Docker e clonar projeto

```bash
# Atualizar sistema
sudo yum update -y

# Instalar Docker
sudo yum install -y docker-engine

# Iniciar Docker
sudo systemctl start docker
sudo usermod -aG docker opc

# Clonar repositório
git clone seu-repositorio.git
cd "Constituição Financeira Pessoal"

# Fazer login no OCIR
docker login -u $(oci os ns get --query data -r) br.ocir.io
```

### 5.4 Deploy com docker-compose

```bash
# Copiar `.env` com suas variáveis
cp .env.example .env
# Editar .env

# Iniciar aplicação
docker-compose up -d
```

## 🚀 Passo 6: Deploy no OKE (Kubernetes) - Recomendado para Produção

### 6.1 Criar arquivo `kubernetes.yaml`

```yaml
apiVersion: v1
kind: Namespace
metadata:
  name: financas

---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: financas-backend
  namespace: financas
spec:
  replicas: 2
  selector:
    matchLabels:
      app: financas-backend
  template:
    metadata:
      labels:
        app: financas-backend
    spec:
      containers:
      - name: backend
        image: seu-registry-oci.br.ocir.io/seu-namespace/financas-backend:latest
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "prod"
        - name: DB_URL
          value: "jdbc:postgresql://postgres-service:5432/financas_pessoais"
        - name: DB_USERNAME
          valueFrom:
            secretKeyRef:
              name: financas-secrets
              key: db-username
        - name: DB_PASSWORD
          valueFrom:
            secretKeyRef:
              name: financas-secrets
              key: db-password
        - name: JWT_SECRET
          valueFrom:
            secretKeyRef:
              name: financas-secrets
              key: jwt-secret
        livenessProbe:
          httpGet:
            path: /api/health
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /api/health
            port: 8080
          initialDelaySeconds: 5
          periodSeconds: 5
        resources:
          requests:
            memory: "512Mi"
            cpu: "250m"
          limits:
            memory: "1Gi"
            cpu: "500m"

---
apiVersion: v1
kind: Service
metadata:
  name: financas-backend-service
  namespace: financas
spec:
  type: LoadBalancer
  ports:
  - port: 80
    targetPort: 8080
  selector:
    app: financas-backend

---
apiVersion: v1
kind: ConfigMap
metadata:
  name: frontend-config
  namespace: financas
data:
  nginx.conf: |
    # ... (conteúdo do nginx.conf)

---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: financas-frontend
  namespace: financas
spec:
  replicas: 2
  selector:
    matchLabels:
      app: financas-frontend
  template:
    metadata:
      labels:
        app: financas-frontend
    spec:
      containers:
      - name: frontend
        image: nginx:alpine
        ports:
        - containerPort: 3000
        volumeMounts:
        - name: html
          mountPath: /usr/share/nginx/html
        - name: nginx-config
          mountPath: /etc/nginx
      volumes:
      - name: html
        configMap:
          name: frontend-html
      - name: nginx-config
        configMap:
          name: frontend-config

---
apiVersion: v1
kind: Service
metadata:
  name: financas-frontend-service
  namespace: financas
spec:
  type: LoadBalancer
  ports:
  - port: 80
    targetPort: 3000
  selector:
    app: financas-frontend
```

### 6.2 Deploy no Kubernetes

```bash
# Criar secrets
kubectl create secret generic financas-secrets \
  --from-literal=db-username=postgres \
  --from-literal=db-password=sua-senha \
  --from-literal=jwt-secret=sua-chave-jwt \
  -n financas

# Aplicar manifesto
kubectl apply -f kubernetes.yaml

# Verificar status
kubectl get pods -n financas
kubectl get svc -n financas
```

## 📊 Monitoramento

### Logs
```bash
kubectl logs -f deployment/financas-backend -n financas
```

### Métricas
```bash
kubectl top pods -n financas
```

### Acessar aplicação
```bash
kubectl port-forward svc/financas-backend-service 8080:80 -n financas
```

## 🔄 CI/CD com GitHub Actions

Crie `.github/workflows/deploy.yml`:

```yaml
name: Deploy to OCI

on:
  push:
    branches: [ main, develop ]

jobs:
  build-and-deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Build Docker image
        run: |
          docker build -t financas-backend:${{ github.sha }} ./pessoais
          docker tag financas-backend:${{ github.sha }} \
            ${{ secrets.OCI_REGISTRY }}/${{ secrets.OCI_NAMESPACE }}/financas-backend:latest
      
      - name: Push to OCI Registry
        run: |
          echo "${{ secrets.OCI_AUTH_TOKEN }}" | docker login -u ${{ secrets.OCI_USERNAME }} --password-stdin ${{ secrets.OCI_REGISTRY }}
          docker push ${{ secrets.OCI_REGISTRY }}/${{ secrets.OCI_NAMESPACE }}/financas-backend:latest
      
      - name: Deploy to OKE
        run: |
          # Configurar kubeconfig
          oci ce cluster create-kubeconfig --cluster-id ${{ secrets.OKE_CLUSTER_ID }} --region ${{ secrets.OCI_REGION }}
          
          # Deploy
          kubectl set image deployment/financas-backend \
            backend=${{ secrets.OCI_REGISTRY }}/${{ secrets.OCI_NAMESPACE }}/financas-backend:latest \
            -n financas
          
          kubectl rollout status deployment/financas-backend -n financas
```

## 🆘 Solução de Problemas

### Conectar ao banco de dados remoto
```bash
# Instalar psql client
sudo apt-get install postgresql-client

# Conectar
psql -h seu-db-endpoint -U postgres -d financas_pessoais
```

### Verificar logs da aplicação
```bash
docker logs financas-backend
```

### Resetar banco de dados (desenvolvimento)
```bash
docker-compose down -v
docker-compose up -d
```

### Incrementar limite de memória
```bash
# No docker-compose.yml, adicione:
services:
  backend:
    deploy:
      resources:
        limits:
          memory: 2G
```

## 📝 Checklist Final de Deploy

- [ ] Variáveis de ambiente configuradas
- [ ] Banco de dados PostgreSQL criado e testado
- [ ] JWT_SECRET alterada para valor único
- [ ] CORS configurado com domínios corretos
- [ ] Imagem Docker buildada e testada localmente
- [ ] Imagem publicada no OCI Registry
- [ ] Security Groups configurados
- [ ] SSL/TLS configurado (use OCI Load Balancer)
- [ ] Backup automático do banco habilitado
- [ ] Monitoramento OCI configurado
- [ ] Testes de funcionalidade completos

## 📞 Suporte

Para mais informações sobre OCI:
- [OCI Documentation](https://docs.oracle.com/en-us/iaas/)
- [OKE Documentation](https://docs.oracle.com/en-us/iaas/container-engine-kubernetes/home.html)
- [OCI CLI Reference](https://docs.oracle.com/en-us/iaas/tools/oci-cli/latest/)

