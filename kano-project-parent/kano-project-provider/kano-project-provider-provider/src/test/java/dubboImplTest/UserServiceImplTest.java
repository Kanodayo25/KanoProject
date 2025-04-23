package dubboImplTest;

import com.kano.project.common.filter.MybatisPlusSqlFilter;
import com.kano.project.common.helper.SpringApplicationHelper;
import com.kano.project.common.model.Result;
import com.kano.project.provider.KanoProjectProviderApplication;
import com.kano.project.provider.dubboImpl.UserServiceImpl;
import dto.UserReqDTO;
import dto.UserResDTO;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = KanoProjectProviderApplication.class)
public class UserServiceImplTest {


    @Resource
    private UserServiceImpl service;

    @Test
    public void userTest(){
        service.queryStudent();
    }


    @Test
    public void insertTest(){
        UserReqDTO reqDTO = new UserReqDTO();
        reqDTO.setUserName("ca");
        reqDTO.setUserAccount("ylt8410407");
        reqDTO.setUserPassword("123456");

        ApplicationContext applicationContext = SpringApplicationHelper.getApplicationContext();
        MybatisPlusSqlFilter bean = applicationContext.getBean(MybatisPlusSqlFilter.class);
        Result<Boolean> booleanResult = service.insetUser(reqDTO);
        System.out.println(booleanResult);
    }

    @Test
    public void loginTest(){
        Result<UserResDTO> userResDTOResult = service.userCorrectCheckAndLogin("17763731121", "Liuao5014.");
        System.out.println(userResDTOResult);
    }
}
