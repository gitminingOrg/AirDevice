package model.order;

import model.Entity;

/**
 * Created by XXH on 2017/12/29.
 */
public class MissionChannel extends Entity {
    private String channelId;
    private String channelName;

    public MissionChannel() {
        super();
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
}
