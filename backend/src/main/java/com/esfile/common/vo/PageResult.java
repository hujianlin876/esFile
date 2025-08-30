package com.esfile.common.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果
 * 
 * @author esfile
 * @since 1.0.0
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 当前页码
     */
    private Integer pageNum;

    /**
     * 每页大小
     */
    private Integer pageSize;

    /**
     * 总页数
     */
    private Integer totalPages;

    /**
     * 是否有上一页
     */
    private Boolean hasPrevious;

    /**
     * 是否有下一页
     */
    private Boolean hasNext;

    /**
     * 是否为第一页
     */
    private Boolean isFirst;

    /**
     * 是否为最后一页
     */
    private Boolean isLast;

    /**
     * 数据列表
     */
    private List<T> list;

    /**
     * 私有构造函数
     */
    private PageResult() {
    }

    /**
     * 创建分页结果
     *
     * @param list      数据列表
     * @param total     总记录数
     * @param pageNum   当前页码
     * @param pageSize  每页大小
     * @param <T>       数据类型
     * @return 分页结果
     */
    public static <T> PageResult<T> of(List<T> list, Long total, Integer pageNum, Integer pageSize) {
        PageResult<T> result = new PageResult<>();
        result.setList(list);
        result.setTotal(total);
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        
        // 计算总页数
        if (total != null && pageSize != null && pageSize > 0) {
            result.setTotalPages((int) Math.ceil((double) total / pageSize));
        }
        
        // 计算分页信息
        if (pageNum != null && result.getTotalPages() != null) {
            result.setHasPrevious(pageNum > 1);
            result.setHasNext(pageNum < result.getTotalPages());
            result.setIsFirst(pageNum == 1);
            result.setIsLast(pageNum.equals(result.getTotalPages()));
        }
        
        return result;
    }

    /**
     * 创建空分页结果
     *
     * @param pageNum  当前页码
     * @param pageSize 每页大小
     * @param <T>      数据类型
     * @return 分页结果
     */
    public static <T> PageResult<T> empty(Integer pageNum, Integer pageSize) {
        return of(null, 0L, pageNum, pageSize);
    }

    /**
     * 创建空分页结果（默认第一页，每页10条）
     *
     * @param <T> 数据类型
     * @return 分页结果
     */
    public static <T> PageResult<T> empty() {
        return empty(1, 10);
    }

    /**
     * 获取起始索引
     *
     * @return 起始索引
     */
    public Integer getStartIndex() {
        if (pageNum == null || pageSize == null) {
            return 0;
        }
        return (pageNum - 1) * pageSize;
    }

    /**
     * 获取结束索引
     *
     * @return 结束索引
     */
    public Integer getEndIndex() {
        if (pageNum == null || pageSize == null) {
            return 0;
        }
        return pageNum * pageSize - 1;
    }

    /**
     * 判断是否为空
     *
     * @return 是否为空
     */
    public boolean isEmpty() {
        return list == null || list.isEmpty();
    }

    /**
     * 判断是否不为空
     *
     * @return 是否不为空
     */
    public boolean isNotEmpty() {
        return !isEmpty();
    }

    /**
     * 获取数据条数
     *
     * @return 数据条数
     */
    public Integer getSize() {
        return list == null ? 0 : list.size();
    }
}
