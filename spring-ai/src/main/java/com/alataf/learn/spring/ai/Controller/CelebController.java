package com.alataf.learn.spring.ai.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/spring/ai")
public class CelebController {

    private final ChatClient chatClient;

    public CelebController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @Value("classpath:prompts/celeb-details.st")
    private Resource celebPrompt;

    /**
     * API with prompt template direct String
     * to fetch the details of any famous personality using his name from chatGPT model
     *
     * @param name
     * @return
     */
    @GetMapping("/celeb/way/one")
    public String getCelebDetails(@RequestParam String name) {

        String message = """
                    List the details of the Famous personality {name}
                    along with their Carrier achievements.
                    Show the details in the readable format with bulletins point
                """;

        PromptTemplate template = new PromptTemplate(message);

        Prompt prompt = template.create(
                Map.of("name", name)
        );

        return chatClient
                .prompt(prompt)
                .call()
                .ChatResponse()
                .getResult()
                .getOutput()
                .getContent();
    }

    /**
     * API with prompt template from resources->file
     * to fetch the details of any famous personality using his name from chatGPT model
     *
     * @param name
     * @return
     */
    @GetMapping("/celeb/way/two")
    public String getCelebDetails(@RequestParam String name) {

        PromptTemplate template = new PromptTemplate(celebPrompt);

        Prompt prompt = template.create(
                Map.of("name", name)
        );

        return chatClient
                .prompt(prompt)
                .call()
                .ChatResponse()
                .getResult()
                .getOutput()
                .getContent();
    }
}
