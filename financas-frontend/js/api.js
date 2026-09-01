// =========================================================
// API.JS
// Comunicação central do Front-end com o Back-end Spring Boot
// =========================================================

// Em ambiente de produção (Nginx) usamos o prefixo relativo /api.
// Em desenvolvimento local, o backend expõe a API em /api dentro do Spring Boot.
// Por isso a base precisa incluir /api em ambos os ambientes.
let API_BASE_URL;
if (typeof window !== 'undefined' && window.location.port === '8080') {
  // Durante desenvolvimento o backend está ouvindo na porta 10000
  API_BASE_URL = 'http://localhost:10000/api';
} else {
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

  let url;
  if (!endpoint) {
    url = API_BASE_URL;
  } else if (endpoint.startsWith('http://') || endpoint.startsWith('https://')) {
    url = endpoint;
  } else if (endpoint.startsWith('/api')) {
    url = endpoint;
  } else {
    url = API_BASE_URL + (endpoint.startsWith('/') ? endpoint : '/' + endpoint);
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