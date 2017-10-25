package model.qrcode;

import model.Entity;

public class PreBindCodeUID extends Entity {
	private String bindId;
	
	private String uid;
	
	private String codeId;
	
	public PreBindCodeUID() {
		super();
	}
	
	public PreBindCodeUID(String uid, String codeId) {
		this();
		this.uid = uid;
		this.codeId = codeId;
	}

	public String getBindId() {
		return bindId;
	}

	public void setBindId(String bindId) {
		this.bindId = bindId;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getCodeId() {
		return codeId;
	}

	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}
}
