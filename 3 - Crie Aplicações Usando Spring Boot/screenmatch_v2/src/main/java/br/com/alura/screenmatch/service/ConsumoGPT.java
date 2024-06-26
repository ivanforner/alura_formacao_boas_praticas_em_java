package br.com.alura.screenmatch.service;

import com.theokanning.openai.OpenAiResponse;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class ConsumoGPT {
    public String obterTraducao(String texto) {
        OpenAiService service = new OpenAiService("token");

        CompletionRequest request = CompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .maxTokens(1000)
                .prompt("Traduza para o português do brasil o texto: " + texto)
                .temperature(0.8)
                .build();

        return service.createCompletion(request).getChoices().get(0).getText();
    }
}
