package com.suchuner.shop.search.exceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.suchuner.shop.search.mail.MailUitl;

public class SearchExceptionReslover implements HandlerExceptionResolver {
	@Autowired
	private MailUitl mailUitl;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Value("${EMAIL_FROM}")
	private String EMAIL_FROM ;
	@Value("${EMAIL_TO}")
	private String EMAIL_TO ;
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
			/*需求:全局的异常处理器
			 * 1.获取异常信息,
			 * 2.发送邮件,或者发送短信给开发人员
			 * 3.日志文件的输出
			 * 4.返回自定义的错误信息和页面
			 * */
		System.out.println("发送邮件给开发者~~~");
		mailUitl.sendMail("系统发生了异常", ex.getMessage(), EMAIL_FROM, EMAIL_TO);
		System.out.println("发送短信给开发者~~~");
		logger.info("系统异常错误信息开始处.............");
		logger.error("系统发生异常",ex);
		logger.info("系统异常错误信息结束处.............");
		return new ModelAndView("error/exception","message","亲,你的网络有问题哦,检查后重试");
	}
	
}
