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
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/spring/ai")
public class AudioController {

    private final OpenAiAudionTransciptionModel openAiAudionTransciptionModel;
    private final OpenAiAudioSpeechModel openAiAudioSpeechModel;

    public AudioController(OpenAiAudionTransciptionModel openAiAudionTransciptionModel, OpenAiAudioSpeechModel openAiAudioSpeechModel) {
        this.openAiAudionTransciptionModel = openAiAudionTransciptionModel;
        this.openAiAudioSpeechModel = openAiAudioSpeechModel;
    }

    /**
     * Audio Transcription API
     *
     * @return
     */
    @RequestMapping("/audio-to-text")
    public String audioTranscription() {
        OpenAiAudioTranscriptionOptions options = OpenAiAudioTranscriptionOptions
                .builder()
                .withLanguage("en")
                .withResponseFormat(OpenAiAudioApi.TranscriptionResponseFormat.SRT) // AUDIO, JSON, SRT
                .withTemperature(0.5f)
                .build();

        AudioTranscriptionFormat prompt = new AudioTranscriptionFormat(new ClassPathResource("audio/audioToTextCheck.mp3"), options);

        return openAiAudionTransciptionModel
                .call(prompt)
                .getResult()
                .getOutput();
    }


    /**
     * Text to Speech API
     *
     * @param prompt
     * @return
     */
    @GetMapping("/text-to-audio/{prompt}")
    public ResponseEntity<Resource> generateAudio(@PathVariable String prompt) {

        OpenAiAudioSpeechOptions options
                = OpenAiAudioSpeechOptions.builder()
                .withModel(OpenAiAudioApi.TtsModel.TTS_1.getValue())
                .withResponseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
                .withVoice(OpenAiAudioApi.SpeechRequest.Voice.NOVA) // ALLOY
                .withSpeed(0.5f) // 0.1f
                .build();

        SpeechPrompt speechPrompt = new SpeechPrompt(prompt, options);

        SpeechResponse speechResponse = openAiAudioSpeechModel.call(speechPrompt);

        byte[] output = speechResponse.getResult().getOutput();

        ByteArrayResource byteArrayResource = new ByteArrayResource(output);

        // To download audio
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(byteArrayResource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename("whatever.mp3")
                                .build().toString())
                .body(byteArrayResource);

    }

}
