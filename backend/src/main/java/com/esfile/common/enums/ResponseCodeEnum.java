package com.esfile.common.enums;

/**
 * 响应码枚举
 * 
 * @author esfile
 * @since 1.0.0
 */
public enum ResponseCodeEnum {

    /**
     * 成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 失败
     */
    FAIL(500, "操作失败"),

    /**
     * 参数错误
     */
    PARAM_ERROR(400, "参数错误"),

    /**
     * 未授权
     */
    UNAUTHORIZED(401, "未授权"),

    /**
     * 禁止访问
     */
    FORBIDDEN(403, "禁止访问"),

    /**
     * 资源不存在
     */
    NOT_FOUND(404, "资源不存在"),

    /**
     * 请求方法不允许
     */
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),

    /**
     * 请求超时
     */
    REQUEST_TIMEOUT(408, "请求超时"),

    /**
     * 冲突
     */
    CONFLICT(409, "资源冲突"),

    /**
     * 请求实体过大
     */
    PAYLOAD_TOO_LARGE(413, "请求实体过大"),

    /**
     * 内部服务器错误
     */
    INTERNAL_SERVER_ERROR(500, "内部服务器错误"),

    /**
     * 服务不可用
     */
    SERVICE_UNAVAILABLE(503, "服务不可用"),

    /**
     * 网关超时
     */
    GATEWAY_TIMEOUT(504, "网关超时"),

    /**
     * 用户名或密码错误
     */
    USERNAME_OR_PASSWORD_ERROR(1001, "用户名或密码错误"),

    /**
     * 用户不存在
     */
    USER_NOT_FOUND(1002, "用户不存在"),

    /**
     * 用户已被禁用
     */
    USER_DISABLED(1003, "用户已被禁用"),

    /**
     * 用户已被锁定
     */
    USER_LOCKED(1004, "用户已被锁定"),

    /**
     * 用户已存在
     */
    USER_ALREADY_EXISTS(1005, "用户已存在"),

    /**
     * 角色不存在
     */
    ROLE_NOT_FOUND(2001, "角色不存在"),

    /**
     * 角色已被禁用
     */
    ROLE_DISABLED(2002, "角色已被禁用"),

    /**
     * 权限不存在
     */
    PERMISSION_NOT_FOUND(3001, "权限不存在"),

    /**
     * 菜单不存在
     */
    MENU_NOT_FOUND(4001, "菜单不存在"),

    /**
     * 文件上传失败
     */
    FILE_UPLOAD_FAIL(5001, "文件上传失败"),

    /**
     * 文件不存在
     */
    FILE_NOT_FOUND(5002, "文件不存在"),

    /**
     * 文件类型不支持
     */
    FILE_TYPE_NOT_SUPPORTED(5003, "文件类型不支持"),

    /**
     * 文件大小超限
     */
    FILE_SIZE_EXCEEDED(5004, "文件大小超限"),

    /**
     * 文件预览失败
     */
    FILE_PREVIEW_FAIL(5005, "文件预览失败"),

    /**
     * 数据库操作失败
     */
    DATABASE_OPERATION_FAIL(6001, "数据库操作失败"),

    /**
     * SQL执行失败
     */
    SQL_EXECUTION_FAIL(6002, "SQL执行失败"),

    /**
     * 搜索失败
     */
    SEARCH_FAIL(7001, "搜索失败"),

    /**
     * 缓存操作失败
     */
    CACHE_OPERATION_FAIL(8001, "缓存操作失败"),

    /**
     * 验证码错误
     */
    CAPTCHA_ERROR(9001, "验证码错误"),

    /**
     * 验证码已过期
     */
    CAPTCHA_EXPIRED(9002, "验证码已过期"),

    /**
     * 登录失败次数过多
     */
    LOGIN_FAIL_TOO_MANY(9003, "登录失败次数过多，账户已被锁定"),

    /**
     * Token无效
     */
    TOKEN_INVALID(9004, "Token无效"),

    /**
     * Token已过期
     */
    TOKEN_EXPIRED(9005, "Token已过期"),

    /**
     * 刷新Token失败
     */
    REFRESH_TOKEN_FAIL(9006, "刷新Token失败");

    /**
     * 响应码
     */
    private final int code;

    /**
     * 响应消息
     */
    private final String message;

    /**
     * 构造函数
     *
     * @param code    响应码
     * @param message 响应消息
     */
    ResponseCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 获取响应码
     *
     * @return 响应码
     */
    public int getCode() {
        return code;
    }

    /**
     * 获取响应消息
     *
     * @return 响应消息
     */
    public String getMessage() {
        return message;
    }

    /**
     * 根据响应码获取枚举
     *
     * @param code 响应码
     * @return 枚举值
     */
    public static ResponseCodeEnum getByCode(int code) {
        for (ResponseCodeEnum responseCode : values()) {
            if (responseCode.getCode() == code) {
                return responseCode;
            }
        }
        return null;
    }

    /**
     * 判断是否为成功响应码
     *
     * @param code 响应码
     * @return 是否为成功
     */
    public static boolean isSuccess(int code) {
        return SUCCESS.getCode() == code;
    }

    /**
     * 判断是否为失败响应码
     *
     * @param code 响应码
     * @return 是否为失败
     */
    public static boolean isFail(int code) {
        return !isSuccess(code);
    }
}
