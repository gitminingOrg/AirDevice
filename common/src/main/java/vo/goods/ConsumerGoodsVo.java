package vo.goods;

import java.sql.Timestamp;

import model.goods.Type;

public class ConsumerGoodsVo {
	private String goodsId;
	
	private String goodsName;
	
	private double goodsPrice;
	
	private Type goodsType;
	
	private int bonus;
	
	private boolean blockFlag;
	
	private Timestamp createAt;

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

	public double getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public Type getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(Type goodsType) {
		this.goodsType = goodsType;
	}

	public int getBonus() {
		return bonus;
	}

	public void setBonus(int bonus) {
		this.bonus = bonus;
	}

	public boolean isBlockFlag() {
		return blockFlag;
	}

	public void setBlockFlag(boolean blockFlag) {
		this.blockFlag = blockFlag;
	}

	public Timestamp getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Timestamp createAt) {
		this.createAt = createAt;
	}
}
