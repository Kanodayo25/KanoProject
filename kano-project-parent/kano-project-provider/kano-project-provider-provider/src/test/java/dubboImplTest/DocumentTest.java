package dubboImplTest;

import com.kano.project.common.model.Result;
import com.kano.project.provider.KanoProjectProviderApplication;
import com.kano.project.provider.dao.DocumentDao;
import dto.DocumentChunkVO;
import dto.DocumentIngestDTO;
import dto.DocumentResultVO;
import dto.DocumentSearchDTO;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import service.DocumentService;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 文档知识库端到端测试：入库 -> 检索 -> 按 documentId 删除
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KanoProjectProviderApplication.class)
public class DocumentTest {

    private static final String COLLECTION = "test_kb";

    @Autowired
    private DocumentDao documentDao;

    @Reference
    private DocumentService documentService;

    private DocumentIngestDTO buildTxtDoc(String fileName, String content) {
        DocumentIngestDTO dto = new DocumentIngestDTO();
        dto.setFileName(fileName);
        dto.setFileContent(content.getBytes(StandardCharsets.UTF_8));
        dto.setCollectionName(COLLECTION);
        return dto;
    }

    @Test
    public void uploadDocumentTest() {
        DocumentIngestDTO dto = buildTxtDoc("测试文档.txt",
                "Milvus 是一个开源的向量数据库，支持十亿级向量检索。\n\n"
                        + "文档入库会先抽取文本，再按段落切分，最后向量化写入 Milvus。");
        Result<DocumentResultVO> result = documentService.uploadDocument(dto);
        System.out.println("uploadDocumentTest result = " + result);
    }

    @Test
    public void searchTest() {
        DocumentSearchDTO dto = new DocumentSearchDTO();
        dto.setQuery("向量数据库能存多少数据");
        dto.setCollectionName(COLLECTION);
        dto.setTopK(3);
        dto.setMinScore(0.0);
        Result<List<DocumentChunkVO>> result = documentService.search(dto);
        System.out.println("searchTest result = " + result);
    }

    /**
     * 完整链路：入库 -> 检索 -> 按 documentId 删除 -> 删除后再次检索应无命中
     */
    @Test
    public void uploadSearchDeleteLifecycleTest() {
        // 1. 入库
        DocumentIngestDTO dto = buildTxtDoc("生命周期测试.txt",
                "第一条知识：Spring Boot 3 使用 Java 17 作为基础运行时。\n\n"
                        + "第二条知识：LangChain4j 提供了文档解析、切分与向量化的一站式 API。\n\n"
                        + "第三条知识：Milvus 通过相似度检索召回相关文本片段。");
        Result<DocumentResultVO> uploadResult = documentService.uploadDocument(dto);
        System.out.println("upload result = " + uploadResult);
        if (uploadResult == null || !uploadResult.isSuccess()) {
            return;
        }
        String documentId = uploadResult.getData().getDocumentId();
        String collectionName = uploadResult.getData().getCollectionName();

        // 2. 检索
        DocumentSearchDTO searchDTO = new DocumentSearchDTO();
        searchDTO.setQuery("LangChain4j 能做什么");
        searchDTO.setCollectionName(collectionName);
        searchDTO.setTopK(3);
        Result<List<DocumentChunkVO>> searchResult = documentService.search(searchDTO);
        System.out.println("search result = " + searchResult);

        // 3. 删除
        Result<Boolean> deleteResult = documentService.deleteDocument(documentId, collectionName);
        System.out.println("delete result = " + deleteResult);

        // 4. 删除后再次检索，验证该文档已无命中
        Result<List<DocumentChunkVO>> afterDelete = documentService.search(searchDTO);
        System.out.println("after delete search = " + afterDelete);
    }

    /**
     * 不走 Dubbo，直接调 dao 编排层（便于单步调试）
     */
    @Test
    public void directDaoUploadTest() {
        DocumentIngestDTO dto = buildTxtDoc("直接dao调用.txt",
                "这是通过 @Autowired DocumentDao 直接调用编排层的文档。");
        DocumentResultVO vo = documentDao.upload(dto);
        System.out.println("directDaoUploadTest result = " + vo);
    }
}
