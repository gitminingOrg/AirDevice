package model.wechat;

import model.Entity;

public class AccessToken extends Entity{
	private String tokenId;
	
	private String tokenValue;
	
	public AccessToken() {
		super();
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getTokenValue() {
		return tokenValue;
	}

	public void setTokenValue(String tokenValue) {
		this.tokenValue = tokenValue;
	}
}
