package com.suchuner.shop.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.suchuner.shop.common.TaotaoResult;
import com.suchuner.shop.sso.service.IUserService;
import com.suchuner.shop.utils.CookieUtils;

public class LoginInterceptor implements HandlerInterceptor {
	@Value("${COOKIE_TOKEN_KEY}")
	private String COOKIE_TOKEN_KEY;
	
	@Value("${SSO_URL}")
	private String SSO_URL;
	
	@Autowired
	private IUserService userService;

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {

	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String token = CookieUtils.getCookieValue(request, COOKIE_TOKEN_KEY);
		TaotaoResult result = userService.getUserByToken(token);
		if(result.getStatus()!=200){
			String url =SSO_URL+"/page/login?url="+request.getRequestURL() ;
			response.sendRedirect(url);
			return false;
		}
			request.setAttribute("loginUser", result.getData());
			return true;
	}
}
