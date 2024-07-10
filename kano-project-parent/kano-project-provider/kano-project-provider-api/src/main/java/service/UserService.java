package service;

import com.kano.project.common.model.Result;
import dto.UserReqDTO;

/**
 * @author kano
 */
public interface UserService {

    void queryStudent();

    /**
     * 新增用户
     * @param reqDTO
     * @return
     */
    Result<Boolean> insetUser(UserReqDTO reqDTO);

    /**
     * 登陆
     * @param reqDTO
     * @return 返回用户id
     */
    Result<Long> loginIn(UserReqDTO reqDTO);
}
