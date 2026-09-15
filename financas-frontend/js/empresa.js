const STORAGE_KEY_EMPRESA = 'empresaPJ';

function obterEmpresaPJ() {
  const armazenado = localStorage.getItem(STORAGE_KEY_EMPRESA);

  if (!armazenado) {
    return {
      razaoSocial: '',
      nomeFantasia: '',
      cnpj: '',
      segmento: '',
      setor: '',
      dataFundacao: '',
      porte: '',
      capitalSocial: '',
      endereco: '',
      cidadeUf: '',
      responsavelFinanceiro: '',
      emailCorporativo: '',
      politicaFinanceira: ''
    };
  }

  try {
    return JSON.parse(armazenado);
  } catch (error) {
    return {};
  }
}

function atualizarResumoEmpresa() {
  const empresa = obterEmpresaPJ();
  const resumo = document.getElementById('empresaResumo');

  if (!resumo) {
    return;
  }

  const nomeEmpresa = empresa.razaoSocial || empresa.nomeFantasia || 'Empresa ainda não cadastrada';
  const cidadeUf = empresa.cidadeUf || 'Local não informado';
  const responsavel = empresa.responsavelFinanceiro || 'Responsável não informado';
  const cnpj = empresa.cnpj || 'CNPJ não informado';

  resumo.innerHTML = `
    <div class="pj-company-line">
      <strong>${nomeEmpresa}</strong>
      <span class="pj-badge pj-badge-info">${empresa.porte || 'Porte não definido'}</span>
    </div>
    <div class="pj-company-meta">
      <span>📍 ${cidadeUf}</span>
      <span>🏢 ${cnpj}</span>
      <span>👤 ${responsavel}</span>
    </div>
  `;
}

function preencherEmpresa() {
  const empresa = obterEmpresaPJ();

  const campos = [
    'razaoSocial',
    'nomeFantasia',
    'cnpj',
    'segmento',
    'setor',
    'dataFundacao',
    'porte',
    'capitalSocial',
    'endereco',
    'cidadeUf',
    'responsavelFinanceiro',
    'emailCorporativo',
    'politicaFinanceira'
  ];

  campos.forEach((campo) => {
    const elemento = document.getElementById(campo);
    if (elemento) {
      elemento.value = empresa[campo] || '';
    }
  });

  atualizarResumoEmpresa();
}

function salvarEmpresa(event) {
  if (event) {
    event.preventDefault();
  }

  const empresa = {
    razaoSocial: document.getElementById('razaoSocial')?.value.trim() || '',
    nomeFantasia: document.getElementById('nomeFantasia')?.value.trim() || '',
    cnpj: document.getElementById('cnpj')?.value.trim() || '',
    segmento: document.getElementById('segmento')?.value.trim() || '',
    setor: document.getElementById('setor')?.value.trim() || '',
    dataFundacao: document.getElementById('dataFundacao')?.value || '',
    porte: document.getElementById('porte')?.value || '',
    capitalSocial: document.getElementById('capitalSocial')?.value || '',
    endereco: document.getElementById('endereco')?.value.trim() || '',
    cidadeUf: document.getElementById('cidadeUf')?.value.trim() || '',
    responsavelFinanceiro: document.getElementById('responsavelFinanceiro')?.value.trim() || '',
    emailCorporativo: document.getElementById('emailCorporativo')?.value.trim() || '',
    politicaFinanceira: document.getElementById('politicaFinanceira')?.value.trim() || ''
  };

  localStorage.setItem(STORAGE_KEY_EMPRESA, JSON.stringify(empresa));
  atualizarResumoEmpresa();

  const botao = document.querySelector('#formEmpresa button[type="submit"]');
  if (botao) {
    const textoOriginal = botao.textContent;
    botao.textContent = 'Dados salvos';
    setTimeout(() => {
      botao.textContent = textoOriginal;
    }, 1200);
  }
}

const formEmpresa = document.getElementById('formEmpresa');
if (formEmpresa) {
  formEmpresa.addEventListener('submit', salvarEmpresa);
}

preencherEmpresa();
