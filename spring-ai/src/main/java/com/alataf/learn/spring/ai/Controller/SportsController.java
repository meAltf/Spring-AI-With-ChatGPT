package com.alataf.learn.spring.ai.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;

import java.util.List;


@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/spring/ai")
public class SportsController {

    private final ChatClient chatClient;

    public SportsController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    /**
     * API to make understand ROLES in API , like user & system roles
     *
     * @param name
     * @return
     */
    @GetMapping("/sports")
    public String getSportsDetails(@RequestParam String name) {
        String messagePrompt = """
                List the details of the sports %s
                along with their Rules and Regulation
                Show the details in readable format with bulletin points
                """;

        String systemMessagePrompt = """
                You are a smart Virtual Assistant.
                Your task is to give the details about the Sports
                If someone asking about something else and if you do not know
                Just say that you do not the answer
                """;

        UserMessage userMessage = new UserMessage(String.format(messagePrompt, name));

        SystemMessage systemMessage = new SystemMessage(systemMessagePrompt);

        Prompt prompt = new Prompt(List.of(userMessage, systemMessage));

        return chatClient
                .prompt(prompt)
                .call()
                .getChatResponse()
                .getResult()
                .getOutput()
                .getContent();
    }
}
