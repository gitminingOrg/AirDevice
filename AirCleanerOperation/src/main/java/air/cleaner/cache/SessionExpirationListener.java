package air.cleaner.cache;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.util.ExpirationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by XXH on 2017/12/25.
 */
public class SessionExpirationListener implements ExpirationListener {

    public static Logger LOG = LoggerFactory.getLogger(SessionCacheManager.class);

    public void expired(Object o) {
        LOG.info("close session..." + ((IoSession) o).getRemoteAddress());
        ((IoSession) o).close(true);
    }
}
