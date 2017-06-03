package model.goods;

import model.Entity;

public class Thumbnail extends Entity{
	private String thumbnailId;
	
	private AbstractGoods goods;
	
	private String path;
	
	private boolean isCover;

	public String getThumbnailId() {
		return thumbnailId;
	}

	public void setThumbnailId(String thumbnailId) {
		this.thumbnailId = thumbnailId;
	}

	public AbstractGoods getGoods() {
		return goods;
	}

	public void setGoods(AbstractGoods goods) {
		this.goods = goods;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isCover() {
		return isCover;
	}

	public void setCover(boolean isCover) {
		this.isCover = isCover;
	}
}
