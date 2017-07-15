package vo.qrcode;

public class QRCodeVo {
	private String codeId;
	
	private String batchNo;
	
	private String value;
	
	private String path;
	
	private boolean codeDelivered;

	public String getCodeId() {
		return codeId;
	}

	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isCodeDelivered() {
		return codeDelivered;
	}

	public void setCodeDelivered(boolean codeDelivered) {
		this.codeDelivered = codeDelivered;
	}
}
