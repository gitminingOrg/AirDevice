package service;

import java.util.List;
import java.util.Map;

import model.goods.Component;
import model.goods.GoodsModel;
import model.goods.ModelComponent;
import pagination.DataTableParam;
import utils.ResultData;

public interface GoodsService {
	ResultData fetch(Map<String, Object> condition);
	
	ResultData fetchModel(Map<String, Object> condition);
	
	ResultData fetchComponent(Map<String, Object> condition);
	
	ResultData fetchComponent(Map<String, Object> condition, DataTableParam param);
	
	ResultData createComponnet(Component component);
	
	ResultData fetchModelDetail(Map<String, Object> condition);
	
	ResultData createModel(GoodsModel model);
	
	ResultData createModelComponentConfig(List<ModelComponent> list);
}
