package vip.service;

import java.util.List;
import java.util.Map;

import vo.goods.ConsumerGoodsVo;
import vo.goods.ThumbnailVo;

public interface GoodsService {
	List<ConsumerGoodsVo> fetchGoodsList4Consumer(Map<String, Object> condition);
	
	ThumbnailVo fetchCover4Goods(Map<String, Object> condition);
	
	List<ThumbnailVo> fetchThumbnails4Goods(Map<String, Object> condition);
}
