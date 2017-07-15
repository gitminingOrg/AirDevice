package vo.goods;

import java.util.List;

import model.goods.Component;

public class ModelDetailVo {
	private String modelId;
	
	private String modelCode;
	
	private String goodsId;
	
	private String goodsName;
	
	private List<ModelComponentVo> config;

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public List<ModelComponentVo> getConfig() {
		return config;
	}

	public void setConfig(List<ModelComponentVo> config) {
		this.config = config;
	}
}
