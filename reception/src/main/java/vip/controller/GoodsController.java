package vip.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import form.GoodsForm;
import model.ResultMap;
import model.goods.Thumbnail;
import vip.service.GoodsService;
import vo.goods.ConsumerGoodsVo;
import vo.goods.ThumbnailVo;

@RestController
@RequestMapping("/goods")
public class GoodsController {
	private Logger logger = LoggerFactory.getLogger(GoodsController.class);

	@Autowired
	private GoodsService goodsService;

	@RequestMapping("/consumer/list")
	public ResultMap list() {
		ResultMap result = new ResultMap();
		Map<String, Object> condition = new HashMap<>();
		List<ConsumerGoodsVo> list = goodsService.fetchGoodsList4Consumer(condition);
		if (list.isEmpty()) {
			result.setStatus(ResultMap.STATUS_FAILURE);
			result.setInfo("Empty list");
		} else {
			result.setStatus(ResultMap.STATUS_SUCCESS);
			result.addContent("goodsList", list);
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{goodsId}/cover")
	public ResultMap cover(@PathVariable("goodsId") String goodsId) {
		ResultMap result = new ResultMap();
		Map<String, Object> condition = new HashMap<>();
		condition.put("goodsId", goodsId);
		condition.put("blockFlag", false);
		ThumbnailVo cover = goodsService.fetchCover4Goods(condition);
		if(StringUtils.isEmpty(cover)) {
			logger.info("No cover found for goods with goods id: " + goodsId);
			result.setStatus(ResultMap.STATUS_FAILURE);
			result.setInfo("No cover found for goods with goods id: " + goodsId);
			return result;
		}
		result.setStatus(ResultMap.STATUS_SUCCESS);
		result.addContent("cover", cover);
		return result;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{goodsId}/thumbnail")
	public ResultMap thumbnail(@PathVariable("goodsId") String goodsId) {
		ResultMap result = new ResultMap();
		Map<String, Object> condition = new HashMap<>();
		condition.put("goodsId",goodsId);
		condition.put("blockFlag", false);
		List<ThumbnailVo> list = goodsService.fetchThumbnails4Goods(condition);
		if(list.isEmpty()) {
			logger.info("No thumbnails found for goods with goods id: " + goodsId);
			result.setStatus(ResultMap.STATUS_FAILURE);
			result.setInfo("No cover found for goods with goods id: " + goodsId);
			return result;
		}
		result.setStatus(ResultMap.STATUS_SUCCESS);
		result.addContent("thumbnails", list);
		return result;
	}

	@RequiresAuthentication
	@RequestMapping(method = RequestMethod.GET, value = "/create")
	public ResultMap create(GoodsForm form) {
		ResultMap result = new ResultMap();
		Map<String, Object> condition = new HashMap<>();
		condition.put("blockFlag", false);
		condition.put("thumbnailId", form.getGoodsCover());
		Thumbnail cover = new Thumbnail();
		return result;
	}
}
