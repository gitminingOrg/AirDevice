package dao;

import java.util.Map;

import model.qrcode.PreBindCodeUID;
import utils.ResultData;

public interface BindDao {
	ResultData query(Map<String, Object> condition);
	
	ResultData insert(PreBindCodeUID pb);
}
