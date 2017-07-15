package dao;

import java.util.List;
import java.util.Map;

import model.goods.GoodsModel;
import model.goods.ModelComponent;
import utils.ResultData;

public interface GoodsModelDao {
	ResultData query(Map<String, Object> condition);
	
	ResultData queryModelDetail(Map<String, Object> condition);
	
	ResultData insert(GoodsModel model);
	
	ResultData insertComponent4Model(List<ModelComponent> list);
}
