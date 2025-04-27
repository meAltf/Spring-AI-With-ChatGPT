package com.alataf.learn.spring.ai.Controller;

import com.alataf.learn.spring.ai.Model.Achievement;
import com.alataf.learn.spring.ai.Model.Player;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;

import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/spring/ai")
public class PlayerController {

    private final ChatClient chatClient;

    public PlayerController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    /**
     * way- 01
     * API to make understand of API with prompt & data, without any format
     *
     * @param name
     * @return
     */
    @GetMapping("/player/way/one")
    public String getPlayerAchievements(@RequestParam String name) {

        String message = """
                Generate a list of Career achievements for the sportsperson {sports}.
                Include the Player as the key and achievements as the value for it
                """;

        PromptTemplate template = new PromptTemplate(message);

        Prompt prompt = template.create(Map.of("sports", name));

        return chatClient
                .prompt(prompt)
                .call()
                .getChatResponse()
                .getResult()
                .getOutput()
                .getContent();

    }

    /**
     * ListOutputConverter and Entity handling in API
     * Way-02
     * API to make understand of API with prompt & data, with format using Bean ( player record)
     *
     * @param name
     * @return
     */
    @GetMapping("/player/way/two")
    public List<Player> getPlayerAchievements(String name) {

        BeanOutputConverter<List<Player>> converter =
                new BeanOutputConverter<>(new ParameterizedTypeReference<List<Player>>());

        String message = """
                Generate a list of Career achievements for the sportsperson {sports}.
                Include the Player as the key and achievements as the value for it
                {format}
                """;

        PromptTemplate template = new PromptTemplate(message);

        Prompt prompt = template.create(Map.of("sports", name,
                "formate", converter.getFormat()));

        Generation result = chatClient
                .prompt(prompt)
                .call()
                .getChatResponse()
                .getContent();

        return converter.convert(result.getOutput().getContent());

    }


    /**
     *
     * @param name
     * @return
     */
    @GetMapping("/achivements/player")
    public List<Achievement> getAchievementByPlayer(String name) {

        String message = """
                Provide a list of Achievements for {player}
                """;

        PromptTemplate template = new PromptTemplate(message);

        Prompt prompt = template.create(Map.of("player", name));

        return chatClient
                .prompt(prompt)
                .call()
                .entity(new ParameterizedTypeReference<List<Achievement>>() {
                });
    }
}
