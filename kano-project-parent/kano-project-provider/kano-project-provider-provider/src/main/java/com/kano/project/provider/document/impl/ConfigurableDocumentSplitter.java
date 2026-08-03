package com.kano.project.provider.document.impl;

import com.kano.project.provider.document.DocumentMetadataKeys;
import com.kano.project.provider.document.DocumentSplitter;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.document.splitter.DocumentByCharacterSplitter;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import dev.langchain4j.data.document.splitter.DocumentBySentenceSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 可配置的切分实现。
 * 策略与参数全部走 application.properties，业务侧零改动即可调整切分粒度。
 */
@Component
public class ConfigurableDocumentSplitter implements DocumentSplitter {

    private final dev.langchain4j.data.document.DocumentSplitter delegate;

    public ConfigurableDocumentSplitter(
            @Value("${doc.splitter.type:paragraph}") String type,
            @Value("${doc.chunk.size:500}") int chunkSize,
            @Value("${doc.chunk.overlap:50}") int chunkOverlap) {
        this.delegate = buildDelegate(type, chunkSize, chunkOverlap);
    }

    private static dev.langchain4j.data.document.DocumentSplitter buildDelegate(String type, int chunkSize, int chunkOverlap) {
        switch (type.toLowerCase()) {
            case "character":
                return new DocumentByCharacterSplitter(chunkSize, chunkOverlap);
            case "sentence":
                return new DocumentBySentenceSplitter(chunkSize, chunkOverlap);
            case "recursive":
                return DocumentSplitters.recursive(chunkSize, chunkOverlap);
            case "paragraph":
            default:
                return new DocumentByParagraphSplitter(chunkSize, chunkOverlap);
        }
    }

    @Override
    public List<TextSegment> split(String text, Metadata baseMetadata) {
        List<TextSegment> rawSegments = delegate.split(new Document(text, baseMetadata));
        List<TextSegment> result = new ArrayList<>(rawSegments.size());
        for (int i = 0; i < rawSegments.size(); i++) {
            Metadata segmentMetadata = new Metadata();
            if (baseMetadata != null) {
                for (Map.Entry<String, Object> entry : baseMetadata.toMap().entrySet()) {
                    segmentMetadata.put(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
            segmentMetadata.put(DocumentMetadataKeys.CHUNK_INDEX, String.valueOf(i));
            result.add(TextSegment.from(rawSegments.get(i).text(), segmentMetadata));
        }
        return result;
    }
}
