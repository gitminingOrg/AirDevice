package model.goods;

public enum Type {
	REAL(0), VIRTUAL(1);
	
	private int code;
	
	Type(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return this.code;
	}
}
