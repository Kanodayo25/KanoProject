package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 检索命中的文档分块
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentChunkVO implements Serializable {

    private static final long serialVersionUID = 111115L;

    /**
     * 所属文档标识
     */
    private String documentId;

    /**
     * 所属文件名
     */
    private String fileName;

    /**
     * 分块在文档内的序号
     */
    private Integer chunkIndex;

    /**
     * 分块文本
     */
    private String text;

    /**
     * 相似度得分
     */
    private Double score;
}
