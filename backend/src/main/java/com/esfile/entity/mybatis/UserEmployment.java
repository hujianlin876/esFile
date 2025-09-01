package com.esfile.entity.mybatis;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * 用户雇佣信息实体类
 * 包含用户的工作相关信息
 * 
 * @author esfile
 * @since 1.0.0
 */
public class UserEmployment extends BaseEntity {

    /**
     * 用户ID（关联用户基本信息）
     */
    private Long userId;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 职位
     */
    private String position;

    /**
     * 员工编号
     */
    private String employeeNo;

    /**
     * 入职日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime hireDate;

    /**
     * 离职日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime leaveDate;

    /**
     * 工作状态：active-在职 leave-离职 suspended-停职
     */
    private String workStatus;

    /**
     * 直属上司ID
     */
    private Long supervisorId;

    /**
     * 工作地点
     */
    private String workLocation;

    /**
     * 薪资等级
     */
    private String salaryGrade;

    /**
     * 合同类型
     */
    private String contractType;

    // =============== Getter/Setter方法 ===============

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public LocalDateTime getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDateTime hireDate) {
        this.hireDate = hireDate;
    }

    public LocalDateTime getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(LocalDateTime leaveDate) {
        this.leaveDate = leaveDate;
    }

    public String getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(String workStatus) {
        this.workStatus = workStatus;
    }

    public Long getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(Long supervisorId) {
        this.supervisorId = supervisorId;
    }

    public String getWorkLocation() {
        return workLocation;
    }

    public void setWorkLocation(String workLocation) {
        this.workLocation = workLocation;
    }

    public String getSalaryGrade() {
        return salaryGrade;
    }

    public void setSalaryGrade(String salaryGrade) {
        this.salaryGrade = salaryGrade;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    @Override
    public String toString() {
        return "UserEmployment{" +
                "id=" + getId() +
                ", userId=" + userId +
                ", deptId=" + deptId +
                ", deptName='" + deptName + '\'' +
                ", position='" + position + '\'' +
                ", employeeNo='" + employeeNo + '\'' +
                ", hireDate=" + hireDate +
                ", workStatus='" + workStatus + '\'' +
                '}';
    }
}

