package dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import model.device.DeviceChip;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import dao.BaseDao;
import dao.QRCodePreBindDao;
import model.qrcode.PreBindCodeUID;
import org.springframework.util.StringUtils;
import pagination.DataTablePage;
import pagination.DataTableParam;
import pagination.MobilePage;
import pagination.MobilePageParam;
import utils.IDGenerator;
import utils.ResponseCode;
import utils.ResultData;
import vo.machine.DeviceChipVO;
import vo.qrcode.PreBindVO;

import java.util.ArrayList;
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

	@Override
	// for web
	public ResultData query(Map<String, Object> condition, DataTableParam param) {
		ResultData result = new ResultData();
		DataTablePage<PreBindVO> page = new DataTablePage<>(param);

		JSONObject jsonObject = JSON.parseObject(param.getParams());
		if (!StringUtils.isEmpty(jsonObject)) {
			if (!StringUtils.isEmpty(jsonObject.getString("startDate"))) {
				condition.put("startTime", jsonObject.getString("startDate"));
			}
			if (!StringUtils.isEmpty(jsonObject.getString("endDate"))) {
				condition.put("endTime", jsonObject.getString("endDate"));
			}
		}

		if (!StringUtils.isEmpty(param.getsSearch())) {
			condition.put("search", new StringBuilder("%").append(param.getsSearch()).append("%").toString());
		}

		ResultData total = query(condition);
		if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
			result.setResponseCode(total.getResponseCode());
			result.setDescription(total.getDescription());
			return result;
		}

		page.setiTotalRecords(((List) total.getData()).size());
		page.setiTotalDisplayRecords(((List) total.getData()).size());
		List<PreBindVO> current = queryByPage(condition, param.getiDisplayStart(), param.getiDisplayLength());

		page.setData(current);
		result.setData(page);

		return result;
	}

	@Override
	public ResultData query(Map<String, Object> condition, MobilePageParam param) {
		ResultData result = new ResultData();
		MobilePage<PreBindVO> page = new MobilePage<>();

		JSONObject jsonObject = JSON.parseObject(param.getParams());
		if (!StringUtils.isEmpty(jsonObject)) {
			if (!StringUtils.isEmpty(jsonObject.getString("startDate"))) {
				condition.put("startTime", jsonObject.getString("startDate"));
			}
			if (!StringUtils.isEmpty(jsonObject.getString("endDate"))) {
				condition.put("endTime", jsonObject.getString("endDate"));
			}
		}

		ResultData total = query(condition);
		if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
			result.setResponseCode(total.getResponseCode());
			result.setDescription(total.getDescription());
			return result;
		}

		page.setTotal(((List) total.getData()).size());

		List<PreBindVO> current = queryByPage(condition, param.getStart(), param.getLength());
		page.setData(current);

		result.setData(page);
		return result;
	}

	@Override
	public ResultData deletePreBindByQrcode(String codeId) {
		ResultData result = new ResultData();
		try {
			sqlSession.update("management.qrcode.prebind.updateIdleMachine", codeId);
			sqlSession.delete("management.qrcode.prebind.deletePrebind", codeId);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		}
		return result;
	}

    private List<PreBindVO> queryByPage(Map<String, Object> condition, int start, int length) {
		List<PreBindVO> list = new ArrayList<>();
		try {
			list = sqlSession.selectList("management.qrcode.prebind.query", condition, new RowBounds(start, length));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return list;
	}
}
