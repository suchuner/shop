package com.suchuner.shop.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suchuner.shop.common.TaotaoResult;
import com.suchuner.shop.domain.TbUser;
import com.suchuner.shop.sso.service.IUserService;
import com.suchuner.shop.utils.CookieUtils;

@Controller
public class UserController {
	@Autowired
	private IUserService userService;

	@Value("${COOKIE_TOKEN_KEY}")
	private String COOKIE_TOKEN_KEY;
	
	@RequestMapping(value = "/user/check/{param}/{type}", method = RequestMethod.GET)
	@ResponseBody
	public TaotaoResult checkData(@PathVariable(value="param") String param, @PathVariable Integer type) {
		return userService.checkData(param, type);
	}

	@RequestMapping(value = "/user/register", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult register(TbUser user) {
		return userService.register(user);
	}

	@RequestMapping(value = "/user/login", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult login(HttpServletRequest request, HttpServletResponse response, TbUser user) {
		TaotaoResult result = userService.login(user);
		if (result.getData().toString() == null && result.getData().toString().trim().equals("")) {
			return result;
		}
		String token = result.getData().toString();
		CookieUtils.setCookie(request, response, COOKIE_TOKEN_KEY, token);
		return result;
	}
	@RequestMapping(value = "/user/token/{token}", method = RequestMethod.GET,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public Object getUserByToken(@PathVariable String token,String callback) {
		TaotaoResult result = userService.getUserByToken(token);
		if(callback!=null&&!callback.trim().equals("")){
			MappingJacksonValue value = new MappingJacksonValue(result);
			value.setJsonpFunction(callback);
			return value;
		}
		return result;
	}
	@RequestMapping(value = "/user/logout/{token}", method = RequestMethod.GET)
	@ResponseBody
	public TaotaoResult logout(@PathVariable String token) {
		TaotaoResult result = userService.logout(token);
		return result;
	}
}
