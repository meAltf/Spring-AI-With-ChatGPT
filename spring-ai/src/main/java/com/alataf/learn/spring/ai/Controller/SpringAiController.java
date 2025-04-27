package com.alataf.learn.spring.ai.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/spring/ai/")
public class SpringAiController {

    private final ChatClient chatClient;

    public SpringAiController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    /**
     * way-01 | using direct Content
     *
     * @param message
     * @return
     */
    @GetMapping("/prompt/way/first")
    public String prompt(@RequestParam String message) {
        return chatClient
                .prompt(message)
                .call()
                .content();
    }

    /**
     * way-02 | using ChatResponse Object
     *
     * @param message
     * @return
     */
    @GetMapping("/prompt/way/second")
    public String prompt(@RequestParam String message) {
        return chatClient
                .prompt(message)
                .call()
                .chatResponse()
                .getResult()
                .getOutput()
                .getContent();
    }

}
