#!/bin/bash

# ============================================
# Deploy Script para Oracle Cloud Infrastructure
# ============================================

set -e

echo "🚀 Iniciando Deploy para OCI..."

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Verificar se variáveis estão definidas
check_env() {
    if [ -z "$1" ]; then
        echo -e "${RED}❌ Erro: Variável $2 não definida${NC}"
        exit 1
    fi
}

# Carregar .env se existir
if [ -f .env ]; then
    export $(cat .env | grep -v '^#' | xargs)
    echo -e "${GREEN}✓ Variáveis carregadas de .env${NC}"
else
    echo -e "${YELLOW}⚠ Arquivo .env não encontrado${NC}"
    echo "   Copie de .env.example: cp .env.example .env"
    exit 1
fi

# Validar variáveis necessárias
check_env "$DB_USERNAME" "DB_USERNAME"
check_env "$DB_PASSWORD" "DB_PASSWORD"
check_env "$JWT_SECRET" "JWT_SECRET"
check_env "$OCI_REGISTRY_URL" "OCI_REGISTRY_URL"

# Etapa 1: Build da imagem Docker
echo -e "\n${YELLOW}📦 Etapa 1: Building Docker image...${NC}"
docker build -t financas-backend:latest ./pessoais
echo -e "${GREEN}✓ Docker image buildada com sucesso${NC}"

# Etapa 2: Tag para OCI Registry
echo -e "\n${YELLOW}🏷 Etapa 2: Tagging para OCI Registry...${NC}"
docker tag financas-backend:latest ${OCI_REGISTRY_URL}/financas-backend:latest
echo -e "${GREEN}✓ Image taggeada para: ${OCI_REGISTRY_URL}/financas-backend:latest${NC}"

# Etapa 3: Push para OCI Registry
echo -e "\n${YELLOW}📤 Etapa 3: Pushing para OCI Registry...${NC}"
echo "   Login no OCI..."
echo "$OCI_REGISTRY_PASSWORD" | docker login -u $OCI_REGISTRY_USERNAME --password-stdin ${OCI_REGISTRY_URL}
docker push ${OCI_REGISTRY_URL}/financas-backend:latest
echo -e "${GREEN}✓ Image enviada com sucesso${NC}"

# Etapa 4: Deploy no OKE (se configurado)
if [ ! -z "$OKE_CLUSTER_ID" ]; then
    echo -e "\n${YELLOW}☸️ Etapa 4: Deploying no Kubernetes (OKE)...${NC}"

    # Configurar kubeconfig
    echo "   Obtendo kubeconfig..."
    oci ce cluster create-kubeconfig \
        --cluster-id $OKE_CLUSTER_ID \
        --region ${OCI_REGION:-us-phoenix-1} \
        --file $HOME/.kube/config

    # Criar namespace se não existir
    kubectl create namespace financas --dry-run=client -o yaml | kubectl apply -f -

    # Criar secrets
    echo "   Criando secrets do Kubernetes..."
    kubectl create secret generic financas-secrets \
        --from-literal=db-username=$DB_USERNAME \
        --from-literal=db-password=$DB_PASSWORD \
        --from-literal=jwt-secret=$JWT_SECRET \
        --namespace=financas \
        --dry-run=client -o yaml | kubectl apply -f -

    # Atualizar imagem no deployment
    echo "   Atualizando deployment..."
    kubectl set image deployment/financas-backend \
        backend=${OCI_REGISTRY_URL}/financas-backend:latest \
        --namespace=financas || echo "   ⚠ Deployment ainda não existe, será criado via manifesto"

    # Aplicar manifesto Kubernetes
    if [ -f kubernetes.yaml ]; then
        echo "   Aplicando kubernetes.yaml..."
        kubectl apply -f kubernetes.yaml
    fi

    # Aguardar rollout
    echo "   Aguardando deploy..."
    kubectl rollout status deployment/financas-backend --namespace=financas --timeout=5m

    echo -e "${GREEN}✓ Deploy no OKE concluído${NC}"

    # Mostrar informações de acesso
    echo -e "\n${GREEN}📊 Informações de Acesso:${NC}"
    kubectl get svc --namespace=financas
else
    echo -e "${YELLOW}⚠ OKE_CLUSTER_ID não configurado${NC}"
    echo "   Para deploy em Kubernetes, configure OKE_CLUSTER_ID no arquivo .env"
fi

echo -e "\n${GREEN}✅ Deploy concluído com sucesso!${NC}"
echo -e "\n${YELLOW}Próximos passos:${NC}"
echo "   1. Verificar status: kubectl get pods -n financas"
echo "   2. Ver logs: kubectl logs -f deployment/financas-backend -n financas"
echo "   3. Obter IP do LoadBalancer: kubectl get svc -n financas"
echo "   4. Atualizar frontend settings.json com URL de produção"

