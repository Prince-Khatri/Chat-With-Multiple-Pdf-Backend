package com.online.rag;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RagController {
    private final RagService ragService;
    private final DocumentService documentService;

    @GetMapping("/search")
    public List<Document> search(@RequestParam String question){
        return ragService.retrive(question);
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String message){
        return ragService.ask(message);

    }
    @PostMapping("/documents")
    public String uploadDocument(@RequestParam("files")MultipartFile[] files) throws IOException {
        int chunks = documentService.upload(List.of(files));
        return "Uploaded "+files.length+" files and "+chunks+" chunks";
    }
}
