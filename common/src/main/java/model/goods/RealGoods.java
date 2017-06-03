package model.goods;

import java.util.List;

public class RealGoods extends AbstractGoods {
	private Thumbnail cover;
	
	private List<Thumbnail> thumbnails;
	
	public RealGoods() {
		super();
		type = Type.REAL;
	}
	
	public RealGoods(String goodsName, double goodsPrice) {
		this();
		this.goodsName = goodsName;
		this.goodsPrice = goodsPrice;
	}
	
	public RealGoods(String goodsName, String goodsNickName, double goodsPrice) {
		this(goodsName, goodsPrice);
		this.goodsNickname = goodsNickName;
	}
	
	public RealGoods(String goodsName, double goodsPrice, Thumbnail cover, List<Thumbnail> thumbnails) {
		this(goodsName, goodsPrice);
		this.cover = cover;
		this.thumbnails = thumbnails;
	}
	
	public RealGoods(String goodsName, String goodsNickName, double goodsPrice, Thumbnail cover, List<Thumbnail> thumbnails) {
		this(goodsName, goodsNickName, goodsPrice);
		this.cover = cover;
		this.thumbnails = thumbnails;
	}

	public Thumbnail getCover() {
		return cover;
	}

	public void setCover(Thumbnail cover) {
		this.cover = cover;
	}

	public List<Thumbnail> getThumbnails() {
		return thumbnails;
	}

	public void setThumbnails(List<Thumbnail> thumbnails) {
		this.thumbnails = thumbnails;
	}	
}
