package dubboImplTest;

import com.kano.project.common.model.Result;
import com.kano.project.provider.KanoProjectProviderApplication;
import dto.UserReqDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = KanoProjectProviderApplication.class)
public class UserServiceImpl {

    @Resource
    private com.kano.project.provider.dubboImpl.UserServiceImpl service;

    @Test
    public void userTest(){
        service.queryStudent();
    }


    @Test
    public void insertTest(){
        UserReqDTO reqDTO = new UserReqDTO();
        reqDTO.setUsername("曹老师");
        Result<Boolean> booleanResult = service.insetUser(reqDTO);
        System.out.println(booleanResult);
    }
}
