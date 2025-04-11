package service;

import com.kano.project.common.model.Result;
import dto.UserReqDTO;
import dto.UserResDTO;

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
     * 用户账号密码验证
     * @param userAccount
     * @param userPassword
     * @return
     */
    Result<UserResDTO> userCorrectCheckAndLogin(String userAccount,String userPassword);
}
