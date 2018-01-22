package vo.order;


import java.sql.Timestamp;

/**
 * Created by hushe on 2018/1/21.
 */
public class OrderDiversionVo {
    private String diversionId;
    private String diversionName;
    private boolean blockFlag;
    private Timestamp createAt;

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

    public boolean isBlockFlag() {
        return blockFlag;
    }

    public void setBlockFlag(boolean blockFlag) {
        this.blockFlag = blockFlag;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }
}
