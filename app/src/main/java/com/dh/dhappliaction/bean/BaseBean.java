
package com.dh.dhappliaction.bean;

public class BaseBean {
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

	private String result;
	private int code;

	@Override
	public String toString() {
		return "BaseBean{" +
				"result='" + result + '\'' +
				", code=" + code +
				'}';
	}
}
