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
        idade: resposta.idade
      }));

      mostrarMensagem("mensagemCadastro", "Cadastro realizado com sucesso e salvo no banco!", "success");

      // Marcar que é primeiro acesso (sem ter completado onboarding)
      localStorage.setItem('onboardingCompleto', 'false');

      setTimeout(function() {
        window.location.href = "onboarding.html";
      }, 800);

    } catch (error) {
      mostrarMensagem("mensagemCadastro", error.message, "error");
      console.error("Erro no cadastro:", error);
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
        idade: resposta.idade
      }));

      mostrarMensagem("mensagemLogin", "Login realizado com sucesso!", "success");

      setTimeout(function() {
        window.location.href = "dashboard.html";
      }, 600);

    } catch (error) {
      mostrarMensagem("mensagemLogin", error.message, "error");
      console.error("Erro no login:", error);
    }
  });
}