// =========================================================
// CONSTITUCIONAL.JS
// Regras oficiais da Constituição Financeira Pessoal
// Com armazenamento separado por usuário logado
// =========================================================

const CONSTITUICAO_CONFIG = {
  limitesGastos: {
    normal: 2250,
    gatilho2: 3500
  },

  gatilhos: {
    normal: {
      codigo: "normal",
      nome: "😌 Estado Normal",
      condicao: "Gastos < R$ 2.250",
      investimento: 30,
      reservaFlex: 10,
      vida: 60,
      objetivo: "Organização financeira padrão"
    },

    gatilho1: {
      codigo: "gatilho1",
      nome: "⚠️ Gatilho 1",
      condicao: "Gastos ≥ R$ 2.250",
      investimento: 40,
      reservaFlex: 10,
      vida: 50,
      objetivo: "Aceleração patrimonial preventiva"
    },

    gatilho2: {
      codigo: "gatilho2",
      nome: "🚨 Gatilho 2",
      condicao: "Gastos ≥ R$ 3.500",
      investimento: 50,
      reservaFlex: 10,
      vida: 40,
      objetivo: "Blindagem máxima e correção estrutural"
    }
  },

  emergencia: {
    tfpMinimo: 40,
    deficitMaximo: -10
  }
};

// =========================================================
// USUÁRIO LOGADO
// =========================================================

function obterUsuarioLogadoConstitucional() {
  const usuario = localStorage.getItem("usuarioLogado");

  if (!usuario) {
    return null;
  }

  try {
    return JSON.parse(usuario);
  } catch (error) {
    return null;
  }
}

/*
 * Cria uma chave separada para cada usuário.
 *
 * Exemplo:
 * despesas -> despesas_diego@email.com
 * receitas -> receitas_diego@email.com
 */
function chavePorUsuario(chave) {
  const usuario = obterUsuarioLogadoConstitucional();

  if (!usuario || !usuario.email) {
    return chave;
  }

  return `${chave}_${usuario.email}`;
}

// =========================================================
// FUNÇÕES GERAIS
// =========================================================

function moeda(valor) {
  return Number(valor || 0).toLocaleString("pt-BR", {
    style: "currency",
    currency: "BRL"
  });
}

function percentual(valor) {
  return Number(valor || 0).toFixed(1) + "%";
}

function hojeBR() {
  return new Date().toLocaleDateString("pt-BR");
}

function salvarStorage(chave, valor) {
  localStorage.setItem(chavePorUsuario(chave), JSON.stringify(valor));
}

function lerStorage(chave, padrao) {
  const valor = localStorage.getItem(chavePorUsuario(chave));

  if (!valor) {
    return padrao;
  }

  try {
    return JSON.parse(valor);
  } catch (error) {
    return padrao;
  }
}

function removerStorage(chave) {
  localStorage.removeItem(chavePorUsuario(chave));
}

function parseValor(valor) {
  return Number(valor || 0);
}

// =========================================================
// GATILHOS
// =========================================================

function calcularGatilhoPorGastos(gastosMensais) {
  const gastos = parseValor(gastosMensais);

  if (gastos >= CONSTITUICAO_CONFIG.limitesGastos.gatilho2) {
    return CONSTITUICAO_CONFIG.gatilhos.gatilho2;
  }

  if (gastos >= CONSTITUICAO_CONFIG.limitesGastos.normal) {
    return CONSTITUICAO_CONFIG.gatilhos.gatilho1;
  }

  return CONSTITUICAO_CONFIG.gatilhos.normal;
}

function obterGatilhoAtivo() {
  return lerStorage("gatilhoAtivo", CONSTITUICAO_CONFIG.gatilhos.normal);
}

function salvarGatilhoAtivo(gatilho) {
  salvarStorage("gatilhoAtivo", gatilho);
}

// =========================================================
// RECEITAS
// =========================================================

function obterReceitas() {
  return lerStorage("receitas", []);
}

function salvarReceitas(receitas) {
  salvarStorage("receitas", receitas);
}

function calcularDivisaoReceita(valorReceita) {
  const valor = parseValor(valorReceita);
  const gatilho = obterGatilhoAtivo();
  const emergencia = obterEstadoEmergencia();

  const investimento = valor * (gatilho.investimento / 100);
  const reservaFlex = valor * (gatilho.reservaFlex / 100);
  const vida = valor * (gatilho.vida / 100);

  return {
    valorReceita: valor,
    gatilho,
    emergenciaAtiva: emergencia.ativo,
    investimento,
    reservaFlex,
    vida,
    destinoInvestimento: emergencia.ativo ? "Liquidez / Reserva Flex" : "Investimentos"
  };
}

function registrarReceitaLocal(dados) {
  const receitas = obterReceitas();
  const divisao = calcularDivisaoReceita(dados.valor);

  const novaReceita = {
    id: Date.now(),
    data: dados.data || hojeBR(),
    descricao: dados.descricao,
    valor: parseValor(dados.valor),
    gatilho: divisao.gatilho.nome,
    investimento: divisao.investimento,
    reservaFlex: divisao.reservaFlex,
    vida: divisao.vida,
    destinoInvestimento: divisao.destinoInvestimento,
    emergenciaAtiva: divisao.emergenciaAtiva
  };

  receitas.push(novaReceita);
  salvarReceitas(receitas);

  return novaReceita;
}

function calcularTotalReceitas() {
  return obterReceitas().reduce(function(total, receita) {
    return total + parseValor(receita.valor);
  }, 0);
}

// =========================================================
// DESPESAS
// =========================================================

function obterDespesas() {
  return lerStorage("despesas", []);
}

function salvarDespesas(despesas) {
  salvarStorage("despesas", despesas);
}

function registrarDespesaLocal(despesa) {
  const despesas = obterDespesas();

  const novaDespesa = {
    id: Date.now(),
    data: despesa.data || hojeBR(),
    descricao: despesa.descricao,
    categoria: despesa.categoria || "Sem categoria",
    valor: parseValor(despesa.valor),
    essencial: despesa.essencial || "sim"
  };

  despesas.push(novaDespesa);
  salvarDespesas(despesas);

  atualizarGatilhoPelasDespesas();

  return novaDespesa;
}

function calcularTotalDespesas() {
  return obterDespesas().reduce(function(total, despesa) {
    return total + parseValor(despesa.valor);
  }, 0);
}

function atualizarGatilhoPelasDespesas() {
  const totalDespesas = calcularTotalDespesas();
  const gatilho = calcularGatilhoPorGastos(totalDespesas);

  salvarGatilhoAtivo(gatilho);

  return gatilho;
}

// =========================================================
// FLUXO DE CAIXA
// =========================================================

function calcularFluxoCaixaTotal() {
  const receitas = calcularTotalReceitas();
  const despesas = calcularTotalDespesas();

  return {
    receitas,
    despesas,
    saldo: receitas - despesas
  };
}

function extrairMesAno(data) {
  if (!data) {
    const hoje = new Date();
    return `${String(hoje.getMonth() + 1).padStart(2, "0")}/${hoje.getFullYear()}`;
  }

  if (data.includes("-")) {
    const partes = data.split("-");
    return `${partes[1]}/${partes[0]}`;
  }

  if (data.includes("/")) {
    const partes = data.split("/");
    return `${partes[1]}/${partes[2]}`;
  }

  const hoje = new Date();
  return `${String(hoje.getMonth() + 1).padStart(2, "0")}/${hoje.getFullYear()}`;
}

function calcularFluxoMensal() {
  const receitas = obterReceitas();
  const despesas = obterDespesas();

  const mapa = {};

  receitas.forEach(function(receita) {
    const mesAno = extrairMesAno(receita.data);

    if (!mapa[mesAno]) {
      mapa[mesAno] = {
        mesAno,
        receitas: 0,
        despesas: 0,
        saldo: 0
      };
    }

    mapa[mesAno].receitas += parseValor(receita.valor);
  });

  despesas.forEach(function(despesa) {
    const mesAno = extrairMesAno(despesa.data);

    if (!mapa[mesAno]) {
      mapa[mesAno] = {
        mesAno,
        receitas: 0,
        despesas: 0,
        saldo: 0
      };
    }

    mapa[mesAno].despesas += parseValor(despesa.valor);
  });

  const lista = Object.values(mapa).map(function(item) {
    item.saldo = item.receitas - item.despesas;
    return item;
  });

  lista.sort(function(a, b) {
    const [mesA, anoA] = a.mesAno.split("/").map(Number);
    const [mesB, anoB] = b.mesAno.split("/").map(Number);

    if (anoA !== anoB) {
      return anoA - anoB;
    }

    return mesA - mesB;
  });

  return lista;
}

// =========================================================
// CARTÕES
// =========================================================

function obterCartoesCredito() {
  return lerStorage("cartoesCredito", []);
}

function salvarCartoesCredito(cartoes) {
  salvarStorage("cartoesCredito", cartoes);
}

function obterComprasCartao() {
  return lerStorage("comprasCartao", []);
}

function salvarComprasCartao(compras) {
  salvarStorage("comprasCartao", compras);
}

// =========================================================
// ATIVOS
// =========================================================

function obterAtivosCarteira() {
  return lerStorage("ativos", []);
}

function salvarAtivosCarteira(ativos) {
  salvarStorage("ativos", ativos);
}

// =========================================================
// TERMÔMETRO FINANCEIRO PESSOAL
// =========================================================

function calcularScoreComportamental(sobraDinheiro, usaCredito, rendaEstavel) {
  const perguntas = [
    {
      pergunta: "💸 Está sobrando dinheiro no mês?",
      resposta: sobraDinheiro,
      score: sobraDinheiro === "sim" ? 100 : 0,
      interpretacao: sobraDinheiro === "sim"
        ? "Positivo: há sobra de dinheiro no mês."
        : "Atenção: não está sobrando dinheiro no mês."
    },
    {
      pergunta: "💳 Está usando crédito para fechar o mês?",
      resposta: usaCredito,
      score: usaCredito === "nao" ? 100 : 0,
      interpretacao: usaCredito === "nao"
        ? "Positivo: não depende de crédito para fechar o mês."
        : "Alerta: está usando crédito para fechar o mês."
    },
    {
      pergunta: "📉 A renda está estável ou previsível?",
      resposta: rendaEstavel,
      score: rendaEstavel === "sim" ? 100 : 0,
      interpretacao: rendaEstavel === "sim"
        ? "Positivo: renda estável ou previsível."
        : "Atenção: renda instável ou imprevisível."
    }
  ];

  const soma = perguntas.reduce(function(total, item) {
    return total + item.score;
  }, 0);

  return {
    score: soma / perguntas.length,
    perguntas
  };
}

function calcularScoreNumerico(receitaTotal, despesasTotais) {
  const receita = parseValor(receitaTotal);
  const despesas = parseValor(despesasTotais);

  if (receita <= 0) {
    return {
      score: 0,
      percentualFluxo: -100,
      fluxo: receita - despesas,
      classificacao: "🔴 Sem receita informada"
    };
  }

  const fluxo = receita - despesas;
  const percentualFluxo = (fluxo / receita) * 100;

  if (percentualFluxo >= 20) {
    return {
      score: 100,
      percentualFluxo,
      fluxo,
      classificacao: "🟢 Superávit ≥ 20%"
    };
  }

  if (percentualFluxo >= 0) {
    return {
      score: 75,
      percentualFluxo,
      fluxo,
      classificacao: "🟡 Superávit entre 0% e 19%"
    };
  }

  if (percentualFluxo >= -10) {
    return {
      score: 40,
      percentualFluxo,
      fluxo,
      classificacao: "🟠 Déficit até -10%"
    };
  }

  return {
    score: 0,
    percentualFluxo,
    fluxo,
    classificacao: "🔴 Déficit maior que -10%"
  };
}

function classificarTFP(tfp) {
  if (tfp >= 80) {
    return {
      nome: "🟢 Estável",
      classe: "success"
    };
  }

  if (tfp >= 60) {
    return {
      nome: "🟡 Atenção",
      classe: "warning"
    };
  }

  if (tfp >= 40) {
    return {
      nome: "🟠 Alerta",
      classe: "warning"
    };
  }

  return {
    nome: "🔴 Emergência",
    classe: "danger"
  };
}

function calcularTFP(dados) {
  const comportamental = calcularScoreComportamental(
    dados.sobraDinheiro,
    dados.usaCredito,
    dados.rendaEstavel
  );

  const numerico = calcularScoreNumerico(
    dados.receitaTotal,
    dados.despesasTotais
  );

  const tfp = (comportamental.score + numerico.score) / 2;
  const classificacao = classificarTFP(tfp);

  const emergenciaAtiva =
    tfp < CONSTITUICAO_CONFIG.emergencia.tfpMinimo ||
    numerico.percentualFluxo < CONSTITUICAO_CONFIG.emergencia.deficitMaximo;

  const resultado = {
    data: hojeBR(),
    receitaTotal: parseValor(dados.receitaTotal),
    despesasTotais: parseValor(dados.despesasTotais),
    scoreComportamental: comportamental.score,
    perguntasComportamentais: comportamental.perguntas,
    scoreNumerico: numerico.score,
    percentualFluxo: numerico.percentualFluxo,
    fluxo: numerico.fluxo,
    classificacaoNumerica: numerico.classificacao,
    tfp,
    classificacao: classificacao.nome,
    classe: classificacao.classe,
    emergenciaAtiva
  };

  salvarStorage("ultimoTFP", resultado);

  if (emergenciaAtiva) {
    ativarEmergenciaPorTFP(resultado);
  } else {
    encerrarEmergenciaSePossivel(resultado);
  }

  return resultado;
}

// =========================================================
// ESTADO DE EMERGÊNCIA
// =========================================================

function obterEstadoEmergencia() {
  return lerStorage("estadoEmergencia", {
    ativo: false,
    origem: "",
    motivo: "",
    dataAtivacao: "",
    tfp: null,
    deficit: null
  });
}

function ativarEmergenciaPorTFP(resultadoTFP) {
  const estado = {
    ativo: true,
    origem: "TFP",
    motivo: "Ativação automática por TFP abaixo de 40% ou déficit superior a -10%",
    dataAtivacao: hojeBR(),
    tfp: resultadoTFP.tfp,
    deficit: resultadoTFP.percentualFluxo,
    efeitos: [
      "Suspensão de gastos não essenciais",
      "Proibição de novos parcelamentos",
      "Prioridade máxima para liquidez",
      "Reforço da Reserva Flex",
      "Aportes direcionados para liquidez"
    ]
  };

  salvarStorage("estadoEmergencia", estado);

  registrarOcorrenciaAutomatica({
    tipo: "🚨 Estado de Emergência",
    descricao: "Estado de Emergência ativado automaticamente pelo Termômetro Financeiro Pessoal.",
    valor: 0,
    plano: "Priorizar liquidez, suspender gastos não essenciais e reforçar Reserva Flex."
  });

  return estado;
}

function encerrarEmergenciaSePossivel(resultadoTFP) {
  const emergencia = obterEstadoEmergencia();

  if (!emergencia.ativo) {
    return emergencia;
  }

  if (resultadoTFP.tfp >= 40) {
    const estadoEncerrado = {
      ...emergencia,
      ativo: false,
      dataEncerramento: hojeBR(),
      motivoEncerramento: "TFP igual ou superior a 40%"
    };

    salvarStorage("estadoEmergencia", estadoEncerrado);

    return estadoEncerrado;
  }

  return emergencia;
}

// =========================================================
// OCORRÊNCIAS
// =========================================================

function registrarOcorrenciaAutomatica(dados) {
  const ocorrencias = lerStorage("ocorrencias", []);

  const nova = {
    id: Date.now(),
    data: hojeBR(),
    tipo: dados.tipo,
    descricao: dados.descricao,
    valor: parseValor(dados.valor),
    plano: dados.plano || "",
    origem: "Automática"
  };

  ocorrencias.push(nova);
  salvarStorage("ocorrencias", ocorrencias);

  return nova;
}