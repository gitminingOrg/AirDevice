package vo.order;

import model.order.OrderChannel;

import java.sql.Timestamp;

/**
 * Created by hushe on 2017/12/16.
 */
public class OrderChannelVo {

    private String channelId;

    private String channelName;

    private boolean blockFlag;

    private Timestamp createAt;

    public OrderChannelVo() {
        super();
    }

    public OrderChannelVo(OrderChannel channel) {
        this.channelId = channel.getChannelId();
        this.channelName = channel.getChannelName();
        this.blockFlag = channel.isBlockFlag();
        this.createAt = channel.getCreateAt();
    }

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

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }
}
