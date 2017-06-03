package dao;

import java.util.List;
import java.util.Map;

import model.goods.Thumbnail;
import vo.goods.ThumbnailVo;

public interface ThumbnailDao {
	ThumbnailVo insert(Thumbnail thumbnail);
	
	List<ThumbnailVo> query(Map<String, Object> condition);
}
