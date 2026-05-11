// =========================================================
// API.JS
// Comunicação central do Front-end com o Back-end Spring Boot
// =========================================================

const API_BASE_URL = "http://localhost:10000";

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

  const response = await fetch(API_BASE_URL + endpoint, config);

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