const DEMO_USER_PJ = {
  razaoSocial: 'Empresa Demo LTDA',
  nomeFantasia: 'Empresa Demo',
  cnpj: '11.111.111/0001-11',
  ramo: 'Consultoria',
  porte: 'Médio',
  endereco: 'Av. Paulista, 1000 - São Paulo/SP',
  cidadeUf: 'São Paulo/SP',
  responsavelFinanceiro: 'Gestor Demo',
  cargo: 'Financeiro',
  emailCorporativo: 'demo.pj@empresa.com.br',
  telefone: '(11) 98765-4321',
  politicaFinanceira: 'Crescimento saudável e enquadramento financeiro',
  senha: 'demo123'
};

function garantirEmpresaDemoPj() {
  const empresaAtual = JSON.parse(localStorage.getItem('empresaPJ') || 'null');

  if (!empresaAtual || !empresaAtual.emailCorporativo) {
    localStorage.setItem('empresaPJ', JSON.stringify(DEMO_USER_PJ));
  }

  return JSON.parse(localStorage.getItem('empresaPJ') || JSON.stringify(DEMO_USER_PJ));
}

function resolveAppBasePathPj() {
  const pathname = window.location.pathname || '/';
  const lastSlash = pathname.lastIndexOf('/');

  if (lastSlash <= 0) {
    return '/';
  }

  return pathname.substring(0, lastSlash + 1);
}

function irParaPaginaPj(page) {
  const basePath = resolveAppBasePathPj();
  const alvo = page.startsWith('/') ? page : `${basePath}${page}`;
  window.location.assign(alvo);
}

function logarUsuarioDemoPj() {
  const empresa = garantirEmpresaDemoPj();
  const usuarioLogado = {
    nome: empresa.responsavelFinanceiro || 'Responsável financeiro',
    email: empresa.emailCorporativo,
    tipo: 'pj',
    empresa: empresa.razaoSocial || 'Empresa PJ'
  };

  try {
    localStorage.setItem('usuarioLogado', JSON.stringify(usuarioLogado));
    localStorage.setItem('modoDemo', 'true');
  } catch (error) {
    console.error('Não foi possível gravar sessão demo PJ:', error);
  }

  return usuarioLogado;
}

function mostrarMensagemPj(idElemento, texto, tipo) {
  const elemento = document.getElementById(idElemento);

  if (!elemento) {
    alert(texto);
    return;
  }

  elemento.textContent = texto;
  elemento.className = 'auth-message ' + tipo;
}

function aplicarEstadoCampo(idElemento, valido) {
  const elemento = document.getElementById(idElemento);

  if (!elemento) {
    return;
  }

  elemento.classList.toggle('invalid', !valido);
}

function limparNumeros(valor) {
  return (valor || '').replace(/\D/g, '');
}

function formatarCnpj(valor) {
  const numerico = limparNumeros(valor).slice(0, 14);

  return numerico
    .replace(/^(\d{2})(\d)/, '$1.$2')
    .replace(/^(\d{2})\.(\d{3})(\d)/, '$1.$2.$3')
    .replace(/^(\d{2})\.(\d{3})\.(\d{3})(\d)/, '$1.$2.$3/$4')
    .replace(/^(\d{2})\.(\d{3})\.(\d{3})\/(\d{4})(\d)/, '$1.$2.$3/$4-$5');
}

function formatarTelefone(valor) {
  const numerico = limparNumeros(valor).slice(0, 11);

  if (numerico.length <= 10) {
    return numerico
      .replace(/^(\d{2})(\d)/, '($1) $2')
      .replace(/^(\(\d{2}\))\s?(\d{4})(\d)/, '$1 $2-$3');
  }

  return numerico
    .replace(/^(\d{2})(\d)/, '($1) $2')
    .replace(/^(\(\d{2}\))\s?(\d{5})(\d)/, '$1 $2-$3');
}

function validarCnpj(cnpj) {
  const numeros = limparNumeros(cnpj);

  if (numeros.length !== 14) {
    return false;
  }

  if (/^(\d)\1+$/.test(numeros)) {
    return false;
  }

  let tamanho = numeros.length - 2;
  let numerosBase = numeros.substring(0, tamanho);
  const digitos = numeros.substring(tamanho);
  let soma = 0;
  let pos = tamanho - 7;

  for (let i = tamanho; i >= 1; i--) {
    soma += Number(numerosBase.charAt(tamanho - i)) * pos--;
    if (pos < 2) {
      pos = 9;
    }
  }

  let primeiroDigito = soma % 11 < 2 ? 0 : 11 - (soma % 11);
  if (primeiroDigito !== Number(digitos.charAt(0))) {
    return false;
  }

  tamanho = tamanho + 1;
  numerosBase = numeros.substring(0, tamanho);
  soma = 0;
  pos = tamanho - 7;

  for (let i = tamanho; i >= 1; i--) {
    soma += Number(numerosBase.charAt(tamanho - i)) * pos--;
    if (pos < 2) {
      pos = 9;
    }
  }

  const segundoDigito = soma % 11 < 2 ? 0 : 11 - (soma % 11);
  return segundoDigito === Number(digitos.charAt(1));
}

function validarTelefone(telefone) {
  const numeros = limparNumeros(telefone);
  return numeros.length >= 10 && numeros.length <= 11;
}

function validarEmail(email) {
  return /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/.test(email);
}

function inicializarMascaraPj() {
  const cnpjInput = document.getElementById('cadastroCnpj');
  const telefoneInput = document.getElementById('cadastroTelefoneCorp');

  if (cnpjInput) {
    cnpjInput.addEventListener('input', function () {
      const valorFormatado = formatarCnpj(this.value);
      this.value = valorFormatado;
      aplicarEstadoCampo('cadastroCnpj', validarCnpj(this.value) || this.value.length === 0);
    });
  }

  if (telefoneInput) {
    telefoneInput.addEventListener('input', function () {
      const valorFormatado = formatarTelefone(this.value);
      this.value = valorFormatado;
      aplicarEstadoCampo('cadastroTelefoneCorp', validarTelefone(this.value) || this.value.length === 0);
    });
  }
}

garantirEmpresaDemoPj();

function tentarLoginDemoPjSeHabilitado() {
  const params = new URLSearchParams(window.location.search);

  if (params.get('demo') !== 'pj') {
    return false;
  }

  logarUsuarioDemoPj();

  setTimeout(function () {
    irParaPaginaPj('empresa.html');
  }, 200);

  return true;
}

const formCadastroPj = document.getElementById('formCadastroPj');

if (formCadastroPj) {
  inicializarMascaraPj();

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

    const campos = [
      { id: 'cadastroEmpresaNome', valor: razaoSocial, mensagem: 'Informe a razão social da empresa.' },
      { id: 'cadastroCnpj', valor: cnpj, mensagem: 'Informe um CNPJ válido da empresa.' },
      { id: 'cadastroEndereco', valor: endereco, mensagem: 'Informe o endereço da empresa.' },
      { id: 'cadastroCidadeUf', valor: cidadeUf, mensagem: 'Informe a cidade e UF da empresa.' },
      { id: 'cadastroResponsavelNome', valor: responsavelNome, mensagem: 'Informe o nome completo do responsável financeiro.' },
      { id: 'cadastroCargo', valor: cargo, mensagem: 'Informe o cargo do responsável financeiro.' },
      { id: 'cadastroEmailCorp', valor: emailCorporativo, mensagem: 'Informe um e-mail corporativo válido.' },
      { id: 'cadastroTelefoneCorp', valor: telefone, mensagem: 'Informe um telefone corporativo válido.' }
    ];

    let primeiraMensagem = '';

    campos.forEach(({ id, valor, mensagem }) => {
      const valido = !!valor;
      aplicarEstadoCampo(id, valido);

      if (!valido && !primeiraMensagem) {
        primeiraMensagem = mensagem;
      }
    });

    if (!razaoSocial || razaoSocial.length < 3) {
      aplicarEstadoCampo('cadastroEmpresaNome', false);
      mostrarMensagemPj('mensagemCadastroPj', 'A razão social deve conter pelo menos 3 caracteres.', 'error');
      return;
    }

    if (!validarCnpj(cnpj)) {
      aplicarEstadoCampo('cadastroCnpj', false);
      mostrarMensagemPj('mensagemCadastroPj', 'Informe um CNPJ válido para registrar a empresa.', 'error');
      return;
    }

    if (!endereco || endereco.length < 8) {
      aplicarEstadoCampo('cadastroEndereco', false);
      mostrarMensagemPj('mensagemCadastroPj', 'O endereço da empresa deve conter informações reais e completas.', 'error');
      return;
    }

    if (!cidadeUf || cidadeUf.length < 4) {
      aplicarEstadoCampo('cadastroCidadeUf', false);
      mostrarMensagemPj('mensagemCadastroPj', 'Informe a cidade e a UF da empresa corretamente.', 'error');
      return;
    }

    if (!responsavelNome || responsavelNome.length < 3) {
      aplicarEstadoCampo('cadastroResponsavelNome', false);
      mostrarMensagemPj('mensagemCadastroPj', 'O nome do responsável financeiro deve ser válido.', 'error');
      return;
    }

    if (!cargo || cargo.length < 3) {
      aplicarEstadoCampo('cadastroCargo', false);
      mostrarMensagemPj('mensagemCadastroPj', 'Informe o cargo do responsável financeiro.', 'error');
      return;
    }

    if (!validarEmail(emailCorporativo)) {
      aplicarEstadoCampo('cadastroEmailCorp', false);
      mostrarMensagemPj('mensagemCadastroPj', 'O e-mail corporativo informado não é válido.', 'error');
      return;
    }

    if (!validarTelefone(telefone)) {
      aplicarEstadoCampo('cadastroTelefoneCorp', false);
      mostrarMensagemPj('mensagemCadastroPj', 'Informe um telefone com DDD e número válidos.', 'error');
      return;
    }

    if (!senha || !confirmarSenha) {
      mostrarMensagemPj('mensagemCadastroPj', 'Digite e confirme a senha de acesso da empresa.', 'error');
      return;
    }

    if (senha.length < 6) {
      mostrarMensagemPj('mensagemCadastroPj', 'A senha precisa ter pelo menos 6 caracteres.', 'error');
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

    mostrarMensagemPj('mensagemCadastroPj', 'Cadastro PJ realizado com sucesso! Dados reais e seguros cadastrados.', 'success');

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

    let empresaPJ = JSON.parse(localStorage.getItem('empresaPJ') || 'null');

    if (!empresaPJ || !empresaPJ.emailCorporativo || !empresaPJ.senha) {
      empresaPJ = DEMO_USER_PJ;
      localStorage.setItem('empresaPJ', JSON.stringify(empresaPJ));
    }

    if (email === DEMO_USER_PJ.emailCorporativo && senha === DEMO_USER_PJ.senha) {
      logarUsuarioDemoPj();
      mostrarMensagemPj('mensagemLogin', 'Login de demonstração realizado com sucesso!', 'success');

      setTimeout(function () {
        irParaPaginaPj('empresa.html');
      }, 700);
      return;
    }

    if (empresaPJ.emailCorporativo === email && empresaPJ.senha === senha) {
      localStorage.setItem('usuarioLogado', JSON.stringify({
        nome: empresaPJ.responsavelFinanceiro || 'Responsável financeiro',
        email: empresaPJ.emailCorporativo,
        tipo: 'pj',
        empresa: empresaPJ.razaoSocial || 'Empresa PJ'
      }));
      localStorage.setItem('modoDemo', 'false');

      mostrarMensagemPj('mensagemLogin', 'Login realizado com sucesso!', 'success');

      setTimeout(function () {
        irParaPaginaPj('empresa.html');
      }, 700);
    } else {
      mostrarMensagemPj('mensagemLogin', 'E-mail ou senha inválidos para o ambiente PJ.', 'error');
    }
  });
}

if (document.getElementById('formLoginPj')) {
  tentarLoginDemoPjSeHabilitado();
}
