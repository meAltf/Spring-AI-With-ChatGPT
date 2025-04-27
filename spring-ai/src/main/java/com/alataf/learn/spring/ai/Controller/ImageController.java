package com.alataf.learn.spring.ai.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/spring/ai")
public class ImageController {

    private final ChatModel chatModel;
    private final ImageModel imageModel;

    public ImageController(ChatModel chatModel, ImageModel imageModel) {
        this.chatModel = chatModel;
        this.imageModel = imageModel;
    }

    /**
     * Image to Text generation API
     *
     * @return
     */
    @GetMapping("/image-to-text")
    public String describeImage() {
        String response = ChatClient.create(chatModel)
                .prompt()
                .user(useSpec -> useSpec.text("Explain what you see in this Image")
                        .media(MimeTypeUtils.IMAGE_JPEG,
                                new ClassPathResource("images/download.jpg")))
                .call()
                .getContent();

        return response;
    }


    /**
     * Image Generation API
     *
     * @param prompt
     * @return
     */
    @GetMapping("/image/{prompt}")
    public String generateImage(@PathVariable String prompt) {

        ImageResponse response = imageModel.call(
                new ImagePrompt(prompt,
                        OpenAiImageOptions
                                .builder()
                                .withN(1)
                                .withWidth(1024)
                                .withHeight(1024)
                                .withQuality("hd")
                                .build())
        );

        return response.getResult().getOutput().getUrl();
    }
}
