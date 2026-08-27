package com.kano.project.provider.dubboImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kano.project.common.model.PageResult;
import com.kano.project.common.model.Result;
import com.kano.project.common.utils.DozerUtils;
import com.kano.project.provider.dao.UserDao;
import com.kano.project.provider.entity.User;
import com.kano.project.provider.utils.RedisUtils;
import dto.UserReqDTO;
import dto.UserResDTO;
import lombok.Synchronized;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    /** 登录用户信息 Redis key 前缀：login:user:{userId} */
    private static final String LOGIN_USER_KEY = "login:user:";
    /** 登录用户信息有效期（秒）：2 小时 */
    private static final long LOGIN_USER_TTL = 7200L;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public void queryStudent() {
        System.out.println("hello,dubbo!");
    }

    @Override
    public Result<Boolean> insetUser(UserReqDTO reqDTO){
        // 角色未指定时默认普通用户（1：用户）
        if (reqDTO.getRole() == null) {
            reqDTO.setRole(Boolean.TRUE);
        }
        User user = new User();
        BeanUtils.copyProperties(reqDTO,user);
        boolean save = userDao.save(user);
        if (!save) {
            return Result.fail("保存失败");
        }
        return Result.success(Boolean.TRUE);
    }


    @Synchronized
    @Override
    public Result<UserResDTO> userCorrectCheckAndLogin(String userAccount, String userPassword) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserAccount,userAccount)
                .eq(User::getUserPassword,userPassword);
        List<User> userList = userDao.list(queryWrapper);
        if(userList.isEmpty()){
            return Result.fail("用户账号密码不正确");
        }
        User user = userList.get(0);
        // 已登录（Redis 中 2 小时内仍有登录信息）则拒绝重复登录
        if(redisUtils.hasKey(LOGIN_USER_KEY + user.getUserId())){
            return Result.fail("用户已登录");
        }
        // 登录用户信息写入 Redis，有效期 2 小时（登录状态由 Redis 承载，不落库）
        redisUtils.set(LOGIN_USER_KEY + user.getUserId(), buildLoginInfo(user), LOGIN_USER_TTL);
        return Result.success(DozerUtils.map(user, UserResDTO.class));
    }

    @Override
    public Result<Boolean> logout(Long userId) {
        // 清除 Redis 中的登录用户信息（登录状态由 Redis 承载，数据库无需复位）
        redisUtils.del(LOGIN_USER_KEY + userId);
        return Result.success(Boolean.TRUE);
    }

    @Override
    public Result<UserResDTO> getLoginUserInfo(Long userId) {
        Object cache = redisUtils.get(LOGIN_USER_KEY + userId);
        if (cache == null) {
            return Result.fail("未登录或登录已过期");
        }
        return Result.success((UserResDTO) cache);
    }

    @Override
    public Result<UserResDTO> getUserByAccount(String userAccount) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserAccount, userAccount)
                .eq(User::getDel, false);
        User user = userDao.getOne(queryWrapper, false);
        if (user == null) {
            return Result.fail("用户不存在");
        }
        UserResDTO dto = DozerUtils.map(user, UserResDTO.class);
        dto.setUserPassword(null);
        return Result.success(dto);
    }

    @Override
    public Result<PageResult<UserResDTO>> pageUsers(Integer pageNo, Integer pageSize) {
        int no = (pageNo == null || pageNo < 1) ? 1 : pageNo;
        int size = (pageSize == null || pageSize < 1) ? 10 : pageSize;
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getDel, false)
                .orderByDesc(User::getCreatedTime);
        Page<User> page = userDao.page(new Page<>(no, size), queryWrapper);
        List<UserResDTO> list = page.getRecords().stream()
                .map(user -> {
                    UserResDTO dto = DozerUtils.map(user, UserResDTO.class);
                    dto.setUserPassword(null);
                    return dto;
                })
                .collect(Collectors.toList());
        return Result.success(new PageResult<>(page.getCurrent(), page.getSize(), page.getTotal(), list));
    }

    @Override
    public Result<Boolean> updateUserRole(Long userId, Boolean role) {
        User user = userDao.getById(userId);
        if (user == null) {
            return Result.fail("用户不存在");
        }
        user.setRole(role);
        userDao.updateById(user);
        return Result.success(Boolean.TRUE);
    }

    @Override
    public Result<Boolean> deleteUser(Long userId) {
        User user = userDao.getById(userId);
        if (user == null) {
            return Result.fail("用户不存在");
        }
        // 软删：标记 del=true，保留数据便于追溯
        user.setDel(Boolean.TRUE);
        userDao.updateById(user);
        return Result.success(Boolean.TRUE);
    }

    /**
     * 构建写入 Redis 的登录用户信息：
     * 不缓存密码；时间字段（LocalDateTime 默认 Jackson 序列化器不支持）与逻辑删除标记一并置空
     */
    private UserResDTO buildLoginInfo(User user) {
        UserResDTO dto = DozerUtils.map(user, UserResDTO.class);
        dto.setUserPassword(null);
        dto.setCreatedTime(null);
        dto.setUpdatedTime(null);
        dto.setDel(null);
        return dto;
    }
}
