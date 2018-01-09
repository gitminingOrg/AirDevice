package model.order;

/**
 * Created by XXH on 2018/1/8.
 */
public enum CommodityType {
    GUOMAI_XINFENG(0), GUOMAI_SCREEN(1), GUOMAI_OTHER(2);

    int code;

    CommodityType(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static CommodityType convertToCommodityType(int code) {
        CommodityType commodityType = CommodityType.GUOMAI_XINFENG;
        switch (code) {
            case 0: commodityType = CommodityType.GUOMAI_XINFENG; break;
            case 1: commodityType = CommodityType.GUOMAI_SCREEN; break;
            case 2: commodityType = CommodityType.GUOMAI_OTHER; break;
            default: break;
        }
        return commodityType;
    }
}
