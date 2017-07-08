package bean;

public class QRCode {
	private String codeID;
	private String goodsID;
	private String batchNo;
	private String codeValue;
	private String codePath;
	private String codeUrl;
	private int codeOccupied;
	private int blockFlag;
	private String createTime;
	public String getCodeID() {
		return codeID;
	}
	public void setCodeID(String codeID) {
		this.codeID = codeID;
	}
	public String getGoodsID() {
		return goodsID;
	}
	public void setGoodsID(String goodsID) {
		this.goodsID = goodsID;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getCodeValue() {
		return codeValue;
	}
	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
	}
	public String getCodePath() {
		return codePath;
	}
	public void setCodePath(String codePath) {
		this.codePath = codePath;
	}
	public String getCodeUrl() {
		return codeUrl;
	}
	public void setCodeUrl(String codeUrl) {
		this.codeUrl = codeUrl;
	}
	public int getCodeOccupied() {
		return codeOccupied;
	}
	public void setCodeOccupied(int codeOccupied) {
		this.codeOccupied = codeOccupied;
	}
	
	public int getBlockFlag() {
		return blockFlag;
	}
	public void setBlockFlag(int blockFlag) {
		this.blockFlag = blockFlag;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
}
