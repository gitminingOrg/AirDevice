package controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;

import form.ComponentForm;
import model.goods.Component;
import pagination.DataTablePage;
import pagination.DataTableParam;
import service.GoodsService;
import utils.ResponseCode;
import utils.ResultData;
import vo.goods.GoodsComponentVo;

@RestController
@RequestMapping("/component")
public class ComponentController {
	private Logger logger = LoggerFactory.getLogger(ComponentController.class);

	@Autowired
	private GoodsService goodsService;
	
	@RequestMapping(method = RequestMethod.GET, value = "/overview")
	public ModelAndView overview() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/backend/component/overview");
		return view;
	}
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value="/list")
	public DataTablePage<GoodsComponentVo> list(DataTableParam param) {
		DataTablePage<GoodsComponentVo> result = new DataTablePage<>(param);
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        ResultData response = goodsService.fetchComponent(condition, param);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (DataTablePage<GoodsComponentVo>) response.getData();
        }
        return result;
	}
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value="/create")
	public ResultData create(@Valid ComponentForm form, BindingResult br) {
		ResultData result = new ResultData();
		if(br.hasErrors()) {
			logger.error(JSON.toJSONString(br.getAllErrors()));
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription("Form parameter error");
			return result;
		}
		Component component = new Component(form.getName());
		ResultData response = goodsService.createComponnet(component);
		if(response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		}else {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription("机器组件" + form.getName() + "创建失败");
		}
		return result;
	}
}
