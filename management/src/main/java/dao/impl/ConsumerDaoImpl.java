package dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import dao.BaseDao;
import dao.ConsumerDao;
import pagination.DataTablePage;
import pagination.DataTableParam;
import utils.ResponseCode;
import utils.ResultData;
import vo.consumer.ConsumerGoods;
import vo.consumer.ConsumerStatiVo;
import vo.goods.ConsumerGoodsVo;
import vo.qrcode.QRCodeVo;
@Repository
public class ConsumerDaoImpl extends BaseDao implements ConsumerDao {
	private Logger logger = LoggerFactory.getLogger(ConsumerDaoImpl.class);
	@Override
	public ResultData query(Map<String, Object> condition) {
		ResultData result = new ResultData();
		try {
			List<ConsumerGoodsVo> list = sqlSession.selectList("management.consumer.consumerGoods.query", condition);
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
		DataTablePage<ConsumerGoodsVo> page = new DataTablePage<>(param);
		ResultData total = query(condition);
		if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(total.getDescription());
			return result;
		}
		page.setiTotalRecords(((List) total.getData()).size());
		page.setiTotalDisplayRecords(((List) total.getData()).size());
		List<ConsumerGoodsVo> current = queryByPage(condition, param.getiDisplayStart(), param.getiDisplayLength());
		if (current.size() == 0) {
			result.setResponseCode(ResponseCode.RESPONSE_NULL);
		}
		page.setData(current);
		result.setData(page);
		return result;
	}

    @Override
    public ResultData queryConsumer(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
        	List<ConsumerStatiVo> list = sqlSession.selectList("management.consumer.consumerStati.query", condition);
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

    private List<ConsumerGoodsVo> queryByPage(Map<String, Object> condition, int start, int length) {
		List<ConsumerGoodsVo> list = new ArrayList<>();
		try {
			list = sqlSession.selectList("management.consumer.consumerGoods.query", condition, new RowBounds(start, length));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return list;
	}
}
