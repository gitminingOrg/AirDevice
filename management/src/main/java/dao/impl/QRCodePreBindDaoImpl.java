package dao.impl;

import model.device.DeviceChip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import dao.BaseDao;
import dao.QRCodePreBindDao;
import model.qrcode.PreBindCodeUID;
import utils.IDGenerator;
import utils.ResponseCode;
import utils.ResultData;
import vo.machine.DeviceChipVO;
import vo.qrcode.PreBindVO;

import java.util.List;
import java.util.Map;

@Repository
public class QRCodePreBindDaoImpl extends BaseDao implements QRCodePreBindDao{
	private Logger logger = LoggerFactory.getLogger(QRCodeDaoImpl.class);
	
	private final Object lock = new Object();
	
	@Override
	public ResultData insert(PreBindCodeUID pb) {
		ResultData result = new ResultData();
		pb.setBindId(IDGenerator.generate("PBC"));
		synchronized (lock) {
			try {
				sqlSession.insert("management.qrcode.prebind.insert", pb);
				result.setData(pb);
			}catch(Exception e) {
				logger.error(e.getMessage());
				result.setResponseCode(ResponseCode.RESPONSE_ERROR);
				result.setDescription(e.getMessage());
			}
		}
		return result;
	}

	// 判断二维码是否已经被绑定
	@Override
	public ResultData selectPreBindByQrcode(String qrcode) {
		ResultData result = new ResultData();
		try {
			List<PreBindVO> preBindCodeUID =
					sqlSession.selectList("management.qrcode.prebind.selectByQrcode", qrcode);
			if (preBindCodeUID.size() == 0)
				result.setResponseCode(ResponseCode.RESPONSE_NULL);
			result.setData(preBindCodeUID);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		}

		return result;
	}

	// 使用uid查询预绑定表单的相关记录
	@Override
	public ResultData selectPreBindByUid(String uid) {
		ResultData result = new ResultData();
		try {
			List<PreBindVO> preBindCodeUID =
					sqlSession.selectList("management.qrcode.prebind.selectByUid", uid);
			if (preBindCodeUID.size() == 0)
				result.setResponseCode(ResponseCode.RESPONSE_NULL);
			result.setData(preBindCodeUID);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		}
		return result;
	}

	// 使用uid查询已经绑定的相关记录
	@Override
	public ResultData selectChipDeviceByUid(String uid) {
		ResultData result = new ResultData();
		try {
			List<DeviceChipVO> deviceChipVOS =
					sqlSession.selectList("management.machine.device_chip.selectByUid", uid);
			if (deviceChipVOS.size() == 0)
				result.setResponseCode(ResponseCode.RESPONSE_NULL);
			result.setData(deviceChipVOS);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		}
		return result;
	}

	@Override
	public ResultData query(Map<String, Object> condition) {
		ResultData result = new ResultData();
		try {
			List<PreBindVO> preBindVOS =
					sqlSession.selectList("management.qrcode.prebind.query", condition);
			if (preBindVOS.size() == 0) {
				result.setResponseCode(ResponseCode.RESPONSE_NULL);
			}
			result.setData(preBindVOS);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		}
		return result;
	}
}
