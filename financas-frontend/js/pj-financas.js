function formatarMoeda(valor) {
  return new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL'
  }).format(Number(valor || 0));
}

function obterDadosFinanceirosPJ() {
  const receitas = JSON.parse(localStorage.getItem('receitas') || '[]');
  const despesas = JSON.parse(localStorage.getItem('despesas') || '[]');

  const totalReceitas = receitas.reduce((soma, item) => soma + Number(item.valor || 0), 0);
  const totalDespesas = despesas.reduce((soma, item) => soma + Number(item.valor || 0), 0);
  const saldo = totalReceitas - totalDespesas;
  const cobertura = totalDespesas > 0 ? (totalReceitas / totalDespesas) : 0;
  const margem = totalReceitas > 0 ? ((saldo / totalReceitas) * 100) : 0;
  const giro = totalReceitas > 0 ? Math.max(10, Math.min(120, Math.round((totalDespesas / totalReceitas) * 60))) : 0;

  return {
    receitas: totalReceitas,
    despesas: totalDespesas,
    saldo,
    cobertura,
    margem,
    giro,
    coberturas: cobertura > 1.8 ? 'Adequada' : cobertura > 1 ? 'Moderada' : 'Baixa'
  };
}

function atualizarDadosPJ() {
  const dados = obterDadosFinanceirosPJ();

  const campos = {
    pjReceitasTotal: formatarMoeda(dados.receitas),
    pjDespesasTotal: formatarMoeda(dados.despesas),
    pjSaldoLiquido: formatarMoeda(dados.saldo),
    pjCobertura: dados.cobertura > 0 ? `${dados.cobertura.toFixed(1)}x` : '0,0x',
    pjGiroDias: `${dados.giro} dias`,
    pjEstoque: formatarMoeda(dados.despesas * 0.35),
    pjClientes: formatarMoeda(dados.receitas * 0.42),
    pjFornecedores: formatarMoeda(dados.despesas * 0.28),
    pjExposicao: dados.saldo >= 0 ? 'Baixa' : 'Alta',
    pjCredito: dados.coberturas,
    pjLiquidez: dados.cobertura > 1.5 ? 'Estável' : dados.cobertura > 0.8 ? 'Atenta' : 'Crítica',
    pjAtencao: dados.saldo >= 0 ? 'Contínua' : 'Urgente',
    pjLiquidezIndicador: `${dados.cobertura > 0 ? dados.cobertura.toFixed(1) : '0,0'}x`,
    pjMargem: `${dados.margem >= 0 ? dados.margem.toFixed(1) : '0,0'}%`,
    pjEndividamento: `${dados.despesas > 0 ? Math.min(100, ((dados.despesas / Math.max(dados.receitas, 1)) * 100)).toFixed(0) : '0'}%`,
    pjCiclo: `${Math.max(10, Math.min(90, Math.round(dados.giro))) }d`
  };

  Object.entries(campos).forEach(([id, valor]) => {
    const el = document.getElementById(id);
    if (el) {
      el.textContent = valor;
    }
  });
}

document.addEventListener('DOMContentLoaded', atualizarDadosPJ);
window.addEventListener('storage', atualizarDadosPJ);
