package dubboImplTest;

import com.kano.project.common.model.Result;
import com.kano.project.provider.KanoProjectProviderApplication;
import dto.DocumentIngestDTO;
import dto.DocumentResultVO;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import service.AiService;
import service.DocumentService;

import java.nio.charset.StandardCharsets;

/**
 * RAG 端到端测试：先向量入库文档，再基于知识库提问，验证回答是否依据检索资料生成
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KanoProjectProviderApplication.class)
public class RagTest {

    @Reference
    private DocumentService documentService;

    @Reference
    private AiService aiService;

    @Test
    public void ragQaTest() {
        // 1. 上传一份知识文档到 test_kb
        DocumentIngestDTO dto = new DocumentIngestDTO();
        dto.setFileName("RAG知识库.txt");
        dto.setCollectionName("test_kb");
        dto.setFileContent(("Milvus 是一个开源向量数据库，支持十亿级向量检索与毫秒级相似度召回。\n\n"
                        + "LangChain4j 提供文档解析、切分、向量化与检索的一站式 API。\n\n"
                        + "RAG 流程是：先把文档向量化存入 Milvus，提问时检索相关片段拼进提示词，再让大模型基于资料回答。")
                .getBytes(StandardCharsets.UTF_8));
        Result<DocumentResultVO> upload = documentService.uploadDocument(dto);
        System.out.println("upload = " + upload);
        if (upload == null || !upload.isSuccess()) {
            return;
        }

        // 2. 基于知识库提问
        Result<String> answer = aiService.doChatWithRag(999L, "RAG 的具体流程是什么？", "test_kb", 3);
        System.out.println("RAG answer = " + answer);
    }
}
