package vo.order;

import model.order.SetupProvider;

import java.sql.Timestamp;

/**
 * Created by XXH on 2017/12/29.
 */
public class SetupProviderVo {

    public String providerId;

    public String providerName;

    public boolean blockFlag;

    public Timestamp createAt;


    public SetupProviderVo() {
        super();
    }

    public SetupProviderVo(SetupProvider provider) {
        this();
        this.providerId = provider.getProviderId();
        this.providerName = provider.getProviderName();
        this.blockFlag = provider.isBlockFlag();
        this.createAt = provider.getCreateAt();
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
