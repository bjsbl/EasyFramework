package org.easy.framekwork.mvc.bean;

import java.io.Serializable;
import java.util.Map;

public class Params implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -629209949984511689L;
	private final Map<String, Object> fieldMap;

	public Params(Map<String, Object> fieldMap) {
		this.fieldMap = fieldMap;
	}

	public Map<String, Object> getFieldMap() {
		return fieldMap;
	}

}
