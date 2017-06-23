package model.qrcode;

import model.Entity;
import model.goods.AbstractGoods;

public class QRCode extends Entity{
	private final static String DEFAULT_PATH = "/material/qrcode/default/";
    
	public final static String DEFAULT_FORMAT = "png";
    
	private String codeId;
	
    private AbstractGoods goods;
    
    private String value;
    
    private String path;
    
    private String url;
    
    private boolean occupied;
    
    public QRCode() {
    	super();
    	this.path = new StringBuffer(DEFAULT_PATH).append(codeId).append(DEFAULT_FORMAT).toString();
    	this.occupied = false;
    }
    
    public QRCode(String value) {
    	this();
    	this.value = value;
    }
    
    public QRCode(String value, AbstractGoods goods) {
    	this(value);
    	this.goods = goods;
    }
    
    public QRCode(String value, String path, String url, AbstractGoods goods) {
        this(value, goods);
        this.path = path;
        this.url = url;
    }

	public String getCodeId() {
		return codeId;
	}

	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}

	public AbstractGoods getGoods() {
		return goods;
	}

	public void setGoods(AbstractGoods goods) {
		this.goods = goods;
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

	public boolean isOccupied() {
		return occupied;
	}

	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}
}
