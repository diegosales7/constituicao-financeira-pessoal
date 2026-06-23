FROM nginx:stable-alpine

# Copiar configuração customizada do nginx (incluída no contexto)
COPY nginx.conf /etc/nginx/nginx.conf

# Copiar os arquivos estáticos do frontend
COPY . /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]

