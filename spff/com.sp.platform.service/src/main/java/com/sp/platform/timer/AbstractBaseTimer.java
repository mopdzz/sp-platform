package com.sp.platform.timer;

import java.util.concurrent.Callable;

public abstract class AbstractBaseTimer implements Callable<FutureBody> {
	public abstract void init();
	public abstract void update();
	private String name;

	@Override
	public FutureBody call() throws Exception {
		try {
			update();
			return new FutureBody(true, name, "");
		} catch (Exception e) {
			return new FutureBody(false, name, e.toString());
		}
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
