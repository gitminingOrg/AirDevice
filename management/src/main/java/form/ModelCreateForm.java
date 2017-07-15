package form;

import com.alibaba.fastjson.JSONObject;
import com.sun.istack.internal.NotNull;

public class ModelCreateForm {
	@NotNull
	private String goodsId;
	
	@NotNull
	private String modelCode;
	
	@NotNull
	private String modelName;
	
	@NotNull
	private String param;

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}
}
