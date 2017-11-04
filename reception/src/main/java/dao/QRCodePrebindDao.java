package dao;

import java.util.Map;

import utils.ResultData;

public interface QRCodePrebindDao {
	ResultData query(Map<String, Object> condition);
}
