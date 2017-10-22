package model.servicerequest;

public enum ServiceRequestStatus {
	ADMINISTRATIVE(0), PROCESSING(1), CLOSED(2);
	
	private int code;
	
	ServiceRequestStatus(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return this.code;
	}
}
