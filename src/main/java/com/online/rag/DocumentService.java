package com.online.rag;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final VectorStore vectorStore;
    private final TokenTextSplitter splitter = TokenTextSplitter.builder()
            .withChunkSize(500)
            .withMinChunkSizeChars(200)
            .withMinChunkLengthToEmbed(20)
            .build();
    private int uploadSingle(MultipartFile file) throws IOException {
        InputStreamResource resource = new InputStreamResource(file.getInputStream());
        PagePdfDocumentReader reader=new PagePdfDocumentReader(resource);
        List<Document> documents = reader.read();
        List<Document> chunks = splitter.split(documents);
        vectorStore.add(chunks);
        return chunks.size();
    }
    public int upload(List<MultipartFile> files) throws IOException {
        int chunks = 0;
        for (MultipartFile file : files) {
            chunks += uploadSingle(file);
        }
        return chunks;
    }
}
