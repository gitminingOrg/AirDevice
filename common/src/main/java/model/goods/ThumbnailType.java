package model.goods;

public enum ThumbnailType {
	ORDINARY(0), COVER(1);
	
	private int code;
	
	ThumbnailType(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return this.code;
	}
}
