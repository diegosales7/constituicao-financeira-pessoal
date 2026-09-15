function mostrarMensagemPj(idElemento, texto, tipo) {
  const elemento = document.getElementById(idElemento);

  if (!elemento) {
    alert(texto);
    return;
  }

  elemento.textContent = texto;
  elemento.className = 'auth-message ' + tipo;
}

const formCadastroPj = document.getElementById('formCadastroPj');

if (formCadastroPj) {
  formCadastroPj.addEventListener('submit', function (event) {
    event.preventDefault();

    const razaoSocial = document.getElementById('cadastroEmpresaNome').value.trim();
    const nomeFantasia = document.getElementById('cadastroEmpresaFantasia').value.trim();
    const cnpj = document.getElementById('cadastroCnpj').value.trim();
    const ramo = document.getElementById('cadastroRamo').value.trim();
    const porte = document.getElementById('cadastroPorte').value.trim();
    const endereco = document.getElementById('cadastroEndereco').value.trim();
    const cidadeUf = document.getElementById('cadastroCidadeUf').value.trim();
    const responsavelNome = document.getElementById('cadastroResponsavelNome').value.trim();
    const cargo = document.getElementById('cadastroCargo').value.trim();
    const emailCorporativo = document.getElementById('cadastroEmailCorp').value.trim().toLowerCase();
    const telefone = document.getElementById('cadastroTelefoneCorp').value.trim();
    const senha = document.getElementById('cadastroSenhaPj').value;
    const confirmarSenha = document.getElementById('cadastroConfirmarSenhaPj').value;
    const politicaFinanceira = document.getElementById('cadastroPoliticaFinanceira').value.trim();

    if (!razaoSocial || !responsavelNome || !emailCorporativo || !senha || !confirmarSenha) {
      mostrarMensagemPj('mensagemCadastroPj', 'Preencha os campos obrigatórios da empresa e do responsável.', 'error');
      return;
    }

    if (senha.length < 4) {
      mostrarMensagemPj('mensagemCadastroPj', 'A senha precisa ter pelo menos 4 caracteres.', 'error');
      return;
    }

    if (senha !== confirmarSenha) {
      mostrarMensagemPj('mensagemCadastroPj', 'As senhas não conferem.', 'error');
      return;
    }

    const empresaPJ = {
      razaoSocial,
      nomeFantasia,
      cnpj,
      ramo,
      porte,
      endereco,
      cidadeUf,
      responsavelFinanceiro: responsavelNome,
      cargo,
      emailCorporativo,
      telefone,
      politicaFinanceira,
      senha,
      criadoEm: new Date().toISOString()
    };

    localStorage.setItem('empresaPJ', JSON.stringify(empresaPJ));
    localStorage.setItem('usuarioLogado', JSON.stringify({
      nome: responsavelNome,
      email: emailCorporativo,
      tipo: 'pj',
      empresa: razaoSocial
    }));

    mostrarMensagemPj('mensagemCadastroPj', 'Cadastro PJ realizado com sucesso!', 'success');

    setTimeout(function () {
      window.location.href = 'empresa.html';
    }, 800);
  });
}

const formLoginPj = document.getElementById('formLoginPj');

if (formLoginPj) {
  formLoginPj.addEventListener('submit', function (event) {
    event.preventDefault();

    const email = document.getElementById('loginEmailPj').value.trim().toLowerCase();
    const senha = document.getElementById('loginSenhaPj').value;

    if (!email || !senha) {
      mostrarMensagemPj('mensagemLogin', 'Informe e-mail e senha da empresa.', 'error');
      return;
    }

    const empresaPJ = JSON.parse(localStorage.getItem('empresaPJ') || '{}');

    if (!empresaPJ || !empresaPJ.emailCorporativo || !empresaPJ.senha) {
      mostrarMensagemPj('mensagemLogin', 'Nenhuma empresa PJ cadastrada com esse e-mail. Crie a conta antes de entrar.', 'error');
      return;
    }

    if (empresaPJ.emailCorporativo === email && empresaPJ.senha === senha) {
      localStorage.setItem('usuarioLogado', JSON.stringify({
        nome: empresaPJ.responsavelFinanceiro || 'Responsável financeiro',
        email: empresaPJ.emailCorporativo,
        tipo: 'pj',
        empresa: empresaPJ.razaoSocial || 'Empresa PJ'
      }));

      mostrarMensagemPj('mensagemLogin', 'Login realizado com sucesso!', 'success');

      setTimeout(function () {
        window.location.href = 'empresa.html';
      }, 700);
    } else {
      mostrarMensagemPj('mensagemLogin', 'E-mail ou senha inválidos para o ambiente PJ.', 'error');
    }
  });
}
