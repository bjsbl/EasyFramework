package org.easy.framekwork.mvc.bean;

import java.io.InputStream;
import java.io.Serializable;

public class Multipart implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1815775157314770603L;
	private String fieldName;
	private String fileName;
	private long fileSize;
	private String contentType;
	private InputStream inputStream;

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public Multipart(String fieldName, String fileName, long fileSize, String contentType, InputStream inputStream) {
		super();
		this.fieldName = fieldName;
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.contentType = contentType;
		this.inputStream = inputStream;
	}

}
