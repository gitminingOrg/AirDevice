package vo.order;

import java.sql.Timestamp;

/**
 * Created by XXH on 2017/12/29.
 */
public class MissionChannelVo {

    public String channelId;
    public String channelName;
    public boolean blockFlag;
    public Timestamp createAt;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
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

    public void setCreateAt(Timestamp createTime) {
        this.createAt = createTime;
    }
}
