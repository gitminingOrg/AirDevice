package model.goods;

import model.Entity;

public abstract class AbstractGoods extends Entity{
	protected String goodsId;
	
	protected String goodsName;
	
	protected String goodsNickname;
	
	protected double goodsPrice;
	
	protected int bonus;
	
	protected Type type;
	
	public AbstractGoods() {
		super();
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

	public String getGoodsNickname() {
		return goodsNickname;
	}

	public void setGoodsNickname(String goodsNickname) {
		this.goodsNickname = goodsNickname;
	}

	public double getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	
	public int getBonus() {
		return bonus;
	}

	public void setBonus(int bonus) {
		this.bonus = bonus;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}
