package dao;

import java.util.Map;

import pagination.DataTableParam;
import utils.ResultData;

public interface ShareCodeDao {
	ResultData query(Map<String, Object> condition);
	
	ResultData query(Map<String, Object> condition, DataTableParam param);
}
