// ============================================
// VARIÁVEIS GLOBAIS
// ============================================
const API_BASE = "http://localhost:10000";
let currentProfile = null;
let editMode = false;
let financialEditMode = false;
let constitutionalMode = false;

// ============================================
// INICIALIZAÇÃO
// ============================================
document.addEventListener("DOMContentLoaded", function() {
    const token = localStorage.getItem("token");
    if (!token) {
        alert("Token não encontrado! Redirecionando para login...");
        window.location.href = "index.html";
        return;
    }

    loadCompleteProfile();
});

// ============================================
// CARREGAR PERFIL COMPLETO
// ============================================
function loadCompleteProfile() {
    const token = localStorage.getItem("token");

    fetch(`${API_BASE}/perfil/completo`, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        }
    })
    .then(res => {
        if (!res.ok) throw new Error("Erro ao carregar perfil");
        return res.json();
    })
    .then(data => {
        currentProfile = data;
        displayProfile(data);
        showAlert("Perfil carregado com sucesso!", "success");
    })
    .catch(err => {
        console.error(err);
        showAlert("Erro ao carregar perfil: " + err.message, "error");
    });
}

// ============================================
// EXIBIR DADOS DO PERFIL
// ============================================
function displayProfile(profile) {
    // Dados Pessoais
    const fullName = (profile.firstName || "-") + " " + (profile.lastName || "-");
    document.getElementById("display-fullname").textContent = fullName.trim() || "-";
    document.getElementById("display-age").textContent = profile.age ? profile.age + " anos" : "-";
    document.getElementById("display-profession").textContent = profile.profissao || "-";
    document.getElementById("display-city").textContent = profile.cidade || "-";
    document.getElementById("display-state").textContent = profile.estado || "-";
    document.getElementById("display-email").textContent = profile.email || "-";

    // Dados Financeiros
    document.getElementById("display-patrimonio").textContent =
        profile.patrimonio ? `R$ ${formatMoney(profile.patrimonio)}` : "-";
    document.getElementById("display-renda").textContent =
        profile.rendaMensal ? `R$ ${formatMoney(profile.rendaMensal)}` : "-";
    document.getElementById("display-meta").textContent =
        profile.metaAnual ? `R$ ${formatMoney(profile.metaAnual)}` : "-";

    // Preencher campos de edição
    document.getElementById("input-firstName").value = profile.firstName || "";
    document.getElementById("input-lastName").value = profile.lastName || "";
    document.getElementById("input-age").value = profile.age || "";
    document.getElementById("input-profession").value = profile.profissao || "";
    document.getElementById("input-city").value = profile.cidade || "";
    document.getElementById("input-state").value = profile.estado || "";
    document.getElementById("input-patrimonio").value = profile.patrimonio || "";
    document.getElementById("input-renda").value = profile.rendaMensal || "";
    document.getElementById("input-meta").value = profile.metaAnual || "";

    // Dados de Constituição (se existente)
    if (profile.investmentPercent) {
        displayConstmoney(profile);
    } else {
        document.getElementById("show-button").style.display = "block";
        document.getElementById("constitution-display").classList.add("hide");
    }
}

// ============================================
// EXIBIR CONSTITUIÇÃO FINANCEIRA
// ============================================
function displayConstmoney(profile) {
    document.getElementById("const-investment").textContent = (profile.investmentPercent || 0) + "%";
    document.getElementById("const-reserve").textContent = (profile.reservePercent || 0) + "%";
    document.getElementById("const-tax").textContent = (profile.taxPercent || 0) + "%";
    document.getElementById("profile-status").textContent = profile.profileStatus || "N/A";

    document.getElementById("const-trigger1").textContent =
        profile.trigger1 ? `R$ ${formatMoney(profile.trigger1)}` : "-";
    document.getElementById("const-trigger2").textContent =
        profile.trigger2 ? `R$ ${formatMoney(profile.trigger2)}` : "-";

    // Atualizar progress bars
    document.getElementById("inv-progress").style.width = (profile.investmentPercent || 0) + "%";
    document.getElementById("res-progress").style.width = (profile.reservePercent || 0) + "%";
    document.getElementById("tax-progress").style.width = (profile.taxPercent || 0) + "%";

    // Preencher campos de edição
    document.getElementById("input-inv-percent").value = profile.investmentPercent || 30;
    document.getElementById("input-res-percent").value = profile.reservePercent || 10;
    document.getElementById("input-tax-percent").value = profile.taxPercent || 5;
    document.getElementById("input-trigger1").value = profile.trigger1 || "";
    document.getElementById("input-trigger2").value = profile.trigger2 || "";

    document.getElementById("show-button").style.display = "none";
    document.getElementById("constitution-display").classList.remove("hide");
}

// ============================================
// TOGGLE EDIT MODES
// ============================================
function toggleEditMode() {
    editMode = !editMode;
    if (editMode) {
        document.getElementById("view-mode").classList.add("hide");
        document.getElementById("edit-mode").classList.remove("hide");
    } else {
        document.getElementById("view-mode").classList.remove("hide");
        document.getElementById("edit-mode").classList.add("hide");
    }
}

function toggleFinancialMode() {
    financialEditMode = !financialEditMode;
    if (financialEditMode) {
        document.getElementById("financial-view").classList.add("hide");
        document.getElementById("financial-edit").classList.remove("hide");
    } else {
        document.getElementById("financial-view").classList.remove("hide");
        document.getElementById("financial-edit").classList.add("hide");
    }
}

function toggleConstitutionMode() {
    constitutionalMode = !constitutionalMode;
    if (constitutionalMode) {
        document.getElementById("constitution-display").classList.add("hide");
        document.getElementById("constitution-edit").classList.remove("hide");
    } else {
        document.getElementById("constitution-display").classList.remove("hide");
        document.getElementById("constitution-edit").classList.add("hide");
    }
}

// ============================================
// SALVAR INFORMAÇÕES PESSOAIS
// ============================================
function savePersonalInfo() {
    const token = localStorage.getItem("token");

    const data = {
        firstName: document.getElementById("input-firstName").value || null,
        lastName: document.getElementById("input-lastName").value || null,
        age: document.getElementById("input-age").value ? parseInt(document.getElementById("input-age").value) : null,
        profissao: document.getElementById("input-profession").value || null,
        cidade: document.getElementById("input-city").value || null,
        estado: document.getElementById("input-state").value || null
    };

    fetch(`${API_BASE}/perfil/atualizar-dados`, {
        method: "PUT",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    })
    .then(res => {
        if (!res.ok) throw new Error("Erro ao salvar");
        return res.json();
    })
    .then(() => {
        showAlert("Informações pessoais atualizadas com sucesso!", "success");
        toggleEditMode();
        loadCompleteProfile();
    })
    .catch(err => {
        showAlert("Erro ao salvar: " + err.message, "error");
    });
}

// ============================================
// SALVAR INFORMAÇÕES FINANCEIRAS
// ============================================
function saveFinancialInfo() {
    const token = localStorage.getItem("token");

    const data = {
        patrimonio: document.getElementById("input-patrimonio").value ?
            parseFloat(document.getElementById("input-patrimonio").value) : null,
        rendaMensal: document.getElementById("input-renda").value ?
            parseFloat(document.getElementById("input-renda").value) : null,
        metaAnual: document.getElementById("input-meta").value ?
            parseFloat(document.getElementById("input-meta").value) : null
    };

    fetch(`${API_BASE}/perfil/atualizar-dados`, {
        method: "PUT",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    })
    .then(res => {
        if (!res.ok) throw new Error("Erro ao salvar");
        return res.json();
    })
    .then(() => {
        showAlert("Informações financeiras atualizadas com sucesso!", "success");
        toggleFinancialMode();
        loadCompleteProfile();
    })
    .catch(err => {
        showAlert("Erro ao salvar: " + err.message, "error");
    });
}

// ============================================
// SETUP CONSTITUIÇÃO
// ============================================
function setupConstituition() {
    const token = localStorage.getItem("token");

    const data = {
        investmentPercent: (() => {
            const inputVal = document.getElementById("input-inv-percent").value.trim();
            if (inputVal === "") return 30;
            const num = Number(inputVal);
            return isNaN(num) ? 30 : num;
        })(),
        reservePercent: (() => {
            const inputVal = document.getElementById("input-res-percent").value.trim();
            if (inputVal === "") return 10;
            const num = Number(inputVal);
            return isNaN(num) ? 10 : num;
        })(),
        taxPercent: (() => {
            const inputVal = document.getElementById("input-tax-percent").value.trim();
            if (inputVal === "") return 5;
            const num = Number(inputVal);
            return isNaN(num) ? 5 : num;
        })(),
        trigger1: document.getElementById("input-trigger1").value ?
            parseFloat(document.getElementById("input-trigger1").value) : 5000,
        trigger2: document.getElementById("input-trigger2").value ?
            parseFloat(document.getElementById("input-trigger2").value) : 10000,
        emergencyMode: false
    };

    fetch(`${API_BASE}/perfil/setup`, {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    })
    .then(res => {
        if (!res.ok) throw new Error("Erro ao configurar");
        return res.json();
    })
    .then(() => {
        showAlert("Constituição configurada com sucesso!", "success");
        toggleConstitutionMode();
        loadCompleteProfile();
    })
    .catch(err => {
        showAlert("Erro ao configurar: " + err.message, "error");
    });
}

// ============================================
// SIMULAR RENDA
// ============================================
function simulate() {
    const token = localStorage.getItem("token");
    const income = document.getElementById("simulate-income").value;

    if (!income || parseFloat(income) <= 0) {
        showAlert("Por favor, insira uma renda válida", "error");
        return;
    }

    fetch(`${API_BASE}/perfil/simular?receita=${income}`, {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        }
    })
    .then(res => {
        if (!res.ok) throw new Error("Erro na simulação");
        return res.json();
    })
    .then(data => {
        displaySimulationResult(data);
        showAlert("Simulação realizada com sucesso!", "success");
    })
    .catch(err => {
        showAlert("Erro na simulação: " + err.message, "error");
    });
}

function displaySimulationResult(data) {
    document.getElementById("sim-gross").textContent = `R$ ${formatMoney(data.grossIncome)}`;
    document.getElementById("sim-investment").textContent = `R$ ${formatMoney(data.investment)}`;
    document.getElementById("sim-reserve").textContent = `R$ ${formatMoney(data.reserve)}`;
    document.getElementById("sim-tax").textContent = `R$ ${formatMoney(data.tax)}`;

    document.getElementById("simulation-result").classList.remove("hide");
}

// ============================================
// LOGOUT
// ============================================
function logout() {
    const token = localStorage.getItem("token");

    fetch(`${API_BASE}/auth/logout`, {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    })
    .then(() => {
        localStorage.removeItem("token");
        window.location.href = "index.html";
    })
    .catch(err => {
        console.error(err);
        localStorage.removeItem("token");
        window.location.href = "index.html";
    });
}

// ============================================
// UTILITÁRIOS
// ============================================
function formatMoney(value) {
    if (!value) return "0,00";
    return parseFloat(value).toLocaleString("pt-BR", {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    });
}

function showAlert(message, type = "info") {
    const container = document.getElementById("alert-container");
    const alert = document.createElement("div");
    alert.className = `alert alert-${type}`;
    alert.textContent = message;

    container.appendChild(alert);

    setTimeout(() => {
        alert.remove();
    }, 5000);
    <script src="./js/sessao.js"></script>
}
