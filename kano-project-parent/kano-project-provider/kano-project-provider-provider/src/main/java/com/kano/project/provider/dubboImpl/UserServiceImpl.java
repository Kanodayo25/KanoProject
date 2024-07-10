package com.kano.project.provider.dubboImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kano.project.common.Enum.DelEnum;
import com.kano.project.common.model.Result;
import com.kano.project.provider.dao.UserDao;
import com.kano.project.provider.entity.User;
import dto.UserReqDTO;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

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

    @Override
    public Result<Long> loginIn(UserReqDTO reqDTO) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserAccount,reqDTO.getUserAccount())
                        .eq(User::getUserPassword,reqDTO.getUserPassword())
                                .eq(User::getDel, DelEnum.NOT_DEL.getCode());
        List<User> userList = userDao.list(queryWrapper);
        if(userList.isEmpty()){
            return Result.fail("用户信息不存在");
        }
        User user = userList.get(0);
        return Result.success(user.getUserid());
    }
}
