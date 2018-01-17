package model.order;

/**
 * Created by XXH on 2018/1/17.
 */
public enum MachineMissionStatus {
    PRE_SURVEY(0), PRE_INSTALL(1), INSTALLED(2), INSTALL_CANCEL(3);

    private int codeId;

    MachineMissionStatus(int codeId) {
        this.codeId = codeId;
    }

    public int getCodeId() {
        return this.codeId;
    }

    public static MachineMissionStatus convertToMissionStatus(int codeId) {
        MachineMissionStatus status = MachineMissionStatus.PRE_SURVEY;
        switch (codeId) {
            case 0: status = MachineMissionStatus.PRE_SURVEY;
                    break;
            case 1: status = MachineMissionStatus.PRE_INSTALL;
                    break;
            case 2: status = MachineMissionStatus.INSTALLED;
                    break;
            case 3: status = MachineMissionStatus.INSTALL_CANCEL;
                    break;
            default:
                    break;
        }
        return status;
    }
}
