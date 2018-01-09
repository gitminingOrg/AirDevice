package form;


import model.order.CommodityType;

/**
 * Created by hushe on 2018/1/9.
 */
public class CommodityForm {
    private int type;

    private String name;

    private int  price;

    private int bonus;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }
}
