package service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.BindDao;
import model.qrcode.PreBindCodeUID;
import service.BindService;
import utils.ResultData;

@Service
public class BindServiceImpl implements BindService {
	private Logger logger = LoggerFactory.getLogger(BindServiceImpl.class);
	
	@Autowired
	private BindDao bindDao;
	
	public ResultData fetchPrebind(Map<String, Object> condition) {
		ResultData result = new ResultData();
		
		return result;
	}

	@Override
	public ResultData createPreBind(PreBindCodeUID pb) {
		ResultData result = new ResultData();
		
		return result;
	}

}
