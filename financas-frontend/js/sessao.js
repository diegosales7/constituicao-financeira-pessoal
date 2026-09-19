// =========================================================
// SESSAO.JS
// Controla usuário logado, ambiente PF/PJ e sidebar
// =========================================================

function removerEmojisDoDom() {
  if (!document.body) {
    return;
  }

  const walker = document.createTreeWalker(document.body, NodeFilter.SHOW_TEXT, null, false);
  const emojiRegex = /[\u{1F300}-\u{1FAFF}]/gu;

  while (walker.nextNode()) {
    const node = walker.currentNode;
    const texto = node.textContent || "";

    if (emojiRegex.test(texto)) {
      node.textContent = texto.replace(emojiRegex, "");
    }
  }
}

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

function paginaAtual() {
  return window.location.pathname.split('/').pop().toLowerCase();
}

function ePaginaLivre() {
  const livre = [
    "index.html",
    "login.html",
    "cadastro.html",
    "login-pj.html",
    "cadastro-pj.html",
    "sobre.html"
  ];

  return livre.includes(paginaAtual());
}

function ePaginaPf() {
  const pf = [
    "dashboard.html",
    "perfil.html",
    "receitas.html",
    "despesas.html",
    "transacoes.html",
    "cartoes.html",
    "termometro.html",
    "fluxo-caixa.html",
    "emergencia.html",
    "reserva-flex.html",
    "ocorrencias.html",
    "ativos.html",
    "constituicao.html",
    "simulacao.html",
    "onboarding.html"
  ];

  return pf.includes(paginaAtual());
}

function ePaginaPj() {
  const pj = [
    "empresa.html",
    "tesouraria.html",
    "fluxo-caixa-pj.html",
    "capital-giro.html",
    "riscos.html",
    "indicadores.html"
  ];

  return pj.includes(paginaAtual());
}

function aplicarTemaAmbiente() {
  const body = document.body;

  if (!body) {
    return;
  }

  body.classList.remove("pf-theme", "pj-theme");

  if (ePaginaPj() || paginaAtual() === "login-pj.html" || paginaAtual() === "cadastro-pj.html") {
    body.classList.add("pj-theme");
    return;
  }

  body.classList.add("pf-theme");
}

function modoDemoAtivo() {
  return localStorage.getItem("modoDemo") === "true";
}

function resolveAppBasePathSessao() {
  const pathname = window.location.pathname || '/';
  const lastSlash = pathname.lastIndexOf('/');

  if (lastSlash <= 0) {
    return '/';
  }

  return pathname.substring(0, lastSlash + 1);
}

function irParaPaginaSessao(page) {
  const basePath = resolveAppBasePathSessao();
  const alvo = page.startsWith('/') ? page : `${basePath}${page}`;
  window.location.assign(alvo);
}

function protegerPagina() {
  const usuario = obterUsuarioLogado();

  if (modoDemoAtivo() && !usuario) {
    const pagina = paginaAtual();
    if (pagina === "login.html") {
      irParaPaginaSessao("dashboard.html");
      return;
    }
    if (pagina === "login-pj.html") {
      irParaPaginaSessao("empresa.html");
      return;
    }
  }

  if (usuario) {
    const pagina = paginaAtual();

    if (usuario.tipo === "pf" && (pagina === "index.html" || pagina === "login-pj.html" || pagina === "cadastro-pj.html" || pagina === "login.html" || pagina === "cadastro.html")) {
      irParaPaginaSessao("dashboard.html");
      return;
    }

    if (usuario.tipo === "pj" && (pagina === "index.html" || pagina === "login.html" || pagina === "cadastro.html" || pagina === "login-pj.html" || pagina === "cadastro-pj.html")) {
      irParaPaginaSessao("empresa.html");
      return;
    }
  }

  if (ePaginaLivre()) {
    aplicarTemaAmbiente();
    return;
  }

  if (!usuario) {
    irParaPaginaSessao("index.html");
    return;
  }

  if (usuario.tipo === "pj" && ePaginaPf()) {
    irParaPaginaSessao("empresa.html");
    return;
  }

  if (usuario.tipo === "pf" && ePaginaPj()) {
    irParaPaginaSessao("dashboard.html");
    return;
  }

  if (usuario.tipo === "pj" && !ePaginaPj() && !ePaginaLivre()) {
    irParaPaginaSessao("empresa.html");
    return;
  }

  if (usuario.tipo === "pf" && !ePaginaPf() && !ePaginaLivre()) {
    irParaPaginaSessao("dashboard.html");
    return;
  }

  if (usuario.tipo !== "pf" && usuario.tipo !== "pj") {
    irParaPaginaSessao("index.html");
    return;
  }

  aplicarTemaAmbiente();
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
    : { nome: "Estado normal" };

  const emergencia = typeof obterEstadoEmergencia === "function"
    ? obterEstadoEmergencia()
    : { ativo: false };

  const caixaUsuario = document.createElement("div");
  caixaUsuario.classList.add("sidebar-user");

  const perfilPf = `
    <div class="sidebar-user-header">
      <div class="sidebar-user-avatar">${(usuario.nome || "U").charAt(0).toUpperCase()}</div>
      <div>
        <div class="sidebar-user-label">Usuário</div>
        <div class="sidebar-user-name">${usuario.nome || "Usuário"}</div>
      </div>
    </div>
    <div class="sidebar-user-email">${usuario.email || "E-mail não informado"}</div>
    <div class="sidebar-user-age">Idade: ${usuario.idade || "Não informada"}</div>

    <div class="sidebar-state">
      <div class="sidebar-state-label">Estado financeiro</div>
      <div class="sidebar-state-value">${gatilho.nome}</div>
    </div>

    <div class="sidebar-state">
      <div class="sidebar-state-label">Emergência</div>
      <div class="sidebar-state-value ${emergencia.ativo ? "state-danger" : "state-normal"}">
        ${emergencia.ativo ? "Ativa" : "Inativa"}
      </div>
    </div>
  `;

  const perfilPj = `
    <div class="sidebar-user-header">
      <div class="sidebar-user-avatar">${(usuario.empresa || "E").charAt(0).toUpperCase()}</div>
      <div>
        <div class="sidebar-user-label">Empresa</div>
        <div class="sidebar-user-name">${usuario.empresa || "Empresa PJ"}</div>
      </div>
    </div>
    <div class="sidebar-user-email">${usuario.email || "E-mail corporativo"}</div>
    <div class="sidebar-user-age">Responsável: ${usuario.nome || "Gestor"}</div>

    <div class="sidebar-state">
      <div class="sidebar-state-label">Ambiente</div>
      <div class="sidebar-state-value">Empresa</div>
    </div>
  `;

  caixaUsuario.innerHTML = usuario.tipo === "pj" ? perfilPj : perfilPf;
  sidebar.insertBefore(caixaUsuario, nav);
}

function configurarSair() {
  const logoutLinks = document.querySelectorAll(".logout-link");

  logoutLinks.forEach(function(link) {
    link.addEventListener("click", function(event) {
      event.preventDefault();

      localStorage.removeItem("usuarioLogado");
      localStorage.removeItem("empresaPJ");
      localStorage.removeItem("modoDemo");
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

function inicializarPluginFinanceiro() {
  const usuario = obterUsuarioLogado();

  if (!usuario || !document.body || document.getElementById("financePluginRoot")) {
    return;
  }

  const root = document.createElement("div");
  root.id = "financePluginRoot";
  root.className = "finance-plugin";
  root.innerHTML = `
    <div id="financePluginModal" class="finance-plugin-modal">
      <div class="finance-plugin-header">
        <h3>Registro rápido</h3>
        <button class="finance-plugin-close" type="button" aria-label="Fechar">×</button>
      </div>

      <form id="financePluginForm" class="finance-plugin-form">
        <div class="finance-plugin-row">
          <div class="form-group" style="margin:0;">
            <label>Tipo</label>
            <select id="financePluginTipo" required>
              <option value="receita">Receita</option>
              <option value="despesa">Despesa</option>
            </select>
          </div>

          <div class="form-group" style="margin:0;">
            <label>Valor</label>
            <input type="number" id="financePluginValor" step="0.01" min="0.01" placeholder="0,00" required>
          </div>
        </div>

        <div class="form-group" style="margin:0;">
          <label>Descrição</label>
          <input type="text" id="financePluginDescricao" placeholder="Ex: salário, mercado, gás..." required>
        </div>

        <div class="finance-plugin-row">
          <div class="form-group" style="margin:0;">
            <label>Categoria</label>
            <input type="text" id="financePluginCategoria" placeholder="Ex: Salário / Alimentação" required>
          </div>

          <div class="form-group" style="margin:0;">
            <label>Data</label>
            <input type="date" id="financePluginData" required>
          </div>
        </div>

        <div class="finance-plugin-actions">
          <button type="submit" class="btn btn-primary">Salvar</button>
          <button type="button" class="btn btn-outline" id="financePluginCancelar">Fechar</button>
        </div>
      </form>
    </div>

    <button id="financePluginToggle" class="finance-plugin-toggle" type="button" aria-label="Adicionar receita ou despesa">+</button>
  `;

  document.body.appendChild(root);

  const modal = document.getElementById("financePluginModal");
  const toggle = document.getElementById("financePluginToggle");
  const closeBtn = document.querySelector(".finance-plugin-close");
  const cancelBtn = document.getElementById("financePluginCancelar");
  const form = document.getElementById("financePluginForm");

  function abrirModal() {
    modal.classList.add("open");
    setTimeout(function() {
      const input = document.getElementById("financePluginDescricao");
      if (input) { input.focus(); }
    }, 50);
  }

  function fecharModal() {
    modal.classList.remove("open");
    if (form) { form.reset(); }
    const inputData = document.getElementById("financePluginData");
    if (inputData) { inputData.valueAsDate = new Date(); }
  }

  toggle.addEventListener("click", abrirModal);
  closeBtn.addEventListener("click", fecharModal);
  cancelBtn.addEventListener("click", fecharModal);

  const inputData = document.getElementById("financePluginData");
  if (inputData) { inputData.valueAsDate = new Date(); }

  form.addEventListener("submit", function(event) {
    event.preventDefault();

    const tipo = document.getElementById("financePluginTipo").value;
    const valor = Number(document.getElementById("financePluginValor").value || 0);
    const descricao = document.getElementById("financePluginDescricao").value.trim();
    const categoria = document.getElementById("financePluginCategoria").value.trim();
    const data = document.getElementById("financePluginData").value;

    if (!descricao || !categoria || !data || valor <= 0) {
      alert("Preencha todos os campos corretamente antes de salvar.");
      return;
    }

    const chave = tipo === "receita" ? "receitas" : "despesas";
    const itens = JSON.parse(localStorage.getItem(chave) || "[]");

    const item = {
      id: Date.now(),
      data,
      descricao,
      valor,
      categoria,
      essencial: tipo === "despesa" ? "sim" : "nao"
    };

    if (tipo === "receita") {
      item.gatilho = "Receita";
      item.investimento = 0;
      item.reservaFlex = 0;
      item.vida = 0;
    }

    itens.push(item);
    localStorage.setItem(chave, JSON.stringify(itens));

    if (typeof atualizarResumoReceitas === "function") {
      atualizarResumoReceitas();
    }

    if (typeof atualizarTelaDespesas === "function") {
      atualizarTelaDespesas();
    }

    if (typeof renderizarTransacoes === "function") {
      renderizarTransacoes();
    }

    if (typeof carregarDashboard === "function") {
      carregarDashboard();
    }

    if (typeof atualizarDadosPJ === "function") {
      atualizarDadosPJ();
    }

    fecharModal();
    alert(tipo === "receita" ? "Receita salva com sucesso!" : "Despesa salva com sucesso!");
  });
}

removerEmojisDoDom();
protegerPagina();
mostrarUsuarioNaSidebar();
configurarSair();
preencherPerfilLogado();
inicializarPluginFinanceiro();