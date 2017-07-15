package model.goods;

import model.Entity;

public class GoodsModel extends Entity{
	private String modelId;
	
	private AbstractGoods goods;
	
	private String modelName;
	
	private String modelCode;
	
	private String modelDescription;

	public GoodsModel() {
		super();
	}
	
	public GoodsModel(String goodsId, String modelCode, String modelName) {
		this();
		AbstractGoods goods = new RealGoods();
		goods.setGoodsId(goodsId);
		this.goods = goods;
		this.modelCode = modelCode;
		this.modelName = modelName;
	}
	
	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public AbstractGoods getGoods() {
		return goods;
	}

	public void setGoods(AbstractGoods goods) {
		this.goods = goods;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getModelDescription() {
		return modelDescription;
	}

	public void setModelDescription(String modelDescription) {
		this.modelDescription = modelDescription;
	}
}
