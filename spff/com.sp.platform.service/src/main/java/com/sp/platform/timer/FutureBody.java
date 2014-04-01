package com.sp.platform.timer;

import java.io.Serializable;

public class FutureBody implements Serializable {
	private static final long serialVersionUID = -3670831787633958974L;

	private boolean flag;
	private String name;
	private String errorMessage;

	public FutureBody() {

	}

	public FutureBody(boolean flag, String name, String errorMessage) {
		super();
		this.flag = flag;
		this.name = name;
		this.errorMessage = errorMessage;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
