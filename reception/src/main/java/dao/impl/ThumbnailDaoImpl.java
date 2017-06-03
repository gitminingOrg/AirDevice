package dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.BaseDaoImpl;
import dao.ThumbnailDao;
import model.goods.Thumbnail;
import utils.IDGenerator;
import vo.goods.ThumbnailVo;

public class ThumbnailDaoImpl extends BaseDaoImpl implements ThumbnailDao {
	private Logger logger = LoggerFactory.getLogger(ThumbnailDaoImpl.class);

	private Object lock = new Object();

	@Override
	public ThumbnailVo insert(Thumbnail thumbnail) {
		thumbnail.setThumbnailId(IDGenerator.generate("THU"));
		synchronized (lock) {
			try {
				
			}catch (Exception e) {
				logger.error(e.getMessage());
				return null;
			}
		}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ThumbnailVo> query(Map<String, Object> condition) {
		List<ThumbnailVo> result = new ArrayList<>();
		try {
			result = sqlSession.selectList("thumbnail.select", condition);
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}

}
