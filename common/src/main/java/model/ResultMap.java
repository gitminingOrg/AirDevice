package model;

import java.util.HashMap;
import java.util.Map;

public class ResultMap {
	public static final int STATUS_SUCCESS = 1;
	public static final int STATUS_FAILURE = 0;
	public static final int STATUS_FORBIDDEN = 2;
	
	public static final String EMPTY_INFO = "";
	
	private int status;
	private String info;
	private Map<String, Object> contents;
	
	public ResultMap() {
		contents = new HashMap<String, Object>();
		status = STATUS_FAILURE;
		info = EMPTY_INFO;
	}
	
	public void addContent(String name, Object content){
		contents.put(name, content);
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public Map<String, Object> getContents() {
		return contents;
	}

	public void setContents(Map<String, Object> contents) {
		this.contents = contents;
	}

	public int getStatus() {
		return status;
	}

	public String getInfo() {
		return info;
	}

}
