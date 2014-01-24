package com.gary.wl.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gary.framework.controller.BaseController;
import com.gary.framework.result.Result;
import com.gary.wl.dto.Config;
import com.gary.wl.service.LoginService;

@RequestMapping("main")
@Controller
public class MainController extends BaseController {
	@Autowired
	private Config config;
	@Autowired
	private LoginService loginService;
	
	@RequestMapping("getConfig")
	@ResponseBody
	private Result getConfig(){
		Result result = new Result();
		result.setSuccess(true);
		result.getData().put("config", config);
		return result;
	}
	
	@RequestMapping("onlines")
	@ResponseBody
	private Result onlines(@RequestParam int type, String value){
		Result result = new Result();
		result.setSuccess(true);
		Map<String, Long> map = loginService.onlineAreaList(type, value);
		result.getData().put("data", map);
		return result;
	}
}
