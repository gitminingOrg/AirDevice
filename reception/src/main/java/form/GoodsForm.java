package form;

public class GoodsForm {
	private String goodsName;
	
	private String goodsNickName;
	
	private String goodsPrice;
	
	private String goodsType;
	
	private String goodsCover;
	
	private String[] goodsThumbnail;
	
	private boolean isBlock;

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsNickName() {
		return goodsNickName;
	}

	public void setGoodsNickName(String goodsNickName) {
		this.goodsNickName = goodsNickName;
	}

	public String getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public String getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}

	public String getGoodsCover() {
		return goodsCover;
	}

	public void setGoodsCover(String goodsCover) {
		this.goodsCover = goodsCover;
	}

	public String[] getGoodsThumbnail() {
		return goodsThumbnail;
	}

	public void setGoodsThumbnail(String[] goodsThumbnail) {
		this.goodsThumbnail = goodsThumbnail;
	}

	public boolean isBlock() {
		return isBlock;
	}

	public void setBlock(boolean isBlock) {
		this.isBlock = isBlock;
	}
}
