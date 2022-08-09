package com.zbdemo.hndl.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zbdemo.hndl.base.PageQuery;
import com.zbdemo.hndl.service.RoleService;
import com.zbdemo.hndl.user.dto.*;
import com.zbdemo.hndl.user.vo.FindUserVo;
import com.zbdemo.hndl.user.vo.RoleMenuVo;
import com.zbdemo.hndl.utlis.*;
import com.zbdemo.hndl.base.ResultData;
import com.zbdemo.hndl.enums.ResponseEnum;
import com.zbdemo.hndl.user.entity.User;
import com.zbdemo.hndl.user.vo.LoginVo;
import com.zbdemo.hndl.mapper.UserMapper;
import com.zbdemo.hndl.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.KeyPair;
import java.util.List;
import java.util.UUID;

/**
 * @author zhangbing
 * @title UserServiceImpl
 * @projectName hndl
 * @description 用户相关接口实现
 * @date 2022/7/15下午6:44
 */
@Service
@Slf4j
@RefreshScope
public class UserServiceImpl implements UserService {
    @Resource
    UserMapper userMapper;
    @Resource
    RedisUtil redisUtil;
    @Resource
    DozerUtil dozerUtil;
    @Resource
    RoleService roleService;

    /**
     * token 过期时间(秒)  2小时
     */
    @Value("${custom.tokenOutTime:7200}")
    private Integer tokenOutTime;

    /**
     * 用户密码登录随机码长度
     */
    @Value("${custom.userLoginCodeLen:5}")
    private Integer userLoginCodeLen;

    /**
     * 用户密码登录随机码过期时间
     */
    @Value("${custom.userCodeTime:120}")
    private Integer userCodeTime;
    /**
     * 用户密码登录随机码前缀标识
     */
    @Value("${custom.userCodeHead:userLoginCode}")
    private String userCodeHead;

    /**
     * 用户默认密码
     */
    @Value("${custom.userPassword:123456}")
    private String userPassword;

    @Override
    public ResultData<LoginVo> login(LoginDto loginDto, HttpServletRequest request) {
        // 判断用户是否有对应平台登陆权限
        int paasCodeNum = userMapper.findPaasCode(loginDto);
        if (paasCodeNum == 0){
            return ResultData.createResult(ResponseEnum.PAAS_CODE_ERROR);
        }
        // 判断登陆类型走不同逻辑
        if (loginDto.getLonginType() == 1){
            // 密码登陆
            // 查询用户密码
            User user = userMapper.findUser(loginDto.getLoginAccount());
            if (user == null){
                return ResultData.createResult(ResponseEnum.PAAS_CODE_ERROR);
            }
            if (StringUtils.isEmpty(user.getPassword())){
                return ResultData.createResult(ResponseEnum.PASSWORD_NULL_ERROR);
            }
            // 获取redis缓存登陆随机code
            Object randomCode = redisUtil.get(userCodeHead + loginDto.getLoginAccount()+loginDto.getPaasCode());
            if (randomCode == null){
                return ResultData.createResult(ResponseEnum.USER_LOGIN_CODE_TIMEOUT);
            }
            // MD5加密10次(密码本身是加密状态，拼接随即登陆码之后再加密10次，保证不可逆防止MD5密码库暴力破解)
            String password = MD5Util.stringToMD5(user.getPassword()+randomCode);
//            String password = "";
//            for (int i = 0;i<10;i++){
//                password = MD5Util.stringToMD5(user.getPassword()+randomCode);
//            }
            if (!password.equals(loginDto.getLoginCode())){
                return ResultData.createResult(ResponseEnum.PAAS_CODE_ERROR);
            }
            // 将user对象转换为loginVo对象
            LoginVo loginVo = dozerUtil.map(user,LoginVo.class);
            loginVo.setIpAddress(request.getRemoteAddr());
            // 获取权限菜单
            RoleMenuVo roleMenuVo = roleService.findRoleMenuById(loginVo.getRoleId()).getData();
            loginVo.setRoleMenuVo(roleMenuVo);
            // 生成token-----------------------------------------------------------------
            createToken(loginVo);
            // 判断生成token是否成功
            if (StringUtils.isEmpty(loginVo.getToken())){
                return ResultData.createResult(ResponseEnum.REDIS_ADD_ERROR);
            }
            return ResultData.success(loginVo);
        }else if (loginDto.getLonginType() == 2){
            // TODO
        }
        return ResultData.createResult(ResponseEnum.LOGIN_TYPE_ERROR);
    }

    /**
    　* @description: 创建并存储token
    　* @param LoginVo
    　* @return String
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/15 下午11:16
    　*/
    private void createToken(LoginVo loginVo){
        // 判断是否登陆，如果登陆则注销上个token
        Object userToken = redisUtil.get(loginVo.getId());
        if (userToken != null){
            redisUtil.del(userToken.toString(),loginVo.getId());
        }
        // 生成新的token
        String token = "access_token_" +UUID.randomUUID().toString().replaceAll("-", "");
        // 将token存入redis,双向存放提供注销功能
        boolean b = redisUtil.set(token, loginVo, tokenOutTime);
        boolean b1 = redisUtil.set(loginVo.getId(), token, tokenOutTime);
        if (b && b1){
            loginVo.setToken(token);
        }
    }

    @Override
    public ResultData<String> getRandomCode(String loginAccount,String paasCode) {
        // 获取随机验证码
        String randomCode = StringUtil.randomAlphanumeric(userLoginCodeLen);
        // 将随机验证码存入redis服务并设置过期时间
        if (redisUtil.set(userCodeHead+loginAccount + paasCode, randomCode, userCodeTime)) {
            return ResultData.success(randomCode);
        } else {
            return ResultData.createResult(ResponseEnum.REDIS_ADD_ERROR);
        }
    }

    @Override
    public ResultData logout(String token) {
        // 获取用户信息
        LoginVo loginVo = getLoginVo(token);
        if (loginVo != null){
            // 删除token缓存，用户缓存
            redisUtil.del(token,loginVo.getId());
        }
        return ResultData.success();
    }

    /**
    　* @description: 获取用户登陆信息
    　* @param String
    　* @return LoginVo
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/15 下午11:15
    　*/
    public LoginVo getLoginVo(String token) {
        Object object = redisUtil.get(token);
        ObjectMapper objectMapper = new ObjectMapper();
        // Object转对象
        LoginVo loginVo = objectMapper.convertValue(object, LoginVo.class);
        return loginVo;
    }

    @Override
    public ResultData insertUser(InsertUserDto insertUserDto) {
        // 将输入对象转换为DO对象
        User user = dozerUtil.map(insertUserDto,User.class);
        // 设置用户默认密码
        user.setPassword(MD5Util.stringToMD5(userPassword));
        // 初始化ID，创建时间等参数
        try {
            EntityUtil.initInsert(user);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return ResultData.createResult(ResponseEnum.ENTITY_INIT_ERROR);
        }
        userMapper.insertUser(user);
        return ResultData.success();
    }

    @Override
    public ResultData updateUser(UpdateUserDto updateUserDto) {
        // 初始化修改时间等参数
        try {
            EntityUtil.initUpdate(updateUserDto);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return ResultData.createResult(ResponseEnum.ENTITY_INIT_ERROR);
        }
        int i = userMapper.updateUser(updateUserDto);
        if (i == 0 ){
            return ResultData.createResult(ResponseEnum.RESET_PASSWORD_ERROR);
        }
        return ResultData.success();
    }

    @Override
    public ResultData deleteUser(List<String> userIds) {
        if (userIds.size() == 0){
            return ResultData.createResult(ResponseEnum.USER_ID_ERROR);
        }
        int d = userMapper.deleteUser(userIds);
        if (d > 0){
            return ResultData.success();
        }
        return ResultData.createResult(ResponseEnum.ERROR_CODE);
    }

    @Override
    public ResultData<Page<FindUserVo>> pageUserList(PageQuery<FindUserDto> pageQuery) {
        //开始分页
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize());
        //获取分页对象
        Page<FindUserVo> findUserVoPage = userMapper.pageUserList(pageQuery.getParams());
        // 查询用户是否在线
        for (FindUserVo findUserVo:findUserVoPage){
            Object o = redisUtil.get(findUserVo.getId());
            if (o != null){
                findUserVo.setOnline(1);
            }else {
                findUserVo.setOnline(0);
            }
        }
        return  ResultData.success(findUserVoPage);
    }

    @Override
    public ResultData stopUser(String userId) {
        int i = userMapper.stopUser(userId);
        if (i == 0 ){
            return ResultData.createResult(ResponseEnum.RESET_PASSWORD_ERROR);
        }
        return ResultData.success();
    }

    @Override
    public ResultData updatePassword(ForgetPasswordDto forgetPasswordDto) {
        // 查询用户信息
        User user = userMapper.findUserById(forgetPasswordDto.getId());
        // 对比老密码是否匹配
        if (forgetPasswordDto.getOldPassword().equals(user.getPassword())){
            // 对比新密码
            if (forgetPasswordDto.getFirstPassword().equals(forgetPasswordDto.getSecondPassword())){
                int i = userMapper.updatePassword(forgetPasswordDto);
                if (i == 0 ){
                    return ResultData.createResult(ResponseEnum.RESET_PASSWORD_ERROR);
                }
                return ResultData.success();
            }else {
                return ResultData.createResult(ResponseEnum.DOUBLE_PASSWORD_ERROR);
            }
        }else {
            return ResultData.createResult(ResponseEnum.OLD_PASSWORD_ERROR);
        }
    }

    @Override
    public ResultData resetPassword(String userId) {
        int i = userMapper.resetPassword(userId,MD5Util.stringToMD5(userPassword));
        if (i == 0 ){
            return ResultData.createResult(ResponseEnum.RESET_PASSWORD_ERROR);
        }
        return ResultData.success();
    }
}
