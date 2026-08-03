package com.kano.project.provider.document;

import java.util.List;
import java.util.Map;

/**
 * 文档入库管线接口：切分 -> embedding -> 写入向量库。
 * 实现内部复用 LangChain4j 的 embedAll + addAll 标准流程。
 */
public interface DocumentIngestor {

    /**
     * 将文本切分、向量化并写入指定集合
     *
     * @param text           抽取后的纯文本
     * @param collectionName 目标集合名（空走默认）
     * @param metadata       基础元数据，合并到每个分块
     * @return 写入的向量 id 列表，其 size 即分块数
     */
    List<String> ingest(String text, String collectionName, Map<String, String> metadata);
}
