package com.zbdemo.hndl.service;

import com.github.pagehelper.Page;
import com.zbdemo.hndl.base.PageQuery;
import com.zbdemo.hndl.base.ResultData;
import com.zbdemo.hndl.user.dto.*;
import com.zbdemo.hndl.user.vo.FindUserVo;
import com.zbdemo.hndl.user.vo.LoginVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author zhangbing
 * @title: UserService
 * @projectName hndl
 * @description: 用户相关接口
 * @date 2022/7/15下午6:43
 */
public interface UserService {
    /**
    　* @description: 用户登陆
    　* @param LoginDto
    　* @return ResultData
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/15 下午6:43 
    　*/
    ResultData<LoginVo> login(LoginDto loginDto, HttpServletRequest request);

    /**
    　* @description: 获取登陆随机码（用于拼接于密码后再进行加密，确保密码密文随机性保证登陆安全）
    　* @param String
    　* @return ResultData
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/15 下午7:10
    　*/
    ResultData<String> getRandomCode(String loginAccount,String paasCode);

    /**
    　* @description: 用户注销登陆
    　* @param String
    　* @return ResultData
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/15 下午10:55
    　*/
    ResultData logout(String token);

    /**
    　* @description: 新增用户
    　* @param InsertUserDto
    　* @return ResultData
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/16 上午1:26
    　*/
    ResultData insertUser(InsertUserDto insertUserDto);

    /**
    　* @description: 修改用户信息
    　* @param UpdateUserDto
    　* @return ResultData
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/18 下午10:53
    　*/
    ResultData updateUser(UpdateUserDto updateUserDto);

    /**
    　* @description: 批量删除用户
    　* @param List
    　* @return ResultData
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/18 下午11:11
    　*/
    ResultData deleteUser(List<String> userIds);

    /**
    　* @description: 查询用户列表
    　* @param PageQuery
    　* @return ResultData
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/19 上午12:07
    　*/
    ResultData<Page<FindUserVo>> pageUserList(PageQuery<FindUserDto> pageQuery);

    /**
    　* @description: 停用用户
    　* @param String
    　* @return ResultData
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/19 上午12:34
    　*/
    ResultData stopUser(String userId);

    /**
    　* @description: 修改密码
    　* @param ForgetPasswordDto
    　* @return ResultData
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/28 上午1:17
    　*/
    ResultData updatePassword(ForgetPasswordDto forgetPasswordDto);

    /**
    　* @description: 重置密码
    　* @param String
    　* @return ResultData
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/28 上午1:31
    　*/
    ResultData resetPassword(String userId);
}
