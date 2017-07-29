package dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;

import dao.BaseDao;
import dao.ShareCodeDao;
import model.consumer.ConsumerShareCode;
import pagination.DataTablePage;
import pagination.DataTableParam;
import utils.ResponseCode;
import utils.ResultData;
import vo.consumer.ConsumerShareCodeVo;
import vo.qrcode.QRCodeVo;

@Repository
public class ShareCodeDaoImpl extends BaseDao implements ShareCodeDao {
	private Logger logger = LoggerFactory.getLogger(ShareCodeDaoImpl.class);

	@Override
	public ResultData query(Map<String, Object> condition) {
		ResultData result = new ResultData();
		try {
			List<ConsumerShareCodeVo> list = sqlSession.selectList("management.consumer.sharecode.queryfrombg", condition);
			if (list.isEmpty()) {
				result.setResponseCode(ResponseCode.RESPONSE_NULL);
			}
			result.setData(list);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		}
		return result;
	}

	@Override
	public ResultData query(Map<String, Object> condition, DataTableParam param) {
		ResultData result = new ResultData();
		DataTablePage<ConsumerShareCodeVo> page = new DataTablePage<>(param);
		if (!StringUtils.isEmpty(param.getsSearch())) {
			condition.put("search", new StringBuffer("%").append(param.getsSearch()).append("%").toString());
		}
		ResultData total = query(condition);
		if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(total.getDescription());
			return result;
		}
		page.setiTotalRecords(((List) total.getData()).size());
		page.setiTotalDisplayRecords(((List) total.getData()).size());
		List<ConsumerShareCodeVo> current = queryByPage(condition, param.getiDisplayStart(), param.getiDisplayLength());
		if (current.size() == 0) {
			result.setResponseCode(ResponseCode.RESPONSE_NULL);
		}
		page.setData(current);
		result.setData(page);
		return result;
	}

	private List<ConsumerShareCodeVo> queryByPage(Map<String, Object> condition, int start, int length) {
		List<ConsumerShareCodeVo> list = new ArrayList<>();
		try {
			list = sqlSession.selectList("management.consumer.sharecode.queryfrombg", condition,
					new RowBounds(start, length));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return list;
	}
}
