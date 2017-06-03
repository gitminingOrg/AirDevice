package vip.service.impl;

import java.util.List;
import java.util.Map;

import javax.json.Json;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import dao.GoodsDao;
import dao.ThumbnailDao;
import model.goods.ThumbnailType;
import vip.service.GoodsService;
import vo.goods.ConsumerGoodsVo;
import vo.goods.ThumbnailVo;

@Service
public class GoodsServiceImpl implements GoodsService {
	private Logger logger = LoggerFactory.getLogger(GoodsServiceImpl.class);

	@Autowired
	private GoodsDao goodsDao;

	@Autowired
	private ThumbnailDao thumbnailDao;

	@Override
	public List<ConsumerGoodsVo> fetchGoodsList4Consumer(Map<String, Object> condition) {
		List<ConsumerGoodsVo> result = goodsDao.query4Consumer(condition);
		if (!StringUtils.isEmpty(result)) {
			return result;
		} else {
			logger.debug("No goods found with condition: " + condition.toString());
			return null;
		}
	}

	@Override
	public ThumbnailVo fetchCover4Goods(Map<String, Object> condition) {
		condition.put("type", ThumbnailType.COVER.getCode());
		List<ThumbnailVo> result = thumbnailDao.query(condition);
		if (result.isEmpty()) {
			return null;
		}
		ThumbnailVo cover = result.get(0);
		return cover;
	}

	@Override
	public List<ThumbnailVo> fetchThumbnails4Goods(Map<String, Object> condition) {
		condition.put("type", ThumbnailType.ORDINARY.getCode());
		List<ThumbnailVo> result = thumbnailDao.query(condition);
		if (result.isEmpty()) {
			return null;
		}
		return result;
	}
}
