// =========================================================
// API.JS
// Comunicação central do Front-end com o Back-end Spring Boot
// =========================================================

// Em ambiente de produção (servido pelo Nginx) usamos o prefixo relativo /api
// Em desenvolvimento local, acessamos o backend diretamente em http://localhost:8080
// Default to relative /api so frontend served by Nginx will proxy correctly.
// Allow direct backend access only when frontend is served from port 8080 (dev).
let API_BASE_URL;
if (typeof window !== 'undefined' && window.location.port === '8080') {
  // development: frontend served on :8080 -> backend likely on same host:8080
  API_BASE_URL = 'http://localhost:8080';
} else {
  // production / served by Nginx: use relative API path
  API_BASE_URL = '/api';
}

async function apiRequest(endpoint, method = "GET", body = null) {
  const config = {
    method: method,
    headers: {
      "Content-Type": "application/json"
    },
    credentials: "include"
  };

  if (body !== null) {
    config.body = JSON.stringify(body);
  }

  // Garantir que não haja duplicação de barras e evitar dupla adição do prefixo /api
  let url;
  if (endpoint.startsWith('/api')) {
    // endpoint já contém o prefixo /api -> usar diretamente
    url = endpoint;
  } else {
    url = endpoint.startsWith('/') ? (API_BASE_URL + endpoint) : (API_BASE_URL + '/' + endpoint);
  }
  const response = await fetch(url, config);

  const text = await response.text();

  let data = null;

  if (text) {
    try {
      data = JSON.parse(text);
    } catch (error) {
      data = text;
    }
  }

  if (!response.ok) {
    if (typeof data === "string") {
      throw new Error(data);
    }

    if (data && data.message) {
      throw new Error(data.message);
    }

    throw new Error("Erro na comunicação com o servidor.");
  }

  return data;
}