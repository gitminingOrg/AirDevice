package model.qrcode;

import model.Entity;
import model.goods.AbstractGoods;

public class QRCode extends Entity{
	private final static String DEFAULT_PATH = "/material/qrcode/default/";
    
	public final static String DEFAULT_FORMAT = "png";
    
	private String codeId;
	
	private String batchNo;
	
    private AbstractGoods goods;
    
    private String modelId;
    
    private String value;
    
    private String path;
    
    private String url;
    
    private boolean delivered;
    
    private boolean occupied;
    
    public QRCode() {
    	super();
    	this.path = new StringBuffer(DEFAULT_PATH).append(codeId).append(DEFAULT_FORMAT).toString();
    	this.occupied = false;
    	this.delivered = false;
    }
    
    public QRCode(String batchNo, String value) {
    	this();
    	this.batchNo = batchNo;
    	this.value = value;
    }
    
    public QRCode(String batchNo, String value, AbstractGoods goods) {
    	this(batchNo, value);
    	this.goods = goods;
    }
    
    public QRCode(String batchNo, String value, String path, String url, AbstractGoods goods, String modelId) {
        this(batchNo, value, goods);
        this.path = path;
        this.url = url;
        this.modelId = modelId;
    }

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

	public AbstractGoods getGoods() {
		return goods;
	}

	public void setGoods(AbstractGoods goods) {
		this.goods = goods;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isDelivered() {
		return delivered;
	}

	public void setDelivered(boolean delivered) {
		this.delivered = delivered;
	}

	public boolean isOccupied() {
		return occupied;
	}

	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}
}
