package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.mail.imap.Utility.Condition;
import com.sun.org.apache.bcel.internal.generic.ReturnaddressType;

import form.ModelCreateForm;
import model.goods.GoodsModel;
import model.goods.ModelComponent;
import service.GoodsService;
import utils.ResponseCode;
import utils.ResultData;

@RestController
@RequestMapping("/model")
public class ModelController {
	private Logger logger = LoggerFactory.getLogger(ModelController.class);

	@Autowired
	private GoodsService goodsService;

	@RequestMapping(method = RequestMethod.GET, value = "/overview")
	public ModelAndView create() {
		ModelAndView view = new ModelAndView();
		Map<String, Object> condition = new HashMap<>();
		ResultData response = goodsService.fetchComponent(condition);
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			view.addObject("component", response.getData());
		}
		view.setViewName("/backend/model/overview");
		return view;
	}

	@ResponseBody
	public ResultData modelAvailable() {
		ResultData result = new ResultData();

		return result;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{modelId}")
	public ModelAndView modelDetail(@PathVariable("modelId") String modelId) {
		ModelAndView view = new ModelAndView();
		if (StringUtils.isEmpty(modelId)) {
			logger.error("parameter model id cannot be empty");
			view.setViewName("redirect:/model/overview");
			return view;
		}
		Map<String, Object> condition = new HashMap<>();
		condition.put("modelId", modelId);
		ResultData response = goodsService.fetchModelDetail(condition);
		if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
			logger.error("query model detail with model id: " + modelId + " failure");
			view.setViewName("redirect:/model/overview");
			return view;
		}
		if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
			logger.error("不存在型号: " + modelId);
			view.setViewName("redirect:/model/overview");
			return view;
		}
		view.addObject("detail", ((List) response.getData()).get(0));
		view.setViewName("/backend/model/detail");
		return view;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/{goodsId}/list")
	public ResultData modelofGoods(@PathVariable("goodsId") String goodsId) {
		ResultData result = new ResultData();
		if(StringUtils.isEmpty(goodsId)) {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription("the parameter goods id cannot be empty");
			return result;
		}
		Map<String, Object> condition = new HashMap<>();
		condition.put("goodsId", goodsId);
		ResultData response = goodsService.fetchModel(condition);
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		} else {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription("No model available for goods with goods id: " + goodsId);
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/create")
	public ResultData create(@Valid ModelCreateForm form, BindingResult br) {
		ResultData result = new ResultData();
		if (br.hasErrors()) {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			logger.error(JSON.toJSONString(br.getAllErrors()));
			result.setDescription("表单数据格式不正确");
			return result;
		}
		GoodsModel gm = new GoodsModel(form.getGoodsId(), form.getModelCode(), form.getModelName());
		ResultData response = goodsService.createModel(gm);
		if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
			logger.error(response.getDescription());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(response.getDescription());
			return result;
		}
		gm = (GoodsModel) response.getData();
		JSONObject param = JSONObject.parseObject(form.getParam());
		Set<String> it = param.keySet();
		if (!it.isEmpty()) {
			List<ModelComponent> list = new LinkedList<>();
			for (String key : it) {
				String componentId = key;
				String supplierName = param.getString(key);
				ModelComponent mc = new ModelComponent(gm, componentId, supplierName);
				list.add(mc);
			}
			response = goodsService.createModelComponentConfig(list);
			if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
				logger.info("创建型号: " + form.getModelCode() + "成功");
			}
		}
		return result;
	}
}
