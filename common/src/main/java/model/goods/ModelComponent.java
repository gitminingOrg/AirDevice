package model.goods;

import model.Entity;

public class ModelComponent extends Entity{
	
	private GoodsModel model;
	
	private String componentId;
	
	private String supplierName;
	
	public ModelComponent() {
		super();
	}
	
	public ModelComponent(GoodsModel model, String componentId, String supplierName) {
		this();
		this.model = model;
		this.componentId = componentId;
		this.supplierName = supplierName;
	}
	
	public GoodsModel getModel() {
		return model;
	}

	public void setModel(GoodsModel model) {
		this.model = model;
	}

	public String getComponentId() {
		return componentId;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
}
