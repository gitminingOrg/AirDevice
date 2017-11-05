package service;

import java.util.Map;

import model.qrcode.PreBindCodeUID;
import utils.ResultData;

public interface BindService {
	ResultData fetchPrebind(Map<String, Object> condition);
	
	ResultData createPreBind(PreBindCodeUID pb);
}
