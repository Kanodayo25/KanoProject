package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 文档入库结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentResultVO implements Serializable {

    private static final long serialVersionUID = 111113L;

    /**
     * 文档唯一标识（即 documentId，用于后续删除/溯源）
     */
    private String documentId;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 实际写入的集合名
     */
    private String collectionName;

    /**
     * 切分后的分块数
     */
    private int chunkCount;
}
