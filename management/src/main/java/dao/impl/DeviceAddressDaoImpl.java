package dao.impl;

import dao.DeviceAddressDao;
import dao.BaseDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import utils.ResponseCode;
import utils.ResultData;
import vo.address.DeviceAddressVO;

import java.util.List;
import java.util.Map;


@Repository
public class DeviceAddressDaoImpl extends BaseDao implements DeviceAddressDao {
    Logger logger = LoggerFactory.getLogger(DeviceAddressDaoImpl.class);

    @Override
    public ResultData getDeviceAddress(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<DeviceAddressVO> list =
                    sqlSession.selectList("management.deviceAddress.selectDeviceAddress", condition);
            if (list.size() == 0)
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
