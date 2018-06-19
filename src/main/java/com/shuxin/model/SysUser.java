package com.shuxin.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
@TableName("t_user")
public class SysUser  implements Serializable {
	
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;
	
	
	/** 主键id */
	@TableId(type = IdType.UUID)
    private String id;
	/** 登陆名 */
	@TableField(value = "user_name")
    private String userName;
	@TableField(value = "user_password")
    private String userPassword;

    private String name;

    private Integer sex;

    private Integer age;

    private String phone;

    private String email;
    
    private String city;

    private Integer status;
    @TableField(value = "create_time")
    private Date createTime;

    @TableField(value = "create_user")
    private String createUser;
  

	@TableField(value = "update_time")
    private Date updateTime;
    @TableField(value = "update_user")
    private String updateUser;
/*    @TableField(value = "department_id")
    private String departmentId;
    public String getDepartmentId() {
  		return departmentId;
  	}

  	public void setDepartmentId(String departmentId) {
  		this.departmentId = departmentId;
  	}
*/
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword == null ? null : userPassword.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser == null ? null : updateUser.trim();
    }

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
}