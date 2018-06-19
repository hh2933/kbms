package com.shuxin.model.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shuxin.commons.utils.JsonUtils;
import com.shuxin.model.Role;
import com.shuxin.model.SysUser;


/**
 * @description：UserVo
 */
public class SysUserVo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String id;

	private String userName;

	private String name;

	@JsonIgnore
	private String userPassword;

	private Integer sex;

	private Integer age;

	//private Integer userType;

	private String city;


	private Integer status;

	private String departmentId;

	private Date createTime;
	

    private String createUser;

    private Date updateTime;

    private String updateUser;
	private String phone;

	private List<Role> rolesList;

	

	private String roleIds;

	private Date createdateStart;
	private Date createdateEnd;


	
	/**
	 * 比较vo和数据库中的用户是否同一个user，采用id比较
	 * @param user 用户
	 * @return 是否同一个人
	 */
	public boolean equalsUser(SysUser user) {
		if (user == null) {
			return false;
		}
		String userId = user.getId();
		if (id == null || userId == null) {
			return false;
		}
		return id.equals(userId);
	}

	public String getDepartmentId() {
		return departmentId;
	}



	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
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
		this.createUser = createUser;
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
		this.updateUser = updateUser;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<Role> getRolesList() {
		return rolesList;
	}

	public void setRolesList(List<Role> rolesList) {
		this.rolesList = rolesList;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

	public Date getCreatedateStart() {
		return createdateStart;
	}

	public void setCreatedateStart(Date createdateStart) {
		this.createdateStart = createdateStart;
	}

	public Date getCreatedateEnd() {
		return createdateEnd;
	}

	public void setCreatedateEnd(Date createdateEnd) {
		this.createdateEnd = createdateEnd;
	}

public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	/*	@Override
	public String toString() {
		
		JsonUtils.toJson();
	}*/
	@Override
	public String toString() {
		return "SysUserVo [id=" + id + ", userName=" + userName + ", name="
				+ name + ", userPassword=" + userPassword + ", sex=" + sex
				+ ", age=" + age + ",city="+city+" status=" + status + ", departmentId="
				+ departmentId + ", createTime=" + createTime + ", createUser="
				+ createUser + ", updateTime=" + updateTime + ", updateUser="
				+ updateUser + ", phone=" + phone + ", rolesList=" + rolesList
				+ ", roleIds=" + roleIds + ", createdateStart="
				+ createdateStart + ", createdateEnd=" + createdateEnd + "]";
	}

	


}