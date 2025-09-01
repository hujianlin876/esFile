package com.esfile.common.vo;

import com.esfile.common.enums.ResponseCodeEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 统一响应结果
 * 
 * @author esfile
 * @since 1.0.0
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 响应时间
     */
    private String timestamp;

    /**
     * 请求路径
     */
    private String path;

    /**
     * 总记录数（分页时使用）
     */
    private Long total;

    /**
     * 当前页码（分页时使用）
     */
    private Integer pageNum;

    /**
     * 每页大小（分页时使用）
     */
    private Integer pageSize;

    /**
     * 私有构造函数
     */
    private ResponseResult() {
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 成功响应（无数据）
     *
     * @return 响应结果
     */
    public static <T> ResponseResult<T> success() {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode(ResponseCodeEnum.SUCCESS.getCode());
        result.setMessage(ResponseCodeEnum.SUCCESS.getMessage());
        return result;
    }

    /**
     * 成功响应（有数据）
     *
     * @param data 响应数据
     * @return 响应结果
     */
    public static <T> ResponseResult<T> success(T data) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode(ResponseCodeEnum.SUCCESS.getCode());
        result.setMessage(ResponseCodeEnum.SUCCESS.getMessage());
        result.setData(data);
        return result;
    }

    /**
     * 成功响应（自定义消息）
     *
     * @param message 响应消息
     * @return 响应结果
     */
    public static <T> ResponseResult<T> success(String message) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode(ResponseCodeEnum.SUCCESS.getCode());
        result.setMessage(message);
        return result;
    }

    /**
     * 成功响应（有数据和自定义消息）
     *
     * @param data    响应数据
     * @param message 响应消息
     * @return 响应结果
     */
    public static <T> ResponseResult<T> success(T data, String message) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode(ResponseCodeEnum.SUCCESS.getCode());
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 失败响应
     *
     * @param responseCode 响应码枚举
     * @return 响应结果
     */
    public static <T> ResponseResult<T> fail(ResponseCodeEnum responseCode) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode(responseCode.getCode());
        result.setMessage(responseCode.getMessage());
        return result;
    }

    /**
     * 失败响应（自定义消息）
     *
     * @param responseCode 响应码枚举
     * @param message      自定义消息
     * @return 响应结果
     */
    public static <T> ResponseResult<T> fail(ResponseCodeEnum responseCode, String message) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode(responseCode.getCode());
        result.setMessage(message);
        return result;
    }

    /**
     * 失败响应（自定义响应码和消息）
     *
     * @param code    响应码
     * @param message 响应消息
     * @return 响应结果
     */
    public static <T> ResponseResult<T> fail(Integer code, String message) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    /**
     * 失败响应（默认失败）
     *
     * @return 响应结果
     */
    public static <T> ResponseResult<T> fail() {
        return fail(ResponseCodeEnum.FAIL);
    }

    /**
     * 失败响应（自定义消息）
     *
     * @param message 响应消息
     * @return 响应结果
     */
    public static <T> ResponseResult<T> fail(String message) {
        return fail(ResponseCodeEnum.FAIL, message);
    }

    /**
     * 错误响应（自定义消息）
     *
     * @param message 响应消息
     * @return 响应结果
     */
    public static <T> ResponseResult<T> error(String message) {
        return fail(ResponseCodeEnum.FAIL, message);
    }

    /**
     * 参数错误响应
     *
     * @return 响应结果
     */
    public static <T> ResponseResult<T> paramError() {
        return fail(ResponseCodeEnum.PARAM_ERROR);
    }

    /**
     * 参数错误响应（自定义消息）
     *
     * @param message 响应消息
     * @return 响应结果
     */
    public static <T> ResponseResult<T> paramError(String message) {
        return fail(ResponseCodeEnum.PARAM_ERROR, message);
    }

    /**
     * 未授权响应
     *
     * @return 响应结果
     */
    public static <T> ResponseResult<T> unauthorized() {
        return fail(ResponseCodeEnum.UNAUTHORIZED);
    }

    /**
     * 未授权响应（自定义消息）
     *
     * @param message 响应消息
     * @return 响应结果
     */
    public static <T> ResponseResult<T> unauthorized(String message) {
        return fail(ResponseCodeEnum.UNAUTHORIZED, message);
    }

    /**
     * 禁止访问响应
     *
     * @return 响应结果
     */
    public static <T> ResponseResult<T> forbidden() {
        return fail(ResponseCodeEnum.FORBIDDEN);
    }

    /**
     * 禁止访问响应（自定义消息）
     *
     * @param message 响应消息
     * @return 响应结果
     */
    public static <T> ResponseResult<T> forbidden(String message) {
        return fail(ResponseCodeEnum.FORBIDDEN, message);
    }

    /**
     * 资源不存在响应
     *
     * @return 响应结果
     */
    public static <T> ResponseResult<T> notFound() {
        return fail(ResponseCodeEnum.NOT_FOUND);
    }

    /**
     * 资源不存在响应（自定义消息）
     *
     * @param message 响应消息
     * @return 响应结果
     */
    public static <T> ResponseResult<T> notFound(String message) {
        return fail(ResponseCodeEnum.NOT_FOUND, message);
    }

    /**
     * 设置请求路径
     *
     * @param path 请求路径
     * @return 当前对象
     */
    public ResponseResult<T> path(String path) {
        this.path = path;
        return this;
    }

    /**
     * 设置分页信息
     *
     * @param total    总记录数
     * @param pageNum  当前页码
     * @param pageSize 每页大小
     * @return 当前对象
     */
    public ResponseResult<T> pageInfo(Long total, Integer pageNum, Integer pageSize) {
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        return this;
    }

    /**
     * 判断是否为成功响应
     *
     * @return 是否为成功
     */
    public boolean isSuccess() {
        return ResponseCodeEnum.isSuccess(this.code);
    }

    /**
     * 判断是否为失败响应
     *
     * @return 是否为失败
     */
    public boolean isFail() {
        return ResponseCodeEnum.isFail(this.code);
    }
}
