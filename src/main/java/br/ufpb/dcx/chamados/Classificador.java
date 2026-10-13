package br.ufpb.dcx.chamados;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** Pede ao LLM a categoria e a prioridade de um chamado, e confere a resposta. */
public class Classificador {

    static final List<String> CATEGORIAS = List.of("rede", "acesso", "equipamento", "software", "outro");
    static final List<String> PRIORIDADES = List.of("alta", "media", "baixa");
    private static final ObjectMapper JSON = new ObjectMapper();

    public record Chamado(String texto, String categoria, String prioridade, String motivo,
                          String respostaBruta, int tokens, long ms) {}

    public static Chamado classificar(String texto) {
        try {
            // O prompt é lido do arquivo a CADA chamado: edite o prompt.txt e cadastre de novo, sem reiniciar.
            String prompt = Files.readString(Path.of("prompt.txt"));
            Llm.Resposta resposta = Llm.conversar(prompt, texto);

            JsonNode json = JSON.readTree(resposta.conteudo());
            String categoria = json.path("categoria").asText();
            String prioridade = json.path("prioridade").asText();

            // O modelo é probabilístico: nunca use a resposta sem conferir.
            if (!CATEGORIAS.contains(categoria) || !PRIORIDADES.contains(prioridade)) {
                return new Chamado(texto, "nao_classificado", "-", "a resposta veio fora do formato esperado",
                        resposta.conteudo(), resposta.tokens(), resposta.ms());
            }
            return new Chamado(texto, categoria, prioridade, json.path("motivo").asText(),
                    resposta.conteudo(), resposta.tokens(), resposta.ms());
        } catch (Exception e) {
            return new Chamado(texto, "nao_classificado", "-", "erro: " + e.getMessage(), "", 0, 0);
        }
    }
}
