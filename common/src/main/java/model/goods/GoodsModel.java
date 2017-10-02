package model.goods;

import model.Entity;

public class GoodsModel extends Entity{
	private String modelId;
	
	private AbstractGoods goods;
	
	private String modelName;
	
	private String modelCode;
	
	private String modelDescription;

	private boolean isAdvanced;
	
	private String description;
	
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
	
	public GoodsModel(String goodsId, String modelCode, String modelName, boolean isAdvanced) {
		this(goodsId, modelCode, modelName);
		this.isAdvanced = isAdvanced;
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

	public boolean isAdvanced() {
		return isAdvanced;
	}

	public void setAdvanced(boolean isAdvanced) {
		this.isAdvanced = isAdvanced;
	}

	public String isDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
