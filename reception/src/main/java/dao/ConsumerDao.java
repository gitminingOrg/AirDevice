package dao;

import java.util.List;
import java.util.Map;

import model.vip.Consumer;
import vo.vip.ConsumerVo;

public interface ConsumerDao {
	List<ConsumerVo> query(Map<String, Object> condition);
	
	boolean insert(Consumer consumer);
}
