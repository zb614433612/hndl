package com.zbdemo.hndl.mapper;

import com.github.pagehelper.IPage;
import com.github.pagehelper.Page;
import com.zbdemo.hndl.user.dto.FindUserDto;
import com.zbdemo.hndl.user.dto.ForgetPasswordDto;
import com.zbdemo.hndl.user.dto.LoginDto;
import com.zbdemo.hndl.user.dto.UpdateUserDto;
import com.zbdemo.hndl.user.entity.User;
import com.zbdemo.hndl.user.vo.FindUserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhangbing
 * @title: UserMapper
 * @projectName hndl
 * @description: 用户相关ORM映射
 * @date 2022/7/15下午6:53
 */
@Mapper
public interface UserMapper {
    /**
    　* @description: 查询用户是否拥有平台登陆权限
    　* @param LoginDto
    　* @return int
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/15 下午6:54
    　*/
    int findPaasCode(@Param("loginDto") LoginDto loginDto);

    /**
     　* @description: 查询用户信息
     　* @param String
     　* @return String
     　* @throws
     　* @author zhangbing
     　* @date 2022/7/15 下午7:27
     　*/
    User findUser(@Param("loginAccount") String loginAccount);

    /**
    　* @description: 新增用户
    　* @param User
    　* @return void
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/16 上午1:42
    　*/
    void insertUser(@Param("user") User user);

    /**
    　* @description: 修改用户信息
    　* @param UpdateUserDto
    　* @return void
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/18 下午10:58
    　*/
    int updateUser(@Param("updateUserDto") UpdateUserDto updateUserDto);

    /**
    　* @description: 批量删除用户
    　* @param List
    　* @return int
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/18 下午11:42
    　*/
    int deleteUser(List<String> userIds);

    /**
    　* @description: 分页查询用户列表
    　* @param FindUserDto
    　* @return Page
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/19 上午12:20
    　*/
    Page<FindUserVo> pageUserList(@Param("params") FindUserDto params);

    /**
    　* @description: 停用用户
    　* @param String
    　* @return void
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/19 上午12:35
    　*/
    int stopUser(String userId);

    /**
    　* @description: 根据id查询用户信息
    　* @param String
    　* @return User
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/28 上午1:19
    　*/
    User findUserById(String id);

    /**
    　* @description: 用户密码修改
    　* @param ForgetPasswordDto
    　* @return void
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/28 上午1:21
    　*/
    int updatePassword(ForgetPasswordDto forgetPasswordDto);

    /**
    　* @description: 重置密码
    　* @param userId
    　* @return void
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/28 上午1:32
    　*/
    int resetPassword(@Param("userId") String userId,@Param("password")String password);
}
