package dao;

import java.util.Map;

import model.goods.Component;
import pagination.DataTableParam;
import utils.ResultData;

public interface ComponentDao {
	ResultData query(Map<String, Object> condition);
	
	ResultData query(Map<String, Object> condition, DataTableParam param);
	
	ResultData insert(Component component);
}
