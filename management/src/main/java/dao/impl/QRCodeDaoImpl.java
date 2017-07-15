package dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

import dao.BaseDao;
import dao.QRCodeDao;
import model.qrcode.QRCode;
import pagination.DataTablePage;
import pagination.DataTableParam;
import utils.IDGenerator;
import utils.ResponseCode;
import utils.ResultData;
import vo.goods.BatchVo;
import vo.goods.GoodsComponentVo;
import vo.qrcode.QRCodeVo;

@Repository
public class QRCodeDaoImpl extends BaseDao implements QRCodeDao {
	private Logger logger = LoggerFactory.getLogger(QRCodeDaoImpl.class);

	private Object lock = new Object();

	@Override
	public ResultData insert(QRCode code) {
		ResultData result = new ResultData();
		code.setCodeId(IDGenerator.generate("QRC"));
		synchronized (lock) {
			try {
				sqlSession.insert("management.qrcode.insert", code);
				result.setData(code);
			} catch (Exception e) {
				logger.error(e.getMessage());
				result.setResponseCode(ResponseCode.RESPONSE_ERROR);
				result.setDescription(e.getMessage());
			}
		}
		return result;
	}
	
	@Override
	public ResultData query(Map<String, Object> condition) {
		ResultData result = new ResultData();
		try {
			List<QRCodeVo> list = sqlSession.selectList("management.qrcode.query", condition);
			if(list.isEmpty()) {
				result.setResponseCode(ResponseCode.RESPONSE_NULL);
			}
			result.setData(list);
		}catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		}
		return result;
	}

	@Override
	public ResultData queryByBatch(Map<String, Object> condition) {
		ResultData result = new ResultData();
		try {
			List<BatchVo> list = sqlSession.selectList("management.qrcode.batch.query", condition);
			if(list.isEmpty()) {
				result.setResponseCode(ResponseCode.RESPONSE_NULL);
			}
			result.setData(list);
		}catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		}
		return result;
	}

	@Override
	public ResultData query(Map<String, Object> condition, DataTableParam param) {
		ResultData result = new ResultData();
		DataTablePage<QRCodeVo> page = new DataTablePage<>(param);
		ResultData total = query(condition);
		if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(total.getDescription());
			return result;
		}
		page.setiTotalRecords(((List) total.getData()).size());
		page.setiTotalDisplayRecords(((List) total.getData()).size());
		List<QRCodeVo> current = queryByPage(condition, param.getiDisplayStart(), param.getiDisplayLength());
		if (current.size() == 0) {
			result.setResponseCode(ResponseCode.RESPONSE_NULL);
		}
		page.setData(current);
		result.setData(page);
		return result;
	}
	
	private List<QRCodeVo> queryByPage(Map<String, Object> condition, int start, int length) {
		List<QRCodeVo> list = new ArrayList<>();
		try {
			list = sqlSession.selectList("management.qrcode.query", condition, new RowBounds(start, length));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return list;
	}

	@Override
	public ResultData udpate(QRCode code) {
		ResultData result = new ResultData();
		synchronized (lock) {
			try{
				sqlSession.update("management.qrcode.update", code);
				result.setData(code);
			}catch (Exception e) {
				logger.error(e.getMessage());
				result.setResponseCode(ResponseCode.RESPONSE_ERROR);
				result.setDescription(e.getMessage());
				return result;
			}
		}
		return result;
	}
}
