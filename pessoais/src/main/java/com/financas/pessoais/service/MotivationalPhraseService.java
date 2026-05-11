package com.financas.pessoais.service;

import com.financas.pessoais.dto.MotivationalPhraseResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class MotivationalPhraseService {

    private final List<MotivationalPhraseResponse> phrases = new ArrayList<>();

    public MotivationalPhraseService() {
        // Inicializar banco de frases inspiradoras
        phrases.add(new MotivationalPhraseResponse(
                "O dinheiro é apenas uma ferramenta. Ele te levará para onde você quer ir, mas não substitui você como motorista.",
                "Ayn Rand",
                "INVESTIMENTO"
        ));

        phrases.add(new MotivationalPhraseResponse(
                "Não é quanto dinheiro você faz, mas quanto você mantém, quanto você gasta e por quanto tempo o dinheiro trabalha para você.",
                "Robert Kiyosaki",
                "EDUCACAO_FINANCEIRA"
        ));

        phrases.add(new MotivationalPhraseResponse(
                "A melhor maneira de se prever o futuro é inventá-lo.",
                "Alan Kay",
                "PLANEJAMENTO"
        ));

        phrases.add(new MotivationalPhraseResponse(
                "Riqueza consiste não em ter grandes possessões, mas em ter poucas vontades.",
                "Epicteto",
                "FRUGALIDADE"
        ));

        phrases.add(new MotivationalPhraseResponse(
                "O tempo é dinheiro. Cada hora que você não investe é uma hora perdida de crescimento.",
                "Benjamin Franklin",
                "INVESTIMENTO"
        ));

        phrases.add(new MotivationalPhraseResponse(
                "Não há atalho para qualquer lugar que valha a pena ir.",
                "Beverly Sills",
                "PACIENCIA"
        ));

        phrases.add(new MotivationalPhraseResponse(
                "A liberdade financeira é a capacidade de viver segundo seus próprios termos.",
                "Naval Ravikant",
                "LIBERDADE_FINANCEIRA"
        ));

        phrases.add(new MotivationalPhraseResponse(
                "Investir em conhecimento é o melhor investimento que você pode fazer.",
                "Warren Buffett",
                "EDUCACAO_FINANCEIRA"
        ));

        phrases.add(new MotivationalPhraseResponse(
                "A paciência é a maior virtude de um investidor. Rome não foi construída em um dia.",
                "JP Morgan",
                "PACIENCIA"
        ));

        phrases.add(new MotivationalPhraseResponse(
                "Se você vivesse como os pobres vivem, eventualmente ficará rico.",
                "Nassim Taleb",
                "FRUGALIDADE"
        ));

        phrases.add(new MotivationalPhraseResponse(
                "Não gaste seu dinheiro em coisas que você não precisa, para impressionar pessoas que não se importam.",
                "David Ramsey",
                "FRUGALIDADE"
        ));

        phrases.add(new MotivationalPhraseResponse(
                "A chave para ficar rico é encontrar maneiras de fazer o dinheiro trabalhar para você, não o contrário.",
                "Robert Kiyosaki",
                "INVESTIMENTO"
        ));

        phrases.add(new MotivationalPhraseResponse(
                "Comece cedo, invista regularmente e deixe o tempo trabalhar por você.",
                "Albert Einstein",
                "INVESTIMENTO"
        ));

        phrases.add(new MotivationalPhraseResponse(
                "A única diferença entre uma pessoa rica e uma pobre é como eles gastam seu tempo.",
                "Robert Kiyosaki",
                "TEMPO"
        ));

        phrases.add(new MotivationalPhraseResponse(
                "Seu maior ativo são suas habilidades. Invista em você mesmo.",
                "Ray Dalio",
                "AUTODESENVOLVIMENTO"
        ));

        phrases.add(new MotivationalPhraseResponse(
                "A diversificação é proteção contra a ignorância.",
                "Warren Buffett",
                "INVESTIMENTO"
        ));

        phrases.add(new MotivationalPhraseResponse(
                "Risco vem do não saber o que você está fazendo.",
                "Warren Buffett",
                "CONHECIMENTO"
        ));

        phrases.add(new MotivationalPhraseResponse(
                "A melhor time para plantar uma árvore foi há 20 anos. A segunda melhor time é agora.",
                "Provérbio Chinês",
                "PLANEJAMENTO"
        ));

        phrases.add(new MotivationalPhraseResponse(
                "Ganhar dinheiro é a prioridade da primeira classe, poupar dinheiro é a prioridade da segunda classe.",
                "Warren Buffett",
                "POUPANCA"
        ));

        phrases.add(new MotivationalPhraseResponse(
                "Se você não encontrar uma forma de ganhar dinheiro enquanto dorme, você trabalhará o resto da vida.",
                "Warren Buffett",
                "INVESTIMENTO"
        ));
    }

    /**
     * Obtém uma frase inspiradora aleatória
     */
    public MotivationalPhraseResponse getRandomPhrase() {
        Random random = new Random();
        return phrases.get(random.nextInt(phrases.size()));
    }

    /**
     * Obtém uma frase inspiradora por categoria
     */
    public MotivationalPhraseResponse getPhraseByCategory(String category) {
        return phrases.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .findFirst()
                .orElseGet(this::getRandomPhrase);
    }

    /**
     * Obtém todas as categorias disponíveis
     */
    public List<String> getCategories() {
        return phrases.stream()
                .map(MotivationalPhraseResponse::getCategory)
                .distinct()
                .toList();
    }
}

