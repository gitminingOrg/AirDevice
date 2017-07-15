package service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.ComponentDao;
import dao.GoodsDao;
import dao.GoodsModelDao;
import model.goods.Component;
import model.goods.GoodsModel;
import model.goods.ModelComponent;
import pagination.DataTableParam;
import service.GoodsService;
import utils.ResponseCode;
import utils.ResultData;

@Service
public class GoodsServiceImpl implements GoodsService {
	private Logger logger = LoggerFactory.getLogger(GoodsService.class);

	@Autowired
	private GoodsDao goodsDao;

	@Autowired
	private GoodsModelDao goodsModelDao;

	@Autowired
	private ComponentDao componentDao;

	@Override
	public ResultData fetch(Map<String, Object> condition) {
		ResultData result = new ResultData();
		ResultData response = goodsDao.query(condition);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() != ResponseCode.RESPONSE_ERROR) {
			result.setData(response.getData());
		}
		return result;
	}

	@Override
	public ResultData fetchModel(Map<String, Object> condition) {
		ResultData result = new ResultData();
		ResultData response = goodsModelDao.query(condition);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() != ResponseCode.RESPONSE_ERROR) {
			result.setData(response.getData());
		}
		return result;
	}

	@Override
	public ResultData fetchComponent(Map<String, Object> condition) {
		ResultData result = new ResultData();
		ResultData response = componentDao.query(condition);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() != ResponseCode.RESPONSE_ERROR) {
			result.setData(response.getData());
		}
		return result;
	}

	@Override
	public ResultData fetchComponent(Map<String, Object> condition, DataTableParam param) {
		ResultData result = new ResultData();
		ResultData response = componentDao.query(condition, param);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		} else {
			response.setDescription(response.getDescription());
		}
		return result;
	}

	@Override
	public ResultData createComponnet(Component component) {
		ResultData result = new ResultData();
		ResultData response = componentDao.insert(component);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		} else {
			result.setDescription(response.getDescription());
		}
		return result;
	}

	@Override
	public ResultData fetchModelDetail(Map<String, Object> condition) {
		ResultData result = new ResultData();
		ResultData response = goodsModelDao.queryModelDetail(condition);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
			result.setDescription(response.getDescription());
		} else {
			result.setData(response.getData());
		}
		return result;
	}

	@Override
	public ResultData createModel(GoodsModel model) {
		ResultData result = new ResultData();
		ResultData response = goodsModelDao.insert(model);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		} else {
			result.setDescription(response.getDescription());
		}
		return result;
	}

	@Override
	public ResultData createModelComponentConfig(List<ModelComponent> list) {
		ResultData result = new ResultData();
		ResultData response = goodsModelDao.insertComponent4Model(list);
		result.setResponseCode(response.getResponseCode());
		if(response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		}else {
			result.setDescription(response.getDescription());
		}
		return result;
	}
}
