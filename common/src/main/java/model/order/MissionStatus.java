package model.order;

/**
 * Created by XXH on 2018/1/17.
 */
public enum MissionStatus {
    PRE_SURVEY(0), PRE_INSTALL(1), INSTALLED(2), INSTALL_CANCEL(3);

    int codeId;

    MissionStatus(int codeId) {
        this.codeId = codeId;
    }

    public MissionStatus convertToMissionStatus(int codeId) {
        MissionStatus status = MissionStatus.PRE_SURVEY;
        switch (codeId) {
            case 0: status = MissionStatus.PRE_SURVEY;
                    break;
            case 1: status = MissionStatus.PRE_INSTALL;
                    break;
            case 2: status = MissionStatus.INSTALLED;
                    break;
            case 3: status = MissionStatus.INSTALL_CANCEL;
                    break;
            default:
                    break;
        }
        return status;
    }
}
