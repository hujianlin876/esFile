package com.esfile.common.util;

import com.esfile.common.vo.ResponseResult;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 控制器通用工具类
 */
public class ControllerUtil {

    /**
     * 创建分页响应结果
     */
    public static <T> ResponseResult<Map<String, Object>> createPageResult(
            List<T> list, int page, int size, long total) {
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        result.put("pages", (total + size - 1) / size);
        return ResponseResult.success(result);
    }

    /**
     * 创建成功响应结果
     */
    public static <T> ResponseResult<T> createSuccessResult(T data, String message) {
        return ResponseResult.success(data, message);
    }

    /**
     * 创建成功响应结果
     */
    public static <T> ResponseResult<T> createSuccessResult(T data) {
        return ResponseResult.success(data);
    }

    /**
     * 创建失败响应结果
     */
    public static <T> ResponseResult<T> createFailResult(String message) {
        return ResponseResult.fail(message);
    }

    /**
     * 创建操作结果响应
     */
    public static ResponseResult<String> createOperationResult(boolean success, String successMsg, String failMsg) {
        if (success) {
            return ResponseResult.success(successMsg);
        } else {
            return ResponseResult.fail(failMsg);
        }
    }

    /**
     * 创建统计信息响应
     */
    public static ResponseResult<Map<String, Object>> createStatsResult(Map<String, Object> stats) {
        return ResponseResult.success(stats);
    }

    /**
     * 安全处理异常
     */
    public static ResponseResult<String> handleException(Exception e, String operation) {
        String message = operation + "失败: " + e.getMessage();
        return ResponseResult.fail(message);
    }

    /**
     * 创建批量操作结果
     */
    public static ResponseResult<Map<String, Object>> createBatchResult(
            int successCount, int failCount, String[] errors) {
        Map<String, Object> result = new HashMap<>();
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("errors", errors);
        return ResponseResult.success(result);
    }
}
