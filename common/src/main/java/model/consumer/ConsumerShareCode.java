package model.consumer;

import model.Entity;
import utils.ShareCodeGenerator;

public class ConsumerShareCode extends Entity{
	private String codeId;
	
	private String consumerId;
	
	private String codeValue;

	public ConsumerShareCode() {
		super();
	}
	
	public ConsumerShareCode(String consumerId) {
		this();
		this.consumerId = consumerId;
		this.codeValue = ShareCodeGenerator.generate();
	}
	
	public String getCodeId() {
		return codeId;
	}

	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}

	public String getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}

	public String getCodeValue() {
		return codeValue;
	}

	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
	}
}
