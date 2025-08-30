package com.esfile.entity.dto;

import java.util.List;

/**
 * SQL执行数据传输对象
 */
public class SqlExecuteDto {
    
    /**
     * SQL语句
     */
    private String sql;
    
    /**
     * SQL参数
     */
    private List<Object> parameters;
    
    /**
     * 超时时间（秒）
     */
    private Integer timeout;
    
    /**
     * 是否只读
     */
    private Boolean readOnly;
    
    /**
     * 最大返回行数
     */
    private Integer maxRows;
    
    /**
     * 数据库名称
     */
    private String databaseName;
    
    /**
     * 数据源名称
     */
    private String dataSourceName;
    
    /**
     * 是否启用事务
     */
    private Boolean enableTransaction;
    
    /**
     * 是否记录执行日志
     */
    private Boolean logExecution;
    
    /**
     * 执行用户ID
     */
    private Long executeUserId;
    
    /**
     * 执行用户名
     */
    private String executeUserName;
    
    /**
     * 备注
     */
    private String remark;

    // Getters and Setters
    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public void setParameters(List<Object> parameters) {
        this.parameters = parameters;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Boolean getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    public Integer getMaxRows() {
        return maxRows;
    }

    public void setMaxRows(Integer maxRows) {
        this.maxRows = maxRows;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public Boolean getEnableTransaction() {
        return enableTransaction;
    }

    public void setEnableTransaction(Boolean enableTransaction) {
        this.enableTransaction = enableTransaction;
    }

    public Boolean getLogExecution() {
        return logExecution;
    }

    public void setLogExecution(Boolean logExecution) {
        this.logExecution = logExecution;
    }

    public Long getExecuteUserId() {
        return executeUserId;
    }

    public void setExecuteUserId(Long executeUserId) {
        this.executeUserId = executeUserId;
    }

    public String getExecuteUserName() {
        return executeUserName;
    }

    public void setExecuteUserName(String executeUserName) {
        this.executeUserName = executeUserName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
