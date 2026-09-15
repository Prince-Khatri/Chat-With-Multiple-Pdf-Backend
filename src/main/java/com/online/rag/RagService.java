package com.online.rag;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RagService {
    private final VectorStore vectorStore;
    private final ChatClient chatClient;

    public String ask(String question){
        List<Document> documents = vectorStore.similaritySearch(question);
        String context = documents
                .stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n"));
        return chatClient
                .prompt()
                .system(
                        """
                                You are a helpful assistant.
                                Answer the user's question using ONLY the
                                information provided in the context.
                                If the answer cannot be found in the context,
                                say "I don't know based on the provided document."
                                Context:%s
                                """.formatted(context)
                )
                .user(question)
                .call()
                .content();
    }
    public List<Document> retrive(String question){
        return vectorStore.similaritySearch(question);
    }
}
