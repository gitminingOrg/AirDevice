package model;

public class GmairOrder extends Entity {
    private String orderId;

    private String name;

    private String province;

    private String city;

    private String district;

    private String address;

    private String phone;

    private double longtitude;

    private double latitude;

    public GmairOrder() {
        super();
    }

    public GmairOrder(String orderId, String name, String province, String city, String district, String address, String phone) {
        this();
        this.orderId = orderId;
        this.name = name;
        this.province = province;
        this.city = city;
        this.district = district;
        this.address = address;
        this.phone = phone;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
