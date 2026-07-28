package dubboImplTest;


import com.kano.project.provider.KanoProjectProviderApplication;
import com.kano.project.provider.dao.AiDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = KanoProjectProviderApplication.class)
public class AiServiceTest {

    @Autowired
    private AiDao aiDao;

    @Test
    public void AiProjectTest(){

    }
}
