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
import java.util.*;
import java.util.stream.Collectors;

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


    @Test
    public void compareList() {
        List<String> list = new ArrayList<>();
        list.add("A1");
        list.add("A2");
        list.add("A3");
        list.add("A4");
        list.add("B1");
        list.add("B2");
        list.add("B3");
        list.add("B4");
        list.add("C1");
        list.add("C2");
        list.add("C3");
        list.add("C4");

        // 分组并计算每组的最大值
        Map<String, List<String>> groupedMap = list.stream()
                .collect(Collectors.groupingBy(item -> item.substring(0, 1)));

        Map<String, String> maxValues = list.stream()
                .collect(Collectors.toMap(
                        item -> item.substring(0, 1), // 分组键（字母）
                        item -> item,                  // 值（原字符串）
                        (v1, v2) -> v1.compareTo(v2) > 0 ? v1 : v2 // 保留较大的值
                ));
        

        List<String> list1 = new ArrayList<>();
        list1.add("A1");
        list1.add("A2");
        list1.add("A3");
        list1.add("B1");
        list1.add("B2");
        list1.add("C1");
        // 分组并计算每组的最大值
        Map<String, List<String>> groupedMap1 = list1.stream()
                .collect(Collectors.groupingBy(item -> item.substring(0, 1)));

        Map<String, String> maxValues1 = list1.stream()
                .collect(Collectors.toMap(
                        item -> item.substring(0, 1), // 分组键（字母）
                        item -> item,                  // 值（原字符串）
                        (v1, v2) -> v1.compareTo(v2) > 0 ? v1 : v2 // 保留较大的值
                ));

    }


    public static void main(String[] args) {
        int[] a = {9,1,2,5,7,4,8,6,3,5};
        System.out.println(Arrays.toString(a));
        shellDoing(a,10);
    }

    public static void shellDoing(int[] a,int n){
        int gap = n;
        while (gap > 1)
        {
            //gap /= 2;
            gap = gap / 3 + 1;
            for (int j = 0; j < gap; j++)
            {
                for (int i = j; i < n - gap; i += gap)
                {
                    int end = i;
                    int tmp = a[end + gap];
                    while (end >= 0)
                    {
                        if (tmp < a[end])
                        {
                            a[end + gap] = a[end];
                            end -= gap;
                        }
                        else
                        {
                            break;
                        }
                    }
                    a[end + gap] = tmp;
                }
            }
        }
        System.out.println(Arrays.toString(a));
    }

}
