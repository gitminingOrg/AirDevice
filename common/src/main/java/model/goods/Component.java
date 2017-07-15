package model.goods;

import model.Entity;

public class Component extends Entity{
	private String componentId;
	
	private String componentName;

	public Component() {
		super();
	}
	
	public Component(String componentName) {
		this();
		this.componentName = componentName;
	}
	
	public String getComponentId() {
		return componentId;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}
}
