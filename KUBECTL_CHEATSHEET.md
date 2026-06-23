# 🚀 OCI Deploy - Cheat Sheet (Referência Rápida)

## 🔐 Authentication & Setup

```bash
# Configurar OCI CLI
oci setup config

# Login no Docker Registry OCI
docker login -u $(oci os ns get --query data -r) br.ocir.io

# Verificar namespace OCI
oci os ns get --query data

# Listar compartimentos
oci iam compartment list
```

## 🐳 Docker Commands

```bash
# Build local
docker build -t financas-backend:latest ./pessoais

# Tag para registry
docker tag financas-backend:latest seu-registry.br.ocir.io/seu-namespace/financas-backend:latest

# Push para OCI
docker push seu-registry.br.ocir.io/seu-namespace/financas-backend:latest

# Testar localmente
docker-compose up -d
docker-compose logs -f backend
docker-compose down

# Limpar imagens
docker rmi -f financas-backend:latest
docker system prune -a
```

## ☸️ Kubernetes Commands

### Status
```bash
# Ver todos os pods
kubectl get pods -n financas

# Ver services
kubectl get svc -n financas

# Ver deployments
kubectl get deploy -n financas

# Ver detailed info
kubectl describe pod nome-do-pod -n financas

# Ver HPA status
kubectl get hpa -n financas
```

### Logs
```bash
# Logs em tempo real
kubectl logs -f deployment/backend -n financas

# Últimas 100 linhas
kubectl logs --tail=100 deployment/backend -n financas

# Logs de container anterior (crash)
kubectl logs deployment/backend -n financas --previous

# Logs de todos os pods
kubectl logs -f -l app=backend -n financas
```

### Exec & Debug
```bash
# Terminal interativo
kubectl exec -it pod-name -n financas -- /bin/bash

# Executar comando
kubectl exec pod-name -n financas -- ps aux

# Port forward para acesso local
kubectl port-forward svc/backend-service 8080:8080 -n financas

# Verificar variáveis de ambiente
kubectl exec pod-name -n financas -- env | grep DB_
```

### Deploy & Update
```bash
# Aplicar manifesto
kubectl apply -f kubernetes.yaml

# Validar manifesto
kubectl apply -f kubernetes.yaml --dry-run=client

# Atualizar imagem
kubectl set image deployment/backend \
  backend=seu-registry.br.ocir.io/seu-namespace/financas-backend:v1.0 \
  -n financas

# Rollout status
kubectl rollout status deployment/backend -n financas

# Rollback (se algo der errado)
kubectl rollout undo deployment/backend -n financas

# Restart pods
kubectl rollout restart deployment/backend -n financas

# Scale replicas
kubectl scale deployment backend --replicas=3 -n financas

# Delete pod (vai recriar)
kubectl delete pod pod-name -n financas
```

### Secrets & ConfigMaps
```bash
# Criar secret
kubectl create secret generic financas-secrets \
  --from-literal=db-password=senha \
  --from-literal=jwt-secret=secreta \
  -n financas

# Ver secrets (valores cifrados)
kubectl get secrets -n financas

# Descrição de secret
kubectl describe secret financas-secrets -n financas

# Editar secret
kubectl edit secret financas-secrets -n financas

# Ver value em base64
kubectl get secret financas-secrets -o jsonpath='{.data.db-password}' | base64 --decode
```

### Storage
```bash
# Ver PVC
kubectl get pvc -n financas

# Ver PV
kubectl get pv

# Usar tamanho de PVC
kubectl edit pvc postgres-storage-postgres-0 -n financas
```

## 📊 Monitoring & Metrics

```bash
# CPU e Memória dos pods
kubectl top pods -n financas

# CPU e Memória dos nós
kubectl top nodes

# Eventos do cluster
kubectl get events -n financas

# Status detalhado
kubectl status -n financas

# HPA metrics
kubectl get hpa backend-hpa -n financas -w

# Describe HPA
kubectl describe hpa backend-hpa -n financas
```

## 🗄️ Database

### Local (Docker)
```bash
# Conectar ao DB local
docker-compose exec postgres psql -U postgres -d financas_pessoais

# Backup database
docker-compose exec postgres pg_dump -U postgres financas_pessoais > backup.sql

# Restore database
docker-compose exec -T postgres psql -U postgres financas_pessoais < backup.sql

# Ver size
docker-compose exec postgres du -sh /var/lib/postgresql/data
```

### OCI PostgreSQL Service
```bash
# Conectar via tunnel SSH
psql -h seu-db-endpoint -U postgres -d financas_pessoais

# Listar databases
\l

# Conectar a database
\c financas_pessoais

# Listar tabelas
\dt

# Ver tamanho de tabelas
SELECT schemaname, tablename, pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename))
FROM pg_tables ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC;
```

## 🔍 Network & Connectivity

```bash
# Testar conectividade para DB
kubectl exec deployment/backend -n financas -- nc -zv postgres-service 5432

# Realizar ping
kubectl exec deployment/backend -n financas -- ping google.com

# Verificar DNS
kubectl exec deployment/backend -n financas -- nslookup postgres-service.financas.svc.cluster.local

# Investigar conectividade de network
kubectl exec deployment/backend -n financas -- curl http://backend-service:8080/api/health
```

## 💾 Backup & Restore

```bash
# Backup do manifesto Kubernetes
kubectl get all -n financas -o yaml > backup-financas.yaml

# Restore
kubectl apply -f backup-financas.yaml

# Backup de secrets
kubectl get secrets -n financas -o yaml > backup-secrets.yaml

# Export database
kubectl exec postgres-0 -n financas -- \
  pg_dump -U postgres financas_pessoais | gzip > db_backup.sql.gz

# Restore database
zcat db_backup.sql.gz | kubectl exec -i postgres-0 -n financas -- \
  psql -U postgres -d financas_pessoais
```

## 🔐 Security

```bash
# Verificar RBAC
kubectl auth can-i list pods --as=system:serviceaccount:financas:default

# Aplicar network policy
kubectl apply -f - <<EOF
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: deny-all
  namespace: financas
spec:
  podSelector: {}
  policyTypes:
  - Ingress
  - Egress
EOF

# Ver network policies
kubectl get networkpolicy -n financas
```

## 📈 Scaling

```bash
# Manual scale
kubectl scale deployment backend --replicas=5 -n financas

# Ver atual
kubectl get deploy backend -o jsonpath='{.spec.replicas}' -n financas

# Watch scaling
kubectl get deployment backend -w -n financas

# HPA limits
kubectl patch hpa backend-hpa -p '{"spec":{"maxReplicas":10}}' -n financas
```

## 🧹 Cleanup

```bash
# Delete pod (vai recreate)
kubectl delete pod pod-name -n financas

# Delete deployment (deleta todos os pods)
kubectl delete deployment backend -n financas

# Delete service
kubectl delete svc backend-service -n financas

# Delete todo o namespace (CUIDADO!)
kubectl delete namespace financas

# Delete recurso específico
kubectl delete secret financas-secrets -n financas
```

## 🐛 Troubleshooting

```bash
# Checar status geral
kubectl get all -n financas

# Ver eventos recentes
kubectl get events -n financas --sort-by='.lastTimestamp'

# Describe para detalhes
kubectl describe deployment backend -n financas
kubectl describe pod pod-name -n financas
kubectl describe svc backend-service -n financas

# Ver logs de erro
kubectl logs deployment/backend -n financas | grep -i error

# Check container status
kubectl get pods -o jsonpath='{range .items[*]}{.metadata.name}{"\t"}{.status.containerStatuses[*].state}{"\n"}{end}'

# Diagnose pod
kubectl debug pod-name -n financas -it --image=busybox
```

## 🌍 OCI CLI Commands

```bash
# Listar images no registry
oci artifacts container images list --repository-name financas

# Deletar image antiga
oci artifacts container images delete --image-id seu-image-id

# Ver clusters
oci ce cluster list

# Ver compute instances
oci compute instance list

# Ver databases
oci dbcs db-system list

# Ver load balancers
oci lb load-balancer list

# Criar/Update security group rules
oci network security-group rules add \
  --security-group-id seu-sg-id \
  --protocol tcp \
  --source 0.0.0.0/0 \
  --tcp-destination-port-range 443
```

## ⚡ One-Liners Úteis

```bash
# Reiniciar todos os pods
kubectl delete pods --all -n financas

# Force delete pod travado
kubectl delete pod pod-name -n financas --grace-period=0 --force

# Ver URLs de serviços
for svc in $(kubectl get svc -n financas -o name); do
  echo "Service: $svc"
  kubectl get $svc -n financas -o wide
done

# Atualizar 100% das replicas em paralelo
kubectl patch deployment backend -p \
  '{"spec":{"strategy":{"type":"RollingUpdate","rollingUpdate":{"maxSurge":"100%"}}}}' \
  -n financas

# Watch status em tempo real
watch -n 2 'kubectl get pods -n financas'

# Copiar arquivo de pod para local
kubectl cp financas/pod-name:/etc/nginx/nginx.conf ./nginx.conf

# Copiar arquivo local para pod
kubectl cp ./app.jar financas/pod-name:/tmp/app.jar

# Executar script em todos os pods
kubectl exec -i deployment/backend -n financas -- bash < script.sh

# Ver resource requests vs usage
kubectl get pods -n financas -o custom-columns=NAME:.metadata.name,CPU_REQ:.spec.containers[0].resources.requests.cpu,MEM_REQ:.spec.containers[0].resources.requests.memory
```

## 📋 Variáveis de Referência

```bash
# Exportar para facilitar scripts
export NAMESPACE=financas
export BACKEND_POD=$(kubectl get pod -n $NAMESPACE -l app=backend -o jsonpath='{.items[0].metadata.name}')
export DB_POD=$(kubectl get pod -n $NAMESPACE -l app=postgres -o jsonpath='{.items[0].metadata.name}')
export BACKEND_NODE=$(kubectl get pod $BACKEND_POD -n $NAMESPACE -o jsonpath='{.spec.nodeName}')

# Usar
kubectl logs $BACKEND_POD -n $NAMESPACE
kubectl exec $DB_POD -n $NAMESPACE -- psql -U postgres
kubectl describe node $BACKEND_NODE
```

---

## 📚 Documentação Relacionada

- `DEPLOY_OCI.md` - Guia completo
- `ARQUITETURA_DEPLOY.md` - Arquitetura
- `TROUBLESHOOTING_OCI.md` - Problemas comuns
- Kubernetes Docs: https://kubernetes.io/docs/reference/kubectl/

