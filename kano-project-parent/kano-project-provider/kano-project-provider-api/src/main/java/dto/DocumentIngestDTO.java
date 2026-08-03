package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * 文档入库入参
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentIngestDTO implements Serializable {

    private static final long serialVersionUID = 111112L;

    /**
     * 文件名（带扩展名），用于类型识别与元数据
     */
    private String fileName;

    /**
     * 文件二进制内容
     */
    private byte[] fileContent;

    /**
     * Milvus 集合名，为空走默认知识库
     */
    private String collectionName;

    /**
     * 额外元数据，例如 userId / 业务分类 / 来源标签
     */
    private Map<String, String> metadata;
}
