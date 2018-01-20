package model.order;

import model.Entity;

import java.sql.Timestamp;

/**
 * Created by XXH on 2017/12/29.
 */
public class SetupProvider extends Entity {

    private String providerId;

    private String providerName;

    public SetupProvider() {
        super();
    }

    public SetupProvider(String providerName) {
        this();
        this.providerName = providerName;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public boolean isBlockFlag() {
        return blockFlag;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }
}
