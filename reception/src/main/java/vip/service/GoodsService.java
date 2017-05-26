package vip.service;

import java.util.List;
import java.util.Map;

public interface GoodsService {
	List fetchGoodsList(Map<String, Object> condition);
}
