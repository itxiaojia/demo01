package com.pyg.utils;

import java.io.Serializable;

/**
 * @Author: Mr.jia
 * @Date: 2018/9/19 20:16
 */
public class PygResult implements Serializable {

	private boolean success;

	private String message;

	public PygResult(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}