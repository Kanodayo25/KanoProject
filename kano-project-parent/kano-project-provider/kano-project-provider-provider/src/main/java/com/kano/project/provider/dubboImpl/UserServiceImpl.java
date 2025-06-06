package com.kano.project.provider.dubboImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kano.project.common.model.Result;
import com.kano.project.common.utils.DozerUtils;
import com.kano.project.provider.dao.UserDao;
import com.kano.project.provider.entity.User;
import com.kano.project.provider.utils.RedisUtils;
import dto.UserReqDTO;
import dto.UserResDTO;
import io.jsonwebtoken.lang.Assert;
import lombok.Synchronized;
import objEnum.LoginStatusEnum;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

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
        String loginStatus = String.valueOf(redisUtils.get(String.valueOf(user.getUserId())));
        if(LoginStatusEnum.LOGINED.getCode().equals(loginStatus)){
            return Result.fail("用户已登录");
        }
        redisUtils.set(user.getUserId().toString(),"1");
        //设置登录状态
        user.setIsLogin(Boolean.TRUE);
        boolean updateFlag = userDao.updateById(user);
        Assert.isTrue(updateFlag,"登录状态修改失败");
        return Result.success(DozerUtils.map(user, UserResDTO.class));
    }
}
