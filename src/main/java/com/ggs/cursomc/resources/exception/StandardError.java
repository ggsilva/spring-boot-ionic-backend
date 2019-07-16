package com.ggs.cursomc.resources.exception;

import java.io.Serializable;

public class StandardError implements Serializable {
	
	private static final long serialVersionUID = 7055475554751462953L;
	
	private Integer status;
	private String msg;
	private Long timestamp;
	
	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public Long getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

}
