# Deploy simples na Oracle Cloud Infrastructure (VM Ubuntu + Docker Compose)

Este guia mostra passo-a-passo como subir o projeto "Constituição Financeira Pessoal" em uma Compute Instance (VM Ubuntu) da OCI usando Docker Compose. É uma opção simples e recomendada para iniciantes.

Arquitetura:
- Browser → Nginx (frontend) → /api → Backend Spring Boot → PostgreSQL (container)

O que será criado na VM:
- Uma VM Ubuntu 22.04 (ou similar)
- Docker e Docker Compose
- Containers: postgres, backend (Spring Boot) e frontend (Nginx)

Portas a liberar no Security List/NSG da OCI:
- 22 (SSH) — para administração
- 80 (HTTP) — para acesso ao frontend

IMPORTANTE: NÃO liberar 8080 ou 5432 publicamente. Apenas 80 e 22.

Passo a passo resumido:
1) Criar a VM Ubuntu na OCI (tamanho mínimo: 1 OCPU + 2GB RAM recomendado)
2) Abrir portas 22 e 80 no Security List/Network Security Group
3) Conectar via SSH
4) Instalar Docker e Docker Compose
5) Clonar o repositório git
6) Criar o arquivo `.env` a partir de `.env.example` e editar as senhas
7) Rodar `docker compose up -d --build`
8) Testar `curl http://localhost/api/health` e abrir http://IP_PUBLICO_DA_VM

Comandos e detalhes técnicos estão no arquivo `COMANDOS_OCI_VM.md` neste repositório.

Passo-a-passo detalhado

1) Criar a VM na OCI
- No Console OCI → Compute → Instances → Create Instance
- Escolha imagem Ubuntu 22.04, shape compatível (ex: VM.Standard.E2.1)
- Adicionar sua chave SSH pública para acesso

2) Configurar a rede (Security List / NSG)
- Permita entrada (ingress) para as portas TCP 22 e 80 do seu IP ou 0.0.0.0/0 (se quiser acesso público)
- NÃO permita 8080 ou 5432 publicamente

3) Conectar via SSH
ssh ubuntu@IP_PUBLICO_DA_VM

4) Instalar Docker e Docker Compose (comandos para executar na VM):
sudo apt update; sudo apt upgrade -y
curl -fsSL https://get.docker.com -o get-docker.sh; sudo sh get-docker.sh
sudo usermod -aG docker $USER
sudo apt install -y git
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose; sudo chmod +x /usr/local/bin/docker-compose

Após adicionar seu usuário ao grupo docker, reconecte a sessão SSH (ou execute `newgrp docker`).

5) Clonar o projeto
git clone https://github.com/diegosales7/constituicao-financeira-pessoal.git
cd "constituicao-financeira-pessoal"

6) Criar `.env` com as variáveis de produção
cp .env.example .env
nano .env   # editar as variáveis: POSTGRES_PASSWORD, DB_PASSWORD, JWT_SECRET, CORS_ALLOWED_ORIGINS

7) Subir os containers
docker compose up -d --build

8) Verificar se os serviços estão prontos
docker compose ps
docker compose logs -f backend
docker compose logs -f frontend
docker compose logs -f postgres

9) Testar healthcheck e front
curl http://localhost/api/health
curl http://IP_PUBLICO_DA_VM/api/health
Abra no navegador: http://IP_PUBLICO_DA_VM

Atualizar o deploy (atualizar código e rebuild):
git pull
docker compose up -d --build

Parar os serviços:
docker compose down

Backup rápido do banco (na VM):
docker compose exec postgres pg_dump -U ${POSTGRES_USER} ${POSTGRES_DB} > backup.sql

Restaurar backup:
cat backup.sql | docker compose exec -T postgres psql -U ${POSTGRES_USER} -d ${POSTGRES_DB}

Erros comuns e soluções rápidas:
- Container backend falha ao iniciar: verifique `docker compose logs backend` e se variáveis DB_* estão corretas
- Erro de conexão com PostgreSQL: confirme que `DB_URL` aponta para `postgres:5432` e credenciais batem
- 502 Bad Gateway no frontend: verifique se o backend está saudável (`/api/health`) e que o Nginx está proxying para `backend:8080`

Observação: Este é um deploy simples para iniciantes. Arquivos Kubernetes/OKE permanecem no repositório para evolução futura.

