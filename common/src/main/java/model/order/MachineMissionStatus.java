package model.order;

/**
 * Created by XXH on 2018/1/17.
 */
public enum MachineMissionStatus {
    PRE_SURVEY(0), SURVEYED(1), SHIPPING(2), SHIPPED(3), NEGOTIATE_INSTALL(4), INSTALLED(5), INSTALL_CANCEL(6);

    static MachineMissionStatus[] status = new MachineMissionStatus[]{
            MachineMissionStatus.PRE_SURVEY, MachineMissionStatus.SURVEYED,
            MachineMissionStatus.SHIPPING, MachineMissionStatus.SHIPPED,
            MachineMissionStatus.NEGOTIATE_INSTALL, MachineMissionStatus.INSTALLED, MachineMissionStatus.INSTALL_CANCEL};

    private int code;

    MachineMissionStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static MachineMissionStatus convertToMissionStatus(int code) {
        return status[code];
    }
}
