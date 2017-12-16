package model.order;

import model.Entity;

import java.nio.channels.Channel;

/**
 * Created by hushe on 2017/12/16.
 */
public class OrderChannel extends Entity {
    private String ChannelId;
    private String ChannelName;

    public String getChannelId() {
        return ChannelId;
    }

    public void setChannelId(String channelId) {
        ChannelId = channelId;
    }

    public String getChannelName() {
        return ChannelName;
    }

    public void setChannelName(String channelName) {
        ChannelName = channelName;
    }
}
