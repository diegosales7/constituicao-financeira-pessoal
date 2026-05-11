// =========================================================
// SESSAO.JS
// Controla usuário logado e sidebar
// =========================================================

function obterUsuarioLogado() {
  const usuario = localStorage.getItem("usuarioLogado");

  if (!usuario) {
    return null;
  }

  try {
    return JSON.parse(usuario);
  } catch (error) {
    localStorage.removeItem("usuarioLogado");
    return null;
  }
}

function protegerPagina() {
  const paginaAtual = window.location.pathname.toLowerCase();

  const paginasLivres = [
    "index.html",
    "cadastro.html",
    "sobre.html"
  ];

  const ehPaginaLivre = paginasLivres.some(function(pagina) {
    return paginaAtual.endsWith(pagina);
  });

  if (ehPaginaLivre) {
    return;
  }

  // Validar onboarding
  if (paginaAtual.endsWith("onboarding.html")) {
    const usuario = obterUsuarioLogado();
    const onboardingCompleto = localStorage.getItem("onboardingCompleto");

    if (!usuario) {
      window.location.href = "index.html";
      return;
    }

    // Se o onboarding já foi completo, redirecionar para dashboard
    if (onboardingCompleto === "true") {
      window.location.href = "dashboard.html";
      return;
    }

    return;
  }

  const usuario = obterUsuarioLogado();

  if (!usuario) {
    window.location.href = "index.html";
    return;
  }

  // Se não completou onboarding, redirecionar para lá (primeira vez)
  const onboardingCompleto = localStorage.getItem("onboardingCompleto");
  if (onboardingCompleto !== "true" && !paginaAtual.endsWith("onboarding.html")) {
    // Permitir acesso ao onboarding apenas na primeira vez
    if (onboardingCompleto === "false") {
      // window.location.href = "onboarding.html";
      // Comentado para permitir uso normal após primeira visita
    }
  }
}

function mostrarUsuarioNaSidebar() {
  const usuario = obterUsuarioLogado();
  const sidebar = document.querySelector(".sidebar");
  const nav = document.querySelector(".sidebar-nav");

  if (!usuario || !sidebar || !nav) {
    return;
  }

  if (document.querySelector(".sidebar-user")) {
    return;
  }

  const gatilho = typeof obterGatilhoAtivo === "function"
    ? obterGatilhoAtivo()
    : { nome: "😌 Estado Normal" };

  const emergencia = typeof obterEstadoEmergencia === "function"
    ? obterEstadoEmergencia()
    : { ativo: false };

  const caixaUsuario = document.createElement("div");
  caixaUsuario.classList.add("sidebar-user");

  caixaUsuario.innerHTML = `
    <div class="sidebar-user-label">Logado como</div>
    <div class="sidebar-user-name">${usuario.nome || "Usuário"}</div>
    <div class="sidebar-user-email">📧 ${usuario.email || "E-mail não informado"}</div>
    <div class="sidebar-user-age">🎂 Idade: ${usuario.idade || "Não informada"}</div>

    <div class="sidebar-state">
      <div class="sidebar-state-label">Estado Financeiro</div>
      <div class="sidebar-state-value">
        ${gatilho.nome}
      </div>
    </div>

    <div class="sidebar-state">
      <div class="sidebar-state-label">Emergência</div>
      <div class="sidebar-state-value ${emergencia.ativo ? "state-danger" : "state-normal"}">
        ${emergencia.ativo ? "🚨 Ativa pelo TFP" : "✅ Inativa"}
      </div>
    </div>
  `;

  sidebar.insertBefore(caixaUsuario, nav);
}

function configurarSair() {
  const logoutLinks = document.querySelectorAll(".logout-link");

  logoutLinks.forEach(function(link) {
    link.addEventListener("click", function(event) {
      event.preventDefault();

      localStorage.removeItem("usuarioLogado");

      window.location.href = "index.html";
    });
  });
}

function preencherPerfilLogado() {
  const usuario = obterUsuarioLogado();

  if (!usuario) {
    return;
  }

  const nomeLogado = document.getElementById("nomeUsuarioLogado");
  const emailLogado = document.getElementById("emailUsuarioLogado");
  const idadeLogada = document.getElementById("idadeUsuarioLogado");

  if (nomeLogado) {
    nomeLogado.textContent = usuario.nome || "Usuário";
  }

  if (emailLogado) {
    emailLogado.textContent = usuario.email || "E-mail não informado";
  }

  if (idadeLogada) {
    idadeLogada.textContent = usuario.idade || "Não informada";
  }
}

protegerPagina();
mostrarUsuarioNaSidebar();
configurarSair();
preencherPerfilLogado();