package model.order;

import model.Entity;

import java.sql.Timestamp;

/**
 * Created by hushe on 2017/12/16.
 */
public class OrderChannel extends Entity {
    private String channelId;

    private String channelName;

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

    public Timestamp getCreateAt() {
        return createAt;
    }
}
