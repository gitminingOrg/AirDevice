package model.guomai;

import model.Entity;
import model.order.CommodityType;

/**
 * Created by hushe on 2018/1/8.
 */
public class Commodity extends Entity{
    private String commodityId;

    private String commodityName;

    private int commodityPrice;

    private int commodityBonus;

    private CommodityType type;

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public int getCommodityPrice() {
        return commodityPrice;
    }

    public void setCommodityPrice(int commodityPrice) {
        this.commodityPrice = commodityPrice;
    }

    public int getCommodityBonus() {
        return commodityBonus;
    }

    public void setCommodityBonus(int commodityBonus) {
        this.commodityBonus = commodityBonus;
    }

    public CommodityType getType() {
        return type;
    }

    public void setType(CommodityType type) {
        this.type = type;
    }

    public Commodity() {
        super();
    }

    public Commodity(CommodityType type, String commodityName, int commodityPrice, int commodityBonus){
        super();
        this.type = type;
        this.commodityName = commodityName;
        this.commodityPrice = commodityPrice;
        this.commodityBonus = commodityBonus;
    }
}
