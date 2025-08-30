package com.esfile.entity.mybatis;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/**
 * 用户实体类
 * 
 * @author esfile
 * @since 1.0.0
 */
public class User extends BaseEntity {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    @JsonIgnore
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别（0：未知，1：男，2：女）
     */
    private String gender;

    /**
     * 生日
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime birthday;

    /**
     * 状态（0：禁用，1：启用）
     */
    private String status;

    /**
     * 最后登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime lastLoginTime;

    /**
     * 最后登录IP
     */
    private String lastLoginIp;

    /**
     * 登录失败次数
     */
    @JsonIgnore
    private Integer loginFailCount;

    /**
     * 账户锁定时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime lockTime;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 职位
     */
    private String position;

    /**
     * 工号
     */
    private String employeeNo;

    /**
     * 入职时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime hireDate;

    /**
     * 离职时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime leaveDate;

    /**
     * 获取用户名
     *
     * @return 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     *
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取密码
     *
     * @return 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取昵称
     *
     * @return 昵称
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 设置昵称
     *
     * @param nickname 昵称
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 获取真实姓名
     *
     * @return 真实姓名
     */
    public String getRealName() {
        return realName;
    }

    /**
     * 设置真实姓名
     *
     * @param realName 真实姓名
     */
    public void setRealName(String realName) {
        this.realName = realName;
    }

    /**
     * 获取邮箱
     *
     * @return 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮箱
     *
     * @param email 邮箱
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取手机号
     *
     * @return 手机号
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置手机号
     *
     * @param phone 手机号
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取头像
     *
     * @return 头像
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * 设置头像
     *
     * @param avatar 头像
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     * 获取性别
     *
     * @return 性别
     */
    public String getGender() {
        return gender;
    }

    /**
     * 设置性别
     *
     * @param gender 性别
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * 获取生日
     *
     * @return 生日
     */
    public LocalDateTime getBirthday() {
        return birthday;
    }

    /**
     * 设置生日
     *
     * @param birthday 生日
     */
    public void setBirthday(LocalDateTime birthday) {
        this.birthday = birthday;
    }

    /**
     * 获取状态
     *
     * @return 状态
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态
     *
     * @param status 状态
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取最后登录时间
     *
     * @return 最后登录时间
     */
    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    /**
     * 设置最后登录时间
     *
     * @param lastLoginTime 最后登录时间
     */
    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    /**
     * 获取最后登录IP
     *
     * @return 最后登录IP
     */
    public String getLastLoginIp() {
        return lastLoginIp;
    }

    /**
     * 设置最后登录IP
     *
     * @param lastLoginIp 最后登录IP
     */
    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    /**
     * 获取登录失败次数
     *
     * @return 登录失败次数
     */
    public Integer getLoginFailCount() {
        return loginFailCount;
    }

    /**
     * 设置登录失败次数
     *
     * @param loginFailCount 登录失败次数
     */
    public void setLoginFailCount(Integer loginFailCount) {
        this.loginFailCount = loginFailCount;
    }

    /**
     * 获取账户锁定时间
     *
     * @return 账户锁定时间
     */
    public LocalDateTime getLockTime() {
        return lockTime;
    }

    /**
     * 设置账户锁定时间
     *
     * @param lockTime 账户锁定时间
     */
    public void setLockTime(LocalDateTime lockTime) {
        this.lockTime = lockTime;
    }

    /**
     * 获取部门ID
     *
     * @return 部门ID
     */
    public Long getDeptId() {
        return deptId;
    }

    /**
     * 设置部门ID
     *
     * @param deptId 部门ID
     */
    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    /**
     * 获取职位
     *
     * @return 职位
     */
    public String getPosition() {
        return position;
    }

    /**
     * 设置职位
     *
     * @param position 职位
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * 获取工号
     *
     * @return 工号
     */
    public String getEmployeeNo() {
        return employeeNo;
    }

    /**
     * 设置工号
     *
     * @param employeeNo 工号
     */
    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    /**
     * 获取入职时间
     *
     * @return 入职时间
     */
    public LocalDateTime getHireDate() {
        return hireDate;
    }

    /**
     * 设置入职时间
     *
     * @param hireDate 入职时间
     */
    public void setHireDate(LocalDateTime hireDate) {
        this.hireDate = hireDate;
    }

    /**
     * 获取离职时间
     *
     * @return 离职时间
     */
    public LocalDateTime getLeaveDate() {
        return leaveDate;
    }

    /**
     * 设置离职时间
     *
     * @param leaveDate 离职时间
     */
    public void setLeaveDate(LocalDateTime leaveDate) {
        this.leaveDate = leaveDate;
    }

    /**
     * 判断是否为启用状态
     *
     * @return 是否为启用状态
     */
    public boolean isEnabled() {
        return "1".equals(this.status);
    }

    /**
     * 判断是否为禁用状态
     *
     * @return 是否为禁用状态
     */
    public boolean isDisabled() {
        return "0".equals(this.status);
    }

    /**
     * 判断是否为男性
     *
     * @return 是否为男性
     */
    public boolean isMale() {
        return "1".equals(this.gender);
    }

    /**
     * 判断是否为女性
     *
     * @return 是否为女性
     */
    public boolean isFemale() {
        return "2".equals(this.gender);
    }

    /**
     * 判断是否为未知性别
     *
     * @return 是否为未知性别
     */
    public boolean isUnknownGender() {
        return "0".equals(this.gender);
    }

    /**
     * 判断账户是否被锁定
     *
     * @return 账户是否被锁定
     */
    public boolean isLocked() {
        return this.lockTime != null;
    }

    /**
     * 判断是否为超级管理员
     *
     * @return 是否为超级管理员
     */
    public boolean isSuperAdmin() {
        return getId() != null && getId().equals(1L);
    }

    /**
     * 获取显示名称
     *
     * @return 显示名称
     */
    public String getDisplayName() {
        if (realName != null && !realName.trim().isEmpty()) {
            return realName;
        }
        if (nickname != null && !nickname.trim().isEmpty()) {
            return nickname;
        }
        return username;
    }
}
