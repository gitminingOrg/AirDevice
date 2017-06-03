package dao;

import java.util.List;
import java.util.Map;

import vo.goods.ConsumerGoodsVo;

public interface GoodsDao {
	List<ConsumerGoodsVo> query4Consumer(Map<String, Object> condition);
}
