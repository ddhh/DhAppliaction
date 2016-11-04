package com.dh.dhappliaction.bean;

public class LoginBean {

	@Override
	public String toString() {
		return "LoginBean [result=" + result + ", code=" + code + ", user_id="
				+ user_id + "]";
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	private String result;
	private int code;
	private String user_id;

}
