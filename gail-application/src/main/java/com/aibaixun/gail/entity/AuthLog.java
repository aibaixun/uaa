package com.aibaixun.gail.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author hjhuang
 * @since 2022-01-06
 */
@TableName("t_auth_log")
public class AuthLog extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("userId")
    private String userId;

    @ApiModelProperty("名称")
    @TableField(exist = false)
    private String realname;

    @ApiModelProperty("最近登录")
    private String lastLoginTime;

    @ApiModelProperty("最近登录ip")
    private String lastLoginIp;

    @ApiModelProperty("激活状态")
    private String active;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

}
