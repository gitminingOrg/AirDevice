package vip.service;

import model.consumer.ConsumerShareCode;
import utils.ResultData;

public interface ShareCodeService {
	ResultData customizeShareCode(String path, String value);
	
	ResultData refreshCodeBG(ConsumerShareCode code);
}
