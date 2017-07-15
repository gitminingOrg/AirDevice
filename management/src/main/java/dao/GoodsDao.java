package dao;

import java.util.Map;

import utils.ResultData;

public interface GoodsDao {
	ResultData query(Map<String, Object> condition);
}
