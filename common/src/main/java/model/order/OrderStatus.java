package model.order;

public enum OrderStatus {
	/* 未付款, 已付款, 已发货, 已签收, 已换货, 退款中, 已退款 */
    NOT_PAYED(0), PAYED(1), SHIPPED(2), RECEIVED(3), INSTALLING(4), SUCCEED(5), EXCHANGED(6), REFUNDING(7), REFUNDED(8), CLOSED(9);

    private int code;

    OrderStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
