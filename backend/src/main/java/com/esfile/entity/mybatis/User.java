package com.esfile.entity.mybatis;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

/**
 * 用户实体类 - 重构版
 * 聚合用户的各个方面信息，保持轻量级
 * 符合阿里巴巴编码规范，控制在400行以内
 * 
 * @author esfile
 * @since 1.0.0
 */
public class User extends BaseEntity {

    // =============== 基本信息 ===============
    
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
     * 性别：male-男 female-女 unknown-未知
     */
    private String gender;

    /**
     * 用户状态：1-激活 0-未激活 2-锁定 3-禁用
     */
    private Integer status;

    // =============== 安全信息 ===============
    
    /**
     * 最后登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLoginTime;

    /**
     * 最后登录IP
     */
    private String lastLoginIp;

    /**
     * 登录失败次数
     */
    private Integer loginFailCount;

    // =============== 工作信息 ===============
    
    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 职位
     */
    private String position;

    /**
     * 员工编号
     */
    private String employeeNo;

    // =============== 扩展信息 ===============
    
    /**
     * 用户详细资料（关联UserProfile）
     */
    private UserProfile profile;

    /**
     * 用户安全信息（关联UserSecurity）
     */
    private UserSecurity security;

    /**
     * 用户雇佣信息（关联UserEmployment）
     */
    private UserEmployment employment;

    // =============== Getter/Setter方法 ===============

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public Integer getLoginFailCount() {
        return loginFailCount;
    }

    public void setLoginFailCount(Integer loginFailCount) {
        this.loginFailCount = loginFailCount;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
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

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    public UserSecurity getSecurity() {
        return security;
    }

    public void setSecurity(UserSecurity security) {
        this.security = security;
    }

    public UserEmployment getEmployment() {
        return employment;
    }

    public void setEmployment(UserEmployment employment) {
        this.employment = employment;
    }

    // =============== 业务方法 ===============

    /**
     * 检查用户是否被锁定
     */
    public boolean isLocked() {
        return Integer.valueOf(2).equals(this.status);
    }

    /**
     * 检查用户是否激活
     */
    public boolean isActive() {
        return Integer.valueOf(1).equals(this.status);
    }

    /**
     * 检查用户是否被禁用
     */
    public boolean isDisabled() {
        return Integer.valueOf(3).equals(this.status);
    }

    /**
     * 重置登录失败次数
     */
    public void resetLoginFailCount() {
        this.loginFailCount = 0;
    }

    /**
     * 增加登录失败次数
     */
    public void increaseLoginFailCount() {
        this.loginFailCount = (this.loginFailCount == null ? 0 : this.loginFailCount) + 1;
    }

    /**
     * 更新最后登录信息
     */
    public void updateLastLogin(String ip) {
        this.lastLoginTime = LocalDateTime.now();
        this.lastLoginIp = ip;
        this.resetLoginFailCount();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", realName='" + realName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", status=" + status +
                ", deptId=" + deptId +
                ", position='" + position + '\'' +
                '}';
    }
}