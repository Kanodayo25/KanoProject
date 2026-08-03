package com.kano.project.provider.document.impl;

import com.kano.project.provider.document.DocumentParser;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

/**
 * 基于 Apache Tika 的通用文本抽取实现。
 * 一个实现覆盖 txt / pdf / doc / docx / md / html / rtf 等格式，新增文件类型无需改动代码。
 */
@Component
public class TikaDocumentParser implements DocumentParser {

    private final ApacheTikaDocumentParser delegate = new ApacheTikaDocumentParser();

    @Override
    public String parse(byte[] fileBytes, String fileName) {
        Document document = delegate.parse(new ByteArrayInputStream(fileBytes));
        return document.text();
    }
}
