# Stage 1: Build
FROM maven:3.9.4-eclipse-temurin-17 as builder

WORKDIR /app

# Copiar arquivos de dependência
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar código-fonte
COPY src/ src/

# Build da aplicação
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copiar jar do stage anterior
COPY --from=builder /app/target/pessoais-*.jar app.jar

# Criar usuário não-root por segurança
RUN useradd -m -u 1000 appuser && chown -R appuser:appuser /app

# Instalar curl para healthchecks internos (imagem baseada em debian/ubuntu)
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

USER appuser

# Expor porta
EXPOSE 8080

# Health check utilizando o endpoint de saúde da aplicação
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/api/health || exit 1

# Iniciar aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]

