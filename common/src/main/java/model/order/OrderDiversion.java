package model.order;

import model.Entity;

/**
 * Created by hushe on 2018/1/21.
 */
public class OrderDiversion extends Entity {
    private String diversionId;
    private String diversionName;

    public OrderDiversion(){super();}

    public OrderDiversion(String diversionName) {
        this();
        this.diversionName = diversionName;
    }

    public String getDiversionId() {
        return diversionId;
    }

    public void setDiversionId(String diversionId) {
        this.diversionId = diversionId;
    }

    public String getDiversionName() {
        return diversionName;
    }

    public void setDiversionName(String diversionName) {
        this.diversionName = diversionName;
    }

}
