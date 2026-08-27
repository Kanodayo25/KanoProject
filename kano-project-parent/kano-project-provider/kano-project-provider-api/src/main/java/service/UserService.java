package service;

import com.kano.project.common.model.PageResult;
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

    /**
     * 注销：清除 Redis 中的登录用户信息，并复位数据库登录状态
     * @param userId
     * @return
     */
    Result<Boolean> logout(Long userId);

    /**
     * 获取 Redis 中缓存的当前登录用户信息（过期时间 2 小时）
     * @param userId
     * @return
     */
    Result<UserResDTO> getLoginUserInfo(Long userId);

    /**
     * 按账号（手机号）查询用户
     * @param userAccount
     * @return
     */
    Result<UserResDTO> getUserByAccount(String userAccount);

    /**
     * 用户分页列表
     * @param pageNo
     * @param pageSize
     * @return
     */
    Result<PageResult<UserResDTO>> pageUsers(Integer pageNo, Integer pageSize);

    /**
     * 修改用户角色
     * @param userId
     * @param role 0：管理员 1：用户
     * @return
     */
    Result<Boolean> updateUserRole(Long userId, Boolean role);

    /**
     * 删除用户（软删）
     * @param userId
     * @return
     */
    Result<Boolean> deleteUser(Long userId);
}
