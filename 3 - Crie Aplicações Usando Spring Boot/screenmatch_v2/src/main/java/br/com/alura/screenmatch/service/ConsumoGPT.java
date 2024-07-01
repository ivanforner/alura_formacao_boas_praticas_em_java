package br.com.alura.screenmatch.service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
<<<<<<< HEAD
import io.github.cdimascio.dotenv.Dotenv;
=======
>>>>>>> 4e2120da849ba5571f345a8b91260987fc156b01

import java.util.ArrayList;
import java.util.List;

public class ConsumoGPT {
    public static String obterTraducao(String texto) {
<<<<<<< HEAD
        Dotenv dotenv = Dotenv.load();

        OpenAiService service = new OpenAiService(dotenv.get("OPENAI_APIKEY"));
=======
        OpenAiService service = new OpenAiService("");
>>>>>>> 4e2120da849ba5571f345a8b91260987fc156b01

        final List<ChatMessage> messages = new ArrayList<>();

        String systemPrompt = "Assuma que você é um tradutor e traduza a mensagem do usuário para o português do brasil.";
        final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), systemPrompt);
        final ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), texto);
        messages.add(systemMessage);
        messages.add(userMessage);

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .temperature(0.8)
                .maxTokens(1000)
                .build();

        return service.createChatCompletion(request).getChoices().get(0).getMessage().getContent();
    }
}
