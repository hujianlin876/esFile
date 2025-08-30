package com.esfile.entity.mybatis;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础实体类
 * 
 * @author esfile
 * @since 1.0.0
 */
@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 创建人ID
     */
    private Long createBy;

    /**
     * 更新人ID
     */
    private Long updateBy;

    /**
     * 删除标记（0：未删除，1：已删除）
     */
    @JsonIgnore
    private String deleted;

    /**
     * 备注
     */
    private String remark;

    /**
     * 版本号（乐观锁）
     */
    @JsonIgnore
    private Integer version;

    /**
     * 获取主键ID
     *
     * @return 主键ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键ID
     *
     * @param id 主键ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取创建时间
     *
     * @return 创建时间
     */
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return 更新时间
     */
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取创建人ID
     *
     * @return 创建人ID
     */
    public Long getCreateBy() {
        return createBy;
    }

    /**
     * 设置创建人ID
     *
     * @param createBy 创建人ID
     */
    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    /**
     * 获取更新人ID
     *
     * @return 更新人ID
     */
    public Long getUpdateBy() {
        return updateBy;
    }

    /**
     * 设置更新人ID
     *
     * @param updateBy 更新人ID
     */
    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    /**
     * 获取删除标记
     *
     * @return 删除标记
     */
    public String getDeleted() {
        return deleted;
    }

    /**
     * 设置删除标记
     *
     * @param deleted 删除标记
     */
    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    /**
     * 获取备注
     *
     * @return 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取版本号
     *
     * @return 版本号
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * 设置版本号
     *
     * @param version 版本号
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * 判断是否为新记录
     *
     * @return 是否为新记录
     */
    public boolean isNewRecord() {
        return this.id == null;
    }

    /**
     * 判断是否为已删除
     *
     * @return 是否已删除
     */
    public boolean isDeleted() {
        return "1".equals(this.deleted);
    }

    /**
     * 判断是否未删除
     *
     * @return 是否未删除
     */
    public boolean isNotDeleted() {
        return !isDeleted();
    }

    /**
     * 设置创建信息
     *
     * @param createBy 创建人ID
     */
    public void setCreateInfo(Long createBy) {
        this.createBy = createBy;
        this.createTime = LocalDateTime.now();
        this.updateBy = createBy;
        this.updateTime = LocalDateTime.now();
        this.deleted = "0";
        this.version = 1;
    }

    /**
     * 设置更新信息
     *
     * @param updateBy 更新人ID
     */
    public void setUpdateInfo(Long updateBy) {
        this.updateBy = updateBy;
        this.updateTime = LocalDateTime.now();
        if (this.version != null) {
            this.version++;
        }
    }

    /**
     * 设置删除信息
     *
     * @param updateBy 删除人ID
     */
    public void setDeleteInfo(Long updateBy) {
        this.updateBy = updateBy;
        this.updateTime = LocalDateTime.now();
        this.deleted = "1";
        if (this.version != null) {
            this.version++;
        }
    }
}
