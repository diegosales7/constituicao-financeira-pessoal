# Comandos principais para deploy em VM OCI (Ubuntu)

# Atualizar sistema e instalar dependências
sudo apt update; sudo apt upgrade -y

# Instalar Docker
curl -fsSL https://get.docker.com -o get-docker.sh; sudo sh get-docker.sh
sudo usermod -aG docker $USER

# Instalar docker-compose (binário)
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose; sudo chmod +x /usr/local/bin/docker-compose

# Clonar o repositório
git clone https://github.com/diegosales7/constituicao-financeira-pessoal.git
cd "constituicao-financeira-pessoal"

# Criar .env a partir do exemplo e editar
cp .env.example .env
nano .env

# Subir containers (build se necessário)
docker compose up -d --build

# Verificar containers
docker compose ps
docker compose logs -f backend
docker compose logs -f frontend
docker compose logs -f postgres

# Reiniciar serviços
docker compose restart

# Atualizar código e rebuild
git pull
docker compose up -d --build

# Parar e remover containers
docker compose down

# Backup do banco
docker compose exec postgres pg_dump -U ${POSTGRES_USER} ${POSTGRES_DB} > backup.sql

# Restaurar backup
cat backup.sql | docker compose exec -T postgres psql -U ${POSTGRES_USER} -d ${POSTGRES_DB}

