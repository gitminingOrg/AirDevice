package vip.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import model.ResultMap;

@RestController
@RequestMapping("/goods")
public class GoodsController {
	
	@RequestMapping("/list")
	public ResultMap list() {
		ResultMap result = new ResultMap();
		
		return result;
	}
}
