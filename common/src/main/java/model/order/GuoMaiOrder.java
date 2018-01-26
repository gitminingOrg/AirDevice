package model.order;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class GuoMaiOrder extends Order {

    private String buyerName;
    private String buyerAccount;
    private String receiverName;
    private String receiverPhone;
    private double orderPrice;
    private String province;
    private String city;
    private String district;
    private String receiverAddress;
    private String orderChannel;
    private String orderDiversion;
    private String orderCoupon;
    private Timestamp orderTime;
    private String shipNo;
    private List<OrderItem> commodityList;

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerAccount() {
        return buyerAccount;
    }

    public void setBuyerAccount(String buyerAccount) {
        this.buyerAccount = buyerAccount;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getOrderChannel() {
        return orderChannel;
    }

    public void setOrderChannel(String orderChannel) {
        this.orderChannel = orderChannel;
    }

    public String getOrderDiversion() {
        return orderDiversion;
    }

    public void setOrderDiversion(String orderDiversion) {
        this.orderDiversion = orderDiversion;
    }

    public String getOrderCoupon() {
        return orderCoupon;
    }

    public void setOrderCoupon(String orderCoupon) {
        this.orderCoupon = orderCoupon;
    }

    public Timestamp getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Timestamp orderTime) {
        this.orderTime = orderTime;
    }

    public String getShipNo() {
        return shipNo;
    }

    public void setShipNo(String shipNo) {
        this.shipNo = shipNo;
    }

    public List<OrderItem> getCommodityList() {
        return commodityList;
    }

    public void setCommodityList(List<OrderItem> commodityList) {
        this.commodityList = commodityList;
    }



    public GuoMaiOrder(){
        super();
    }

    public GuoMaiOrder(String orderNo, String buyerName, String buyerAccount, String receiverName,
                       String receiverPhone, double orderPrice, String province, String city, String district,
                       String receiverAddress, String orderChannel, String orderCoupon,
                       Timestamp orderTime, String description)
    {
        super();
        this.orderNo = orderNo;
        this.buyerName = buyerName;
        this.buyerAccount = buyerAccount;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.orderPrice = orderPrice;
        this.province = province;
        this.city = city;
        this.district = district;
        this.receiverAddress = receiverAddress;
        this.orderChannel = orderChannel;
        this.orderCoupon = orderCoupon;
        this.orderTime = orderTime;
        this.description = description;

//        this.orderDiversion = "";
        this.shipNo = "";
    }

    public GuoMaiOrder(String[] param){
        this();
//        this.orderNo = param[0].replaceAll("'", "").replaceAll("\"", "").replaceAll("=", ""); //订单编号
//        this.buyerName = param[1];//买家姓名
//        this.price = param[2];//买家实付金额
//        this.receiverName = param[3];//收货人姓名
//        this.receiverPhone = param[4].replaceAll("'", "").replaceAll("\"", "").replaceAll("=", "");//收货人手机号码
//        this.receiverAddress = param[5];//收货人地址
//        this.coupon = param[6];//优惠码
//        this.goodsTitle = param[7];//商品名称
//        this.channel = param[8];//订单渠道
//        this.payTime = param[9];//买家付款时间
//        this.description = param[10];//备注
    }

    public static GuoMaiOrder convertFromJD(String[] param) {
        GuoMaiOrder order = new GuoMaiOrder();

        order.setOrderNo(param[0].replaceAll("'", "").replaceAll("\"", "").replaceAll("=", "")); //订单编号
        order.setBuyerName(param[14]); //买家姓名
        order.setOrderPrice(Double.valueOf(param[11])); //买家实付金额
        order.setReceiverName(param[14]); //收货人姓名
        order.setReceiverAddress(param[15]); //收货人地址
        order.setReceiverPhone(param[16].replaceAll("'", "").replaceAll("\"", "").replaceAll("=", "")); //收货人电话
        try {
            order.setOrderTime(Timestamp.valueOf(param[6])); //订单时间
        } catch (IllegalArgumentException e) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("y/M/d H:m[:s]");
            LocalDateTime localDateTime = LocalDateTime.parse(param[6], formatter);
            order.setOrderTime(Timestamp.valueOf(localDateTime));
        }
        order.setDescription(param[17]); //备注
        //判断订单状态
        if (param[24].trim().length() != 0) {
            order.setShipNo(param[24]);
            order.setOrderStatus(OrderStatus.SHIPPED);
        } else {
            order.setOrderStatus(OrderStatus.PAYED);
        }
        return order;
    }

    public static GuoMaiOrder convertFromTaoBao(String[] param) {
        GuoMaiOrder order = new GuoMaiOrder();

        order.setOrderNo(param[0].replaceAll("'", "").replaceAll("\"", "").replaceAll("=", "")); //订单编号
        order.setBuyerName(param[1]); //买家姓名
        order.setOrderPrice(Double.valueOf(param[8])); //买家实付金额
        order.setReceiverName(param[12]); //收货人姓名
        order.setReceiverAddress(param[13]); //收货人地址
        order.setReceiverPhone(param[16].replaceAll("'", "").replaceAll("\"", "").replaceAll("=", "")); //收货人电话
        order.setOrderTime(Timestamp.valueOf(param[17])); //订单时间
        order.setDescription(param[23]); //备注
        //判断订单状态
        if (param[21].trim().length() != 0) {
            order.setShipNo(param[21]);
            order.setOrderStatus(OrderStatus.SHIPPED);
        } else {
            order.setOrderStatus(OrderStatus.PAYED);
        }
        return order;
    }

    private static String generateOrderNo() {
        String prefix = "No";
        long timestamp = System.currentTimeMillis();
        return prefix + timestamp;
    }

    public static GuoMaiOrder convertFromGroupBuyingOrder(String[] param) {
        if (param[0] == null && param[5] == null) {
            return null;
        }
        if (param[0] != null && param[2] != null &&
                param[0].trim().length() == 0 && param[2].length() == 0) {
            return null;
        }
        GuoMaiOrder order = new GuoMaiOrder();
        if (param[1].length() == 0) {
            order.setOrderNo(generateOrderNo());
        } else {
            try {
                BigInteger orderNo = new BigDecimal(param[1]).toBigInteger();
                order.setOrderNo(orderNo.toString());
            } catch (Exception e) {
                order.setOrderNo(param[1].replaceAll("`", ""));
            }
        }
        order.setOrderDiversion(param[0]);
        order.setBuyerName(param[7]);
        order.setReceiverName(param[7]);
        try {
            BigInteger phone = new BigDecimal(param[8]).toBigInteger();
            order.setReceiverPhone(phone.toString());
        } catch (Exception e) {
            order.setReceiverPhone(param[8].replaceAll("`", ""));
        }
        order.setReceiverAddress(param[10]);
        if (param[5] == null || param[5].trim().length() == 0) {
            order.setOrderTime(Timestamp.valueOf(LocalDateTime.now()));
        } else {
            DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
            try {
                LocalDate dateTime = LocalDate.parse(param[5], simpleDateFormat);
                order.setOrderTime(Timestamp.valueOf(LocalDateTime.of(dateTime, LocalTime.MIN)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (param[6] != null) {
            if (param[6].contains("已付款")) {
                order.setOrderStatus(OrderStatus.PAYED);
            } else if (param[6].contains("未收到款")) {
                order.setOrderStatus(OrderStatus.NOT_PAYED);
            } else {
                // 其他情况下 设置为已付款 并且加上付款的备注
                order.setOrderStatus(OrderStatus.PAYED);
                order.setDescription(param[4] + param[6]);
            }
        } else {
            order.setOrderStatus(OrderStatus.PAYED);
        }
        if (param[2] != null) {
            List<OrderItem> orderItemList = new ArrayList<>();
            OrderItem orderItem = new OrderItem();
            if (param[2].contains("GM320A")) {
                orderItem.setCommodityId(OrderConstant.GUOMAI_320A);
            } else if (param[2].contains("GM320B")) {
                orderItem.setCommodityId(OrderConstant.GUOMAI_320B);
            } else if (param[2].contains("滤网")) {
                orderItem.setCommodityId(OrderConstant.GUOMAI_DEFAULT_SCREEN);
            } else if (param[2].contains("检测仪")) {
                orderItem.setCommodityId(OrderConstant.GUOMAI_PM25_DETACTER);
            } else {
                orderItem.setCommodityId(OrderConstant.defaultOrderCommodityId);
            }
            try {
                orderItem.setCommodityQuantity((int) Double.parseDouble(param[3]));
                if (orderItem.getCommodityQuantity() > 100) {
                    return null;
                }
            } catch (Exception e) {
                orderItem.setCommodityQuantity(1);
            }
            orderItemList.add(orderItem);
            order.setCommodityList(orderItemList);
        }

        return order;
    }
}
