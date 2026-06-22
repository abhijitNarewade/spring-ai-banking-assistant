package com.banking.ai.web;

import com.banking.ai.dto.AssistantRequest;
import com.banking.ai.dto.AssistantResponse;
import com.banking.ai.service.RagAssistantService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/v1/assistant")
public class AssistantController {

    private final RagAssistantService ragAssistantService;
    private final ExecutorService streamingExecutor = Executors.newVirtualThreadPerTaskExecutor();

    public AssistantController(RagAssistantService ragAssistantService) {
        this.ragAssistantService = ragAssistantService;
    }

    @PostMapping("/chat")
    AssistantResponse chat(@Valid @RequestBody AssistantRequest request) {
        return ragAssistantService.answer(request);
    }

    @PostMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    SseEmitter stream(@Valid @RequestBody AssistantRequest request) {
        SseEmitter emitter = new SseEmitter(120_000L);
        streamingExecutor.submit(() -> {
            try {
                AssistantResponse response = ragAssistantService.answer(request);
                for (String token : Arrays.asList(response.answer().split(" "))) {
                    emitter.send(SseEmitter.event().name("token").data(token + " "));
                }
                emitter.send(SseEmitter.event().name("metadata").data(response));
                emitter.complete();
            } catch (IOException exception) {
                emitter.completeWithError(exception);
            } catch (RuntimeException exception) {
                emitter.completeWithError(exception);
            }
        });
        return emitter;
    }
}
