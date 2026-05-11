package com.financas.pessoais.entity;

public enum CategoriaTransacao {
    // Receitas
    SALARIO("Salário"),
    FREELANCER("Freelancer"),
    DIVIDENDOS("Dividendos"),
    ALUGUEL_RECEBIDO("Aluguel Recebido"),
    VENDAS("Vendas"),
    OUTRAS_RECEITAS("Outras Receitas"),

    // Despesas Essenciais
    ALIMENTACAO("Alimentação"),
    MORADIA("Moradia"),
    TRANSPORTE("Transporte"),
    SAUDE("Saúde"),
    EDUCACAO("Educação"),
    UTILIDADES("Utilidades"),

    // Despesas Não Essenciais
    ENTRETENIMENTO("Entretenimento"),
    LAZER("Lazer"),
    ROUPAS("Roupas"),
    COSMETICOS("Cosméticos"),
    RESTAURANTES("Restaurantes"),
    VIAGENS("Viagens"),
    PRESENTES("Presentes"),
    OUTRAS_DESPESAS("Outras Despesas");

    private final String descricao;

    CategoriaTransacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static CategoriaTransacao[] getReceitas() {
        return new CategoriaTransacao[]{
            SALARIO, FREELANCER, DIVIDENDOS, ALUGUEL_RECEBIDO, VENDAS, OUTRAS_RECEITAS
        };
    }

    public static CategoriaTransacao[] getDespesas() {
        return new CategoriaTransacao[]{
            ALIMENTACAO, MORADIA, TRANSPORTE, SAUDE, EDUCACAO, UTILIDADES,
            ENTRETENIMENTO, LAZER, ROUPAS, COSMETICOS, RESTAURANTES, VIAGENS,
            PRESENTES, OUTRAS_DESPESAS
        };
    }
}
