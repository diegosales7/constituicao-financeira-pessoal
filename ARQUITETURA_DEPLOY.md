# Arquitetura de Deploy - Constituição Financeira Pessoal

## 📋 Visão Geral da Arquitetura

```
┌─────────────────────────────────────────────────────────────────┐
│                    ORACLE CLOUD INFRASTRUCTURE (OCI)            │
│                                                                  │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │              OCI LOAD BALANCER (HTTPS)                   │  │
│  │  ├─ Port 443 (HTTPS) → Frontend + Backend               │  │
│  │  └─ Port 80 (HTTP) → Redirect to HTTPS                 │  │
│  └──────────────────────────────────────────────────────────┘  │
│           │                                │                     │
│           ▼                                ▼                     │
│  ┌──────────────────────┐      ┌──────────────────────────┐    │
│  │   OKE CLUSTER        │      │   OCI OBJECT STORAGE     │    │
│  │  (Kubernetes)        │      │   (Frontend estático)    │    │
│  │                      │      │                          │    │
│  │ ┌────────────────┐   │      │ HTML, CSS, JS, Imagens   │    │
│  │ │ Nginx (Frontend)   │      │ CDN habilitado            │    │
│  │ │ (2 pods)       │   │      └──────────────────────────┘    │
│  │ └────────────────┘   │                                       │
│  │                      │                                       │
│  │ ┌─────────────────┐  │                                       │
│  │ │ Backend Java    │  │                                       │
│  │ │ Spring Boot     │  │                                       │
│  │ │ (2-3 pods)      │  │                                       │
│  │ └─────────────────┘  │                                       │
│  └──────────────────────┘                                       │
│           │                                                     │
│           ▼                                                     │
│  ┌──────────────────────┐                                       │
│  │  OCI PostgreSQL DB   │                                       │
│  │  (MySQL/Managed)     │                                       │
│  │                      │                                       │
│  │ - Backup automático  │                                       │
│  │ - Replicação         │                                       │
│  │ - Monitoring         │                                       │
│  └──────────────────────┘                                       │
│                                                                  │
│  ┌──────────────────────┐    ┌──────────────────────┐          │
│  │   OCI VAULT          │    │  OCI MONITORING      │          │
│  │  (Secrets Storage)   │    │  (Metrics, Logs)     │          │
│  │                      │    │                      │          │
│  │ - JWT Secret         │    │ - CPU/Memory         │          │
│  │ - DB Password        │    │ - Network            │          │
│  │ - API Keys           │    │ - Alertas            │          │
│  └──────────────────────┘    └──────────────────────┘          │
│                                                                  │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │              NETWORK (VCN - Virtual Cloud Network)       │  │
│  │  ├─ Subnet pública: LoadBalancer, Bastion               │  │
│  │  └─ Subnet privada: k8s nodes, Database                 │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘

                    Usuário (Browser)
                           │
                           ▼ HTTPS
                    Load Balancer (OCI)
                     /             \
                    /               \
              Frontend CSS/JS         Backend API
              (Nginx/CDN)           (Spring Boot)
                    |                    |
                    └────> Database <────┘
                        (PostgreSQL)
```

## 🔒 Segurança

### Network Security
- Load Balancer em subnet pública
- OKE nodes em subnet privada
- Database em subnet privada
- SSH Bastion para acesso administrativo
- Network Security Groups (firewalls)

### Data Security
- JWT para autenticação
- Senhas em OCI Vault (não em código)
- SSL/TLS para comunicação
- Hashing de senhas (bcrypt)
- Validação de entrada

### Application Security
- Spring Security configurado
- CORS restritivo em produção
- Rate limiting (opcional)
- SQL Injection prevention (JPA)
- XSS protection

## 📦 Componentes

### Frontend (Nginx)
```
nginx-service (LoadBalancer)
├─ Port 80 → Redirect HTTPS
├─ Port 443 → Nginx Container
└─ Volume: HTML/CSS/JS files
```

### Backend (Java Spring Boot)
```
backend-service (ClusterIP)
├─ Port 8080: Java Application
├─ Liveness Probe: /api/health
├─ Readiness Probe: /api/health
└─ Resources: CPU 500m, RAM 1Gi
```

### Database (PostgreSQL)
```
OCI PostgreSQL Database Service
├─ 16.x (latest)
├─ Backup automático diário
├─ Point-in-time recovery
├─ Network: Subnet privada
└─ Monitoring: OCI Metrics
```

### Storage (Secrets)
```
OCI Vault (KMS)
├─ JWT Secret
├─ Database Password
├─ API Keys
└─ Other sensitive data
```

## 🚀 Fluxo de Deploy

```
1. DESENVOLVIMENTO LOCAL
   ├─> docker-compose up
   ├─> Testes em localhost
   └─> Commit no Git

2. CI/CD PIPELINE (GitHub Actions)
   ├─> Build Docker image
   ├─> Testes unitários
   ├─> Push para OCI Registry
   └─> Deploy webhook

3. STAGING (Opcional)
   ├─> Validar em ambiente similar
   ├─> Testes de integração
   └─> QA manual

4. PRODUÇÃO
   ├─> Apply Kubernetes manifest
   ├─> Rollout deployment
   ├─> Smoke tests
   └─> Monitor logs
```

## 📊 Monitoring e Alertas

### Logs
```
OCI Logging Service
├─ Application logs
├─ Nginx access logs
├─ Database logs
└─ Security logs
```

### Metrics
```
OCI Monitoring
├─ CPU utilização
├─ Memory utilização
├─ Network I/O
├─ Request latency
├─ Error rate
└─ Database connections
```

### Alertas
```
OCI (Alarms)
├─ CPU > 70% → Scale up
├─ Memory > 80% → Alert
├─ Error rate > 5% → Alert
├─ DB unavailable → Critical
└─ HTTP 5xx > 10/min → Alert
```

## 🔄 Auto-scaling

### Horizontal (Kubernetes)
```yaml
HorizontalPodAutoscaler:
  minReplicas: 2
  maxReplicas: 5
  targetCPUUtilation: 70%
  targetMemoryUtilization: 80%
```

### Vertical (OKE Node Pool)
```yaml
Node Pool:
  shape: VM.Standard.E4.Flex
  memory: 16 GB
  ocpus: 2
  auto-scaling: enabled
```

## 🆘 Disaster Recovery

### Backup
- Database: Daily backup, 30-day retention
- Application: Versioned Docker images
- Configuration: Infrastructure as Code (Terraform/ORM)

### Recovery
- Database restore: Point-in-time recovery
- Application: Blue-green deployment
- Failover: Automatic via OKE

## 📋 Checklist de Deploy

- [ ] Conta OCI criada e configurada
- [ ] OCI CLI instalado e autenticado
- [ ] Namespace OCI criado
- [ ] VCN criada com subnets
- [ ] PostgreSQL Database Service criado
- [ ] Vault criado com secrets
- [ ] OKE Cluster criado
- [ ] OCIR login configurado
- [ ] Dockerfile testado localmente
- [ ] Imagem Docker publicada (OCIR)
- [ ] Kubernetes secrets criados
- [ ] kubernetes.yaml validado
- [ ] Deployment aplicado com sucesso
- [ ] Services acessíveis
- [ ] HTTPS/SSL configurado
- [ ] DNS apontando para LoadBalancer
- [ ] Monitoring ativo
- [ ] Alertas configurados
- [ ] Backup automático habilitado
- [ ] Testes de smoke completos

## 🔗 Referências

- [OCI Best Practices](https://docs.oracle.com/en/cloud/)
- [Spring Boot on OCI](https://spring.io/blog/2021/10/12/multi-cloud-patterns)
- [Kubernetes on OCI](https://docs.oracle.com/en-us/iaas/container-engine-kubernetes/)
- [PostgreSQL on OCI](https://www.oracle.com/cloud/db-postgresql/)
- [OCI Load Balancer](https://docs.oracle.com/en-us/iaas/Content/Balance/Concepts/balanceoverview.htm)

