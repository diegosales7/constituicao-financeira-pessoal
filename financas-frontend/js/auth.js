// =========================================================
// AUTH.JS
// Login e cadastro integrados com o backend
// =========================================================

function mostrarMensagem(idElemento, texto, tipo) {
  const elemento = document.getElementById(idElemento);

  if (!elemento) {
    alert(texto);
    return;
  }

  elemento.textContent = texto;
  elemento.className = "auth-message " + tipo;
}

const DEMO_USER_PF = {
  nome: "Usuário Demo PF",
  email: "demo.pf@teste.com",
  idade: 32,
  senha: "demo123"
};

function garantirUsuarioDemoPF() {
  const usuarios = obterUsuariosSalvos();
  const jaExiste = usuarios.some((item) => item.email && item.email.toLowerCase() === DEMO_USER_PF.email.toLowerCase());

  if (!jaExiste) {
    usuarios.push({
      nome: DEMO_USER_PF.nome,
      email: DEMO_USER_PF.email,
      idade: DEMO_USER_PF.idade,
      senha: DEMO_USER_PF.senha
    });
    localStorage.setItem("usuariosCadastrados", JSON.stringify(usuarios));
  }

  return DEMO_USER_PF;
}

function resolveAppBasePath() {
  const pathname = window.location.pathname || "/";
  const lastSlash = pathname.lastIndexOf("/");

  if (lastSlash <= 0) {
    return "/";
  }

  return pathname.substring(0, lastSlash + 1);
}

function irParaPagina(page) {
  const basePath = resolveAppBasePath();
  const alvo = page.startsWith("/") ? page : `${basePath}${page}`;
  window.location.assign(alvo);
}

function logarUsuarioDemoPF() {
  const usuarioLogado = {
    nome: DEMO_USER_PF.nome,
    email: DEMO_USER_PF.email,
    idade: DEMO_USER_PF.idade,
    tipo: "pf"
  };

  try {
    localStorage.setItem("usuarioLogado", JSON.stringify(usuarioLogado));
    localStorage.setItem("modoDemo", "true");
  } catch (error) {
    console.error("Não foi possível gravar sessão demo PF:", error);
  }

  return usuarioLogado;
}

function obterUsuariosSalvos() {
  try {
    const dados = JSON.parse(localStorage.getItem("usuariosCadastrados") || "[]");
    return Array.isArray(dados) ? dados : [];
  } catch (error) {
    return [];
  }
}

function salvarUsuarioLocal(usuario) {
  const usuarios = obterUsuariosSalvos();
  const jaExiste = usuarios.some((item) => item.email && item.email.toLowerCase() === usuario.email.toLowerCase());

  if (!jaExiste) {
    usuarios.push(usuario);
    localStorage.setItem("usuariosCadastrados", JSON.stringify(usuarios));
  }
}

function fazerLoginLocalPF(email, senha) {
  const usuarios = obterUsuariosSalvos();
  const usuario = usuarios.find((item) => item.email && item.email.toLowerCase() === email.toLowerCase() && item.senha === senha);

  if (!usuario) {
    return null;
  }

  const usuarioLogado = {
    nome: usuario.nome,
    email: usuario.email,
    idade: usuario.idade,
    tipo: "pf"
  };

  localStorage.setItem("usuarioLogado", JSON.stringify(usuarioLogado));
  return usuarioLogado;
}

// =========================================================
// CADASTRO
// =========================================================

garantirUsuarioDemoPF();

function tentarLoginDemoPFSeHabilitado() {
  const params = new URLSearchParams(window.location.search);

  if (params.get("demo") !== "pf") {
    return false;
  }

  const loginLocal = fazerLoginLocalPF(DEMO_USER_PF.email, DEMO_USER_PF.senha);

  if (loginLocal) {
    setTimeout(function() {
      irParaPagina("dashboard.html");
    }, 200);
    return true;
  }

  return false;
}

const formCadastro = document.getElementById("formCadastro");

if (formCadastro) {
  formCadastro.addEventListener("submit", async function(event) {
    event.preventDefault();

    const nome = document.getElementById("cadastroNome").value.trim();
    const email = document.getElementById("cadastroEmail").value.trim().toLowerCase();
    const idade = Number(document.getElementById("cadastroIdade").value);
    const senha = document.getElementById("cadastroSenha").value;
    const confirmarSenha = document.getElementById("cadastroConfirmarSenha").value;

    if (!nome || !email || !idade || !senha || !confirmarSenha) {
      mostrarMensagem("mensagemCadastro", "Preencha todos os campos.", "error");
      return;
    }

    if (senha.length < 4) {
      mostrarMensagem("mensagemCadastro", "A senha precisa ter pelo menos 4 caracteres.", "error");
      return;
    }

    if (senha !== confirmarSenha) {
      mostrarMensagem("mensagemCadastro", "As senhas não conferem.", "error");
      return;
    }

    const novoUsuario = {
      nome: nome,
      email: email,
      idade: idade,
      senha: senha
    };

    try {
      const resposta = await apiRequest("/auth/register", "POST", novoUsuario);

      localStorage.setItem("usuarioLogado", JSON.stringify({
        nome: resposta.nome,
        email: resposta.email,
        idade: resposta.idade,
        tipo: "pf"
      }));

      salvarUsuarioLocal({
        nome: resposta.nome,
        email: resposta.email,
        idade: resposta.idade,
        senha: senha
      });

      mostrarMensagem("mensagemCadastro", "Cadastro realizado com sucesso e salvo no banco!", "success");

      localStorage.setItem('onboardingCompleto', 'false');

      setTimeout(function() {
        window.location.href = "onboarding.html";
      }, 800);

    } catch (error) {
      salvarUsuarioLocal(novoUsuario);
      localStorage.setItem("usuarioLogado", JSON.stringify({
        nome: novoUsuario.nome,
        email: novoUsuario.email,
        idade: novoUsuario.idade,
        tipo: "pf"
      }));

      mostrarMensagem("mensagemCadastro", "Cadastro local realizado com sucesso!", "success");

      localStorage.setItem('onboardingCompleto', 'false');

      setTimeout(function() {
        window.location.href = "onboarding.html";
      }, 800);
    }
  });
}

// =========================================================
// LOGIN
// =========================================================

const formLogin = document.getElementById("formLogin");

if (formLogin) {
  formLogin.addEventListener("submit", async function(event) {
    event.preventDefault();

    const email = document.getElementById("loginEmail").value.trim().toLowerCase();
    const senha = document.getElementById("loginSenha").value;

    if (!email || !senha) {
      mostrarMensagem("mensagemLogin", "Preencha e-mail e senha.", "error");
      return;
    }

    const login = {
      email: email,
      senha: senha
    };

    try {
      const resposta = await apiRequest("/auth/login", "POST", login);

      localStorage.setItem("usuarioLogado", JSON.stringify({
        nome: resposta.nome,
        email: resposta.email,
        idade: resposta.idade,
        tipo: "pf"
      }));
      localStorage.setItem("modoDemo", "false");

      mostrarMensagem("mensagemLogin", "Login realizado com sucesso!", "success");

      const params = new URLSearchParams(window.location.search);
      const context = params.get('context');
      const destino = context === 'pj' ? 'empresa.html' : 'dashboard.html';
 
      setTimeout(function() {
        irParaPagina(destino);
      }, 600);

    } catch (error) {
      if (email === DEMO_USER_PF.email && senha === DEMO_USER_PF.senha) {
        logarUsuarioDemoPF();
        mostrarMensagem("mensagemLogin", "Login de demonstração realizado com sucesso!", "success");
        setTimeout(function() {
          irParaPagina("dashboard.html");
        }, 600);
        return;
      }

      const loginLocal = fazerLoginLocalPF(email, senha);

      if (loginLocal) {
        localStorage.setItem("modoDemo", "false");
        mostrarMensagem("mensagemLogin", "Login local realizado com sucesso!", "success");
        setTimeout(function() {
          irParaPagina("dashboard.html");
        }, 600);
        return;
      }

      mostrarMensagem("mensagemLogin", "E-mail ou senha inválidos para o ambiente PF.", "error");
      console.error("Erro no login:", error);
    }
  });
}

if (document.getElementById("formLogin")) {
  tentarLoginDemoPFSeHabilitado();
}