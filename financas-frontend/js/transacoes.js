// =========================================================
// TRANSACOES.JS
// Controla a página transacoes.html
// Salva os dados no navegador usando localStorage.
// =========================================================

const formTransacao = document.getElementById("formTransacao");
const listaTransacoes = document.getElementById("listaTransacoes");

const totalReceitas = document.getElementById("totalReceitas");
const totalDespesas = document.getElementById("totalDespesas");
const totalInvestimentos = document.getElementById("totalInvestimentos");
const saldoFinal = document.getElementById("saldoFinal");

// Carrega transações salvas
let transacoes = JSON.parse(localStorage.getItem("transacoes")) || [];

// Coloca a data atual no campo de data
document.getElementById("data").valueAsDate = new Date();

function formatarMoeda(valor) {
  return Number(valor).toLocaleString("pt-BR", {
    style: "currency",
    currency: "BRL"
  });
}

function salvarTransacoes() {
  localStorage.setItem("transacoes", JSON.stringify(transacoes));
}

function formatarTipo(tipo) {
  const tipos = {
    RECEITA: `<span class="badge badge-success">Receita</span>`,
    DESPESA: `<span class="badge badge-danger">Despesa</span>`,
    INVESTIMENTO: `<span class="badge badge-info">Investimento</span>`,
    RESERVA_FLEX: `<span class="badge badge-gold">Reserva Flex</span>`,
    OCORRENCIA: `<span class="badge badge-warning">Ocorrência</span>`
  };

  return tipos[tipo] || tipo;
}

function calcularResumo() {
  let receitas = 0;
  let despesas = 0;
  let investimentos = 0;

  transacoes.forEach(function(t) {
    const valor = Number(t.valor);

    if (t.tipo === "RECEITA") {
      receitas += valor;
    }

    if (t.tipo === "DESPESA" || t.tipo === "OCORRENCIA") {
      despesas += valor;
    }

    if (t.tipo === "INVESTIMENTO" || t.tipo === "RESERVA_FLEX") {
      investimentos += valor;
    }
  });

  const saldo = receitas - despesas - investimentos;

  totalReceitas.textContent = formatarMoeda(receitas);
  totalDespesas.textContent = formatarMoeda(despesas);
  totalInvestimentos.textContent = formatarMoeda(investimentos);
  saldoFinal.textContent = formatarMoeda(saldo);
}

function renderizarTransacoes() {
  listaTransacoes.innerHTML = "";

  transacoes.forEach(function(transacao, index) {
    const tr = document.createElement("tr");

    tr.innerHTML = `
      <td>${transacao.data}</td>
      <td>${formatarTipo(transacao.tipo)}</td>
      <td>${transacao.categoria}</td>
      <td>${transacao.descricao || "-"}</td>
      <td>${formatarMoeda(transacao.valor)}</td>
      <td>
        <button class="btn btn-danger" onclick="removerTransacao(${index})">
          Excluir
        </button>
      </td>
    `;

    listaTransacoes.appendChild(tr);
  });

  calcularResumo();
}

function removerTransacao(index) {
  if (!confirm("Deseja excluir esta transação?")) {
    return;
  }

  transacoes.splice(index, 1);
  salvarTransacoes();
  renderizarTransacoes();
}

formTransacao.addEventListener("submit", function(event) {
  event.preventDefault();

  const novaTransacao = {
    tipo: document.getElementById("tipo").value,
    categoria: document.getElementById("categoria").value,
    valor: Number(document.getElementById("valor").value),
    data: document.getElementById("data").value,
    descricao: document.getElementById("descricao").value
  };

  transacoes.push(novaTransacao);
  salvarTransacoes();
  renderizarTransacoes();

  formTransacao.reset();
  document.getElementById("data").valueAsDate = new Date();
});

renderizarTransacoes();