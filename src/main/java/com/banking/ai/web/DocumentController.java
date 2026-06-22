package com.banking.ai.web;

import com.banking.ai.dto.IngestionRequest;
import com.banking.ai.dto.IngestionResponse;
import com.banking.ai.service.DocumentIngestionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {

    private final DocumentIngestionService ingestionService;

    public DocumentController(DocumentIngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @PostMapping("/ingest")
    IngestionResponse ingest(@Valid @RequestBody IngestionRequest request) {
        return ingestionService.ingest(request);
    }
}
