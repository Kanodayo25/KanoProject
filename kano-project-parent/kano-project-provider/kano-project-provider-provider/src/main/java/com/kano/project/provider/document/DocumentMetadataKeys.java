package com.kano.project.provider.document;

/**
 * 向量分块元数据 Key 常量，统一管理避免散落字符串
 */
public final class DocumentMetadataKeys {

    /**
     * 文档唯一标识
     */
    public static final String DOCUMENT_ID = "documentId";

    /**
     * 文件名
     */
    public static final String FILE_NAME = "fileName";

    /**
     * 分块序号
     */
    public static final String CHUNK_INDEX = "chunkIndex";

    private DocumentMetadataKeys() {
    }
}
