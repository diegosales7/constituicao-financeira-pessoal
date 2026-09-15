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

      mostrarMensagem("mensagemLogin", "Login realizado com sucesso!", "success");

      const params = new URLSearchParams(window.location.search);
      const context = params.get('context');
      const destino = context === 'pj' ? 'empresa.html' : 'dashboard.html';

      setTimeout(function() {
        window.location.href = destino;
      }, 600);

    } catch (error) {
      const loginLocal = fazerLoginLocalPF(email, senha);

      if (loginLocal) {
        mostrarMensagem("mensagemLogin", "Login local realizado com sucesso!", "success");
        setTimeout(function() {
          window.location.href = "dashboard.html";
        }, 600);
        return;
      }

      mostrarMensagem("mensagemLogin", "E-mail ou senha inválidos para o ambiente PF.", "error");
      console.error("Erro no login:", error);
    }
  });
}