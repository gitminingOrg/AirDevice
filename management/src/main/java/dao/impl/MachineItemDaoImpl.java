package dao.impl;

import dao.BaseDao;
import dao.MachineItemDao;
import model.order.MachineItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.IDGenerator;
import utils.ResponseCode;
import utils.ResultData;
import vo.order.MachineItemVo;
import vo.order.MachineMissionVo;

import java.util.List;
import java.util.Map;

/**
 * Created by XXH on 2018/1/20.
 */
public class MachineItemDaoImpl extends BaseDao implements MachineItemDao{
    private Logger logger = LoggerFactory.getLogger(MachineItemDao.class);
    private final Object lock = new Object();
    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<MachineItemVo> list = sqlSession.selectList("management.order.machineItem.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            } else {
                result.setData(list);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData insert(MachineItem machineItem) {
        ResultData result = new ResultData();
        machineItem.setMachineId(IDGenerator.generate("MI"));
        synchronized (lock) {
            try {
                sqlSession.insert("management.order.machineItem.insert", machineItem);
                result.setData(machineItem);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            }
        }
        return result;
    }

    @Override
    public ResultData update(MachineItem machineItem) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                sqlSession.insert("management.order.machineItem.update", machineItem);
                result.setData(machineItem);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            }
        }
        return result;
    }
}
