package com.zbdemo.hndl.enums;


public enum ResponseEnum {

    /**
     * 成功
     */
    SUCCESS_CODE(200, "成功"),

    /**
     * 300-399 参数错误
     */
    USER_ID_ERROR(300, "用户id不能为空"),
    /**
     * 400-499 用户错误
     */
    PAAS_CODE_ERROR(400, "用户不存在或无该平台登陆权限"),
    LOGIN_TYPE_ERROR(401, "登陆类型参数错误,登陆类型为1或2"),
    PASSWORD_NULL_ERROR(402, "用户未设置密码，请查看该用户信息是否正常"),
    USER_LOGIN_CODE_TIMEOUT(403, "登陆随机码失效"),
    TOKEN_NULL_ERROR(404, "鉴权失败,缺少token参数"),
    TOKEN_ERROR(405, "token已过期或者无效"),
    /**
     * 500-599 系统错误
     * 系统错误
     */
    ENTITY_INIT_ERROR(500, "初始化对象失败"),
    IP_ADDRESS_ERROR(501, "登陆ip地址与请求ip地址不一致，请重新登陆"),
    /**
     * 600-699 数据库错误
     */
    REDIS_ADD_ERROR(600, "redis新增失败"),
    /**
     * 700-799 接口错误
     */

    /**
     * 800-899 业务错误
     */

    ERROR_CODE(900, "失败");
    ResponseEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private String msg;

    private int code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
