function formatarMoeda(valor) {
  return new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL'
  }).format(Number(valor || 0));
}

function lerDadosPJ(chave, padrao) {
  try {
    const valor = localStorage.getItem(chave);
    if (!valor) {
      return padrao;
    }
    return JSON.parse(valor);
  } catch (error) {
    return padrao;
  }
}

function salvarDadosPJ(chave, valor) {
  localStorage.setItem(chave, JSON.stringify(valor));
}

function obterEmpresaPJ() {
  return lerDadosPJ('empresaPJ', {
    razaoSocial: 'Empresa ainda não cadastrada',
    nomeFantasia: '',
    cnpj: '',
    porte: '',
    cidadeUf: '',
    responsavelFinanceiro: '',
    emailCorporativo: ''
  });
}

function obterTesourariaPJ() {
  return lerDadosPJ('tesourariaPJ', {
    caixaDisponivel: 0,
    contasReceber: 0,
    contasPagar: 0,
    saldoMinimo: 0,
    prazoRecebimento: 0,
    prazoPagamento: 0,
    risco: 'medio',
    observacao: ''
  });
}

function obterFluxoCaixaPJ() {
  const padrao = {
    entradas: [],
    saidas: [],
    receitas: 0,
    despesas: 0,
    observacao: ''
  };

  const fluxo = lerDadosPJ('fluxoCaixaPJ', padrao);
  if (!Array.isArray(fluxo.entradas)) fluxo.entradas = [];
  if (!Array.isArray(fluxo.saidas)) fluxo.saidas = [];
  if (!Array.isArray(fluxo.receitasLista)) fluxo.receitasLista = [];
  if (!Array.isArray(fluxo.despesasLista)) fluxo.despesasLista = [];
  return fluxo;
}

function obterCapitalGiroPJ() {
  return lerDadosPJ('capitalGiroPJ', {
    estoque: 0,
    clientes: 0,
    fornecedores: 0,
    prazoRecebimento: 0,
    prazoPagamento: 0,
    cicloOperacional: 0,
    observacao: ''
  });
}

function obterRiscosPJ() {
  return lerDadosPJ('riscosPJ', {
    liquidez: 'medio',
    credito: 'medio',
    mercado: 'baixo',
    operacional: 'medio',
    mitigacao: ''
  });
}

function obterDadosFinanceirosPJ() {
  const empresa = obterEmpresaPJ();
  const tesouraria = obterTesourariaPJ();
  const fluxo = obterFluxoCaixaPJ();
  const capital = obterCapitalGiroPJ();
  const risco = obterRiscosPJ();

  const receitasLista = Array.isArray(fluxo.receitasLista) ? fluxo.receitasLista : [];
  const despesasLista = Array.isArray(fluxo.despesasLista) ? fluxo.despesasLista : [];

  const totalReceitas = receitasLista.length
    ? receitasLista.reduce((soma, item) => soma + Number(item.receitas !== undefined && item.receitas !== null ? item.receitas : item.valor || 0), 0)
    : Number(fluxo.receitas || 0);

  const totalDespesas = despesasLista.length
    ? despesasLista.reduce((soma, item) => soma + Number(item.despesas !== undefined && item.despesas !== null ? item.despesas : item.valor || 0), 0)
    : Number(fluxo.despesas || 0);

  const caixaDisponivel = Number(tesouraria.caixaDisponivel || 0);
  const contasReceber = Number(tesouraria.contasReceber || capital.clientes || 0);
  const contasPagar = Number(tesouraria.contasPagar || capital.fornecedores || 0);
  const saldoMinimo = Number(tesouraria.saldoMinimo || 0);
  const saldo = totalReceitas - totalDespesas;
  const cobertura = totalDespesas > 0 ? (totalReceitas / totalDespesas) : 0;
  const margem = totalReceitas > 0 ? ((saldo / totalReceitas) * 100) : 0;
  const endividamento = totalReceitas > 0 ? ((contasPagar / totalReceitas) * 100) : 0;
  const liquidez = saldoMinimo > 0 ? (caixaDisponivel / saldoMinimo) : 0;
  const giroDias = Number(capital.cicloOperacional || 0);

  let nivelRisco = 'Baixo';
  if (risco.liquidez === 'alto' || risco.credito === 'alto' || risco.mercado === 'alto' || risco.operacional === 'alto') {
    nivelRisco = 'Alto';
  } else if (risco.liquidez === 'medio' || risco.credito === 'medio' || risco.mercado === 'medio' || risco.operacional === 'medio') {
    nivelRisco = 'Médio';
  }

  return {
    empresa,
    tesouraria,
    fluxo,
    capital,
    risco,
    receitas: totalReceitas,
    despesas: totalDespesas,
    saldo,
    cobertura,
    margem,
    endividamento,
    liquidez,
    giroDias,
    nivelRisco,
    coberturas: cobertura > 1.8 ? 'Adequada' : cobertura > 1 ? 'Moderada' : 'Baixa',
    exposicao: saldo >= 0 ? 'Baixa' : 'Alta',
    liquidezTexto: liquidez > 1.5 ? 'Estável' : liquidez > 0.8 ? 'Atenta' : 'Crítica',
    atencao: saldo >= 0 ? 'Contínua' : 'Urgente'
  };
}

function atualizarDadosPJ() {
  const dados = obterDadosFinanceirosPJ();

  const campos = {
    pjReceitasTotal: formatarMoeda(dados.receitas),
    pjDespesasTotal: formatarMoeda(dados.despesas),
    pjSaldoLiquido: formatarMoeda(dados.saldo),
    pjCobertura: dados.cobertura > 0 ? `${dados.cobertura.toFixed(1)}x` : '0,0x',
    pjGiroDias: `${Math.max(0, Math.round(dados.giroDias || 0))} dias`,
    pjEstoque: formatarMoeda(Number(dados.capital.estoque || 0)),
    pjClientes: formatarMoeda(Number(dados.capital.clientes || dados.tesouraria.contasReceber || 0)),
    pjFornecedores: formatarMoeda(Number(dados.capital.fornecedores || dados.tesouraria.contasPagar || 0)),
    pjExposicao: dados.exposicao,
    pjCredito: dados.coberturas,
    pjLiquidez: dados.liquidezTexto,
    pjAtencao: dados.atencao,
    pjLiquidezIndicador: `${dados.liquidez > 0 ? dados.liquidez.toFixed(1) : '0,0'}x`,
    pjMargem: `${dados.margem >= 0 ? dados.margem.toFixed(1) : '0,0'}%`,
    pjEndividamento: `${dados.endividamento > 0 ? Math.min(100, dados.endividamento).toFixed(0) : '0'}%`,
    pjCiclo: `${Math.max(0, Math.round(dados.giroDias || 0))}d`,
    caixaDisponivel: formatarMoeda(Number(dados.tesouraria.caixaDisponivel || 0)),
    contasReceber: formatarMoeda(Number(dados.tesouraria.contasReceber || dados.capital.clientes || 0)),
    contasPagar: formatarMoeda(Number(dados.tesouraria.contasPagar || dados.capital.fornecedores || 0)),
    liquidezResultado: `${dados.liquidez > 0 ? dados.liquidez.toFixed(1) : '0,0'}x`,
    empresaNomeTesouraria: dados.empresa.razaoSocial || 'Empresa',
    valorRiscoGeral: dados.nivelRisco,
    valorIndicadorLiquidez: `${dados.liquidez > 0 ? dados.liquidez.toFixed(1) : '0,0'}x`,
    valorIndicadorMargem: `${dados.margem >= 0 ? dados.margem.toFixed(1) : '0,0'}%`,
    valorIndicadorEndividamento: `${dados.endividamento > 0 ? Math.min(100, dados.endividamento).toFixed(0) : '0'}%`
  };

  Object.entries(campos).forEach(([id, valor]) => {
    const el = document.getElementById(id);
    if (el) {
      el.textContent = valor;
    }
  });
}

function registrarFluxoCaixaPJ(dados) {
  const fluxo = obterFluxoCaixaPJ();
  const id = Date.now();
  const item = {
    id,
    mes: dados.mes || new Date().toISOString().slice(0, 7),
    receitas: Number(dados.receitas || 0),
    despesas: Number(dados.despesas || 0),
    investimentos: Number(dados.investimentos || 0),
    observacao: dados.observacao || ''
  };

  fluxo.receitasLista.push(item);
  fluxo.despesasLista.push({
    id,
    valor: Number(dados.despesas || 0),
    despesas: Number(dados.despesas || 0),
    mes: item.mes,
    observacao: item.observacao
  });

  fluxo.receitas = fluxo.receitasLista.reduce((soma, entry) => soma + Number(entry.receitas || 0), 0);
  fluxo.despesas = fluxo.despesasLista.reduce((soma, entry) => soma + Number(entry.despesas !== undefined && entry.despesas !== null ? entry.despesas : entry.valor || 0), 0);
  salvarDadosPJ('fluxoCaixaPJ', fluxo);
  atualizarDadosPJ();
}

function registrarCapitalGiroPJ(dados) {
  const capital = {
    estoque: Number(dados.estoque || 0),
    clientes: Number(dados.clientes || 0),
    fornecedores: Number(dados.fornecedores || 0),
    prazoRecebimento: Number(dados.prazoRecebimento || 0),
    prazoPagamento: Number(dados.prazoPagamento || 0),
    cicloOperacional: Number(dados.cicloOperacional || 0),
    observacao: dados.observacao || ''
  };

  salvarDadosPJ('capitalGiroPJ', capital);
  atualizarDadosPJ();
}

function registrarRiscoPJ(dados) {
  const risco = {
    liquidez: dados.liquidez || 'medio',
    credito: dados.credito || 'medio',
    mercado: dados.mercado || 'baixo',
    operacional: dados.operacional || 'medio',
    mitigacao: dados.mitigacao || ''
  };

  salvarDadosPJ('riscosPJ', risco);
  atualizarDadosPJ();
}

document.addEventListener('DOMContentLoaded', atualizarDadosPJ);
window.addEventListener('storage', atualizarDadosPJ);
window.atualizarDadosPJ = atualizarDadosPJ;
window.registrarFluxoCaixaPJ = registrarFluxoCaixaPJ;
window.registrarCapitalGiroPJ = registrarCapitalGiroPJ;
window.registrarRiscoPJ = registrarRiscoPJ;
