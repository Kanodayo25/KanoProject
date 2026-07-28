package dubboImplTest;


import com.kano.project.common.utils.Base64Utils;

public class AiServiceTest {

    public static void main(String[] args) {
        String encode = Base64Utils.encode("sk-ws-H.EIRHPYD.eWZR.MEUCIF3yYDqPbD4bn_QhE6pO0trv0ohd5HnCAMqMjOxNM8DfAiEA4rNyFi5_QJeSZ_so2SfKVz8c_W338rKEpvYqRSV-tNQ");
        String decode = Base64Utils.decode(encode);
        System.out.println("encode:"+encode);
        System.out.println("decode:"+decode);
    }
}
