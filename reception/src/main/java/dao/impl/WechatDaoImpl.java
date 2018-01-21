package dao.impl;

import dao.WechatDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import utils.ResultData;

/**
 * Created by sunshine on 07/01/2018.
 */
@Repository
public class WechatDaoImpl implements WechatDao {
    private Logger logger = LoggerFactory.getLogger(WechatDaoImpl.class);


    @Override
    public ResultData insert() {
        ResultData result = new ResultData();

        return result;
    }
}
