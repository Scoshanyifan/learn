package com.scosyf.learn.annotation.common;

import java.io.Serializable;
import java.util.Date;

public class Log implements Serializable{
	
	/**
	 * 2018年12月6日
	 * @author syf
	 */
	private static final long serialVersionUID = -3807148358980143174L;
	
	private Long id;
	private String className;
	private String methodName;
	private String description;
	private String parameter;
	private Long userId;
	private String ip;
	private Long costTime;
	private Date logTime;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Long getCostTime() {
		return costTime;
	}
	public void setCostTime(Long costTime) {
		this.costTime = costTime;
	}
	public Date getLogTime() {
		return logTime;
	}
	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "Log [id=" + id + ", className=" + className + ", methodName="
				+ methodName + ", description=" + description + ", parameter="
				+ parameter + ", userId=" + userId + ", ip=" + ip
				+ ", costTime=" + costTime + ", logTime=" + logTime + "]";
	}
	
}
