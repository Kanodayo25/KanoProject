package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 向量检索入参
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentSearchDTO implements Serializable {

    private static final long serialVersionUID = 111114L;

    /**
     * 检索问题/关键字
     */
    private String query;

    /**
     * 返回条数，默认 5
     */
    private Integer topK;

    /**
     * 最小相似度阈值，默认 0.0
     */
    private Double minScore;

    /**
     * 集合名，为空走默认知识库
     */
    private String collectionName;
}
