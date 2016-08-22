package com.zq.server.entity.params;

import io.swagger.annotations.ApiModelProperty;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 用户提交参数
 * 
 * @ClassName: UserPasswordParameter 
 * @Description TODO
 * @author zhaoqiang 
 * @date: 2016年8月22日 下午3:00:05
 */
public class UserPasswordParameter {
    @NotBlank(message = "'用户名'不能为空")
    @Email(message = "'用户名'格式错误")
    @ApiModelProperty(value = "用户名(邮箱地址)",required = true)
    private String username;

    @NotBlank(message = "'密码'不能为空")
    @Length(min=6,max=20, message="'密码'至少为6字符,最多20字符")
    @ApiModelProperty(value = "密码(密码'至少为6字符,最多20字符)",required = true)
    private String password;

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
}
