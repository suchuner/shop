package com.suchuner.shop.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.suchuner.shop.utils.JsonUtils;
import com.suchuner.shop.web.utils.FastDFSClient;

@Controller
public class UploadController {
	@Value("${SHOP_IMAGE_SERVER_URL}")
	private String SHOP_IMAGE_SERVER_URL;

	@RequestMapping("/pic/upload")
	@ResponseBody
	public String uploadPictrue(MultipartFile uploadFile) {
		/*
		 * 需求:上传文件到文件服务器上,前端技术采用kindEditor[前端上传成功后做回显,所以需要图片服务器URL和返回的图片名称] 步骤:
		 * 1.导入用上传文件的客户端包装好的工具类 2.获取上传文件的扩展名 3.利用工具类取到配置文件的位置,FastDFSClient得到对象
		 * 4.用该对象调用上传文件的方法,注意:此处文件的二进制流文件,返回该图片保存的名称
		 * 5.将名称和服务器的ip返回给调用者,即KingEditor,返回格式有要求
		 * 5.1:由于服务器的ip的不一定不变,所以不能硬编码,采用配置文件的显示读取到这里
		 * 5.2:使用springmvc的属性配置文件读取器,取到该controller中,并使用@Value取值,在它的下方定义一个变量来接收[成员变量]
		 * 5.3:定义kindEditor的返回信息,成功返回URL,失败返回错误信息
		 * 5.4:上传文件的兼容,返回使用String
		 */
		try {

			String filename = uploadFile.getOriginalFilename();
			String extName = filename.substring(filename.lastIndexOf(".") + 1);
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/tracker.conf");
			String path = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
			String url = SHOP_IMAGE_SERVER_URL +path;
			Map result = new HashMap<>();
			result.put("error", 0);
			result.put("url", url);
			return JsonUtils.objectToJson(result);
		} catch (Exception e) {
			e.printStackTrace();
			Map result = new HashMap<>();
			result.put("error", 1);
			result.put("message", "图片上传失败");
			return JsonUtils.objectToJson(result);
		}
	}
}
