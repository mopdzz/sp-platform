package com.sp.platform.util;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class CacheNode implements Serializable {
    private static final long serialVersionUID = 5525090255623718067L;

	private String nodeKey = "";
	private String nodeDate = "";
	private AtomicInteger count = new AtomicInteger(0);

	public CacheNode() {
	}

	public CacheNode(String nodeKey, String nodeDate) {
		this.nodeKey = nodeKey;
		this.nodeDate = nodeDate;
	}

	public String getNodeKey() {
		return nodeKey;
	}
	public void setNodeKey(String nodeKey) {
		this.nodeKey = nodeKey;
	}
	public String getNodeDate() {
		return nodeDate;
	}
	public void setNodeDate(String nodeDate) {
		this.nodeDate = nodeDate;
	}
	public int getCount() {
		return count.get();
	}
	public void setCount(int count) {
		this.count = new AtomicInteger(count);
	}
	public int addCount(int count) {
		return this.count.addAndGet(count);
	}
}
