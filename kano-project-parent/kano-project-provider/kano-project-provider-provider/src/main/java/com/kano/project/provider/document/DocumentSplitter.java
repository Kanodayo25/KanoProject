package com.kano.project.provider.document;

import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.segment.TextSegment;

import java.util.List;

/**
 * 文档切分接口。
 * 切分策略（段落/句子/递归/按字符）与 chunkSize / overlap 均可替换配置。
 */
public interface DocumentSplitter {

    /**
     * 将纯文本切分为向量分块，分块自动携带基础元数据并追加 chunkIndex
     *
     * @param text         纯文本
     * @param baseMetadata 基础元数据（如 documentId / fileName / 业务标签），会合并到每个分块上
     * @return 切分结果
     */
    List<TextSegment> split(String text, Metadata baseMetadata);
}
