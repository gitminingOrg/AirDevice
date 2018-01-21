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

    public CommodityType getType() {
        return type;
    }

    public void setType(CommodityType type) {
        this.type = type;
    }

    public Commodity() {
        super();
    }

    public Commodity(CommodityType type, String commodityName, int commodityPrice){
        super();
        this.type = type;
        this.commodityName = commodityName;
        this.commodityPrice = commodityPrice;
    }
}
