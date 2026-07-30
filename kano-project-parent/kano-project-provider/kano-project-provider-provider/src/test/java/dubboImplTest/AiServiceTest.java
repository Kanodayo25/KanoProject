package dubboImplTest;


import com.kano.project.common.model.Result;
import com.kano.project.common.utils.Base64Utils;
import com.kano.project.provider.KanoProjectProviderApplication;
import com.kano.project.provider.dao.AiDao;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import service.AiService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = KanoProjectProviderApplication.class)
public class AiServiceTest {

    @Autowired
    private AiDao aiDao;

    @Reference
    private AiService aiService;

    @Test
    public void AiProjectTest(){
        String message = "资料分析主要考察什么内容";
        Result<String> stringResult = aiService.doChat(1L,message);
        System.out.println(stringResult.getData());
    }

}
