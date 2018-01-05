package model.order;

public enum OrderStatus {
	/* 未付款, 已付款, 已发货, 已签收, 已换货, 退款中, 已退款 */
    NOT_PAYED(0), PAYED(1), SHIPPED(2), RECEIVED(3), INSTALLING(4), SUCCEED(5), EXCHANGED(6), REFUNDING(7), REFUNDED(8), CLOSED(9);

    private int code;

    OrderStatus(int code) {
        this.code = code;
    }

    public static OrderStatus convertToOrderStatus(int code) {
        OrderStatus orderStatus = OrderStatus.PAYED;
        switch (code) {
            case 0: orderStatus = OrderStatus.NOT_PAYED; break;
            case 1: orderStatus = OrderStatus.PAYED; break;
            case 2: orderStatus = OrderStatus.SHIPPED; break;
            case 3: orderStatus = OrderStatus.RECEIVED; break;
            case 4: orderStatus = OrderStatus.INSTALLING; break;
            case 5: orderStatus = OrderStatus.SUCCEED; break;
            case 6: orderStatus = OrderStatus.EXCHANGED; break;
            case 7: orderStatus = OrderStatus.REFUNDING; break;
            case 8: orderStatus = OrderStatus.REFUNDED; break;
            case 9: orderStatus = OrderStatus.CLOSED; break;
            default: break;
        }
        return orderStatus;
    }

    public int getCode() {
        return this.code;
    }
}
