package org.easy.framekwork.mvc.bean;

import java.io.Serializable;
import java.util.Map;

public class View implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3659903228993614109L;
	private String path;
	private Map<String, Object> data;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public View(String path) {
		super();
		this.path = path;
	}

	public boolean isRedirect() {
		return path.startsWith("/");
	}

}
