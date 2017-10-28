package vo.qrcode;

import java.sql.Timestamp;

public class PreBindVO {

    private String bindId;
    private String uid;
    private String codeId;
    private int blockFlag;
    private Timestamp createAt;

    public String getBindId() {
        return bindId;
    }

    public void setBindId(String bindId) {
        this.bindId = bindId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public int getBlockFlag() {
        return blockFlag;
    }

    public void setBlockFlag(int blockFlag) {
        this.blockFlag = blockFlag;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }
}
