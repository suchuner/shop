package com.suchuner.shop.sso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.suchuner.shop.common.TaotaoResult;
import com.suchuner.shop.domain.TbUser;
import com.suchuner.shop.domain.TbUserExample;
import com.suchuner.shop.domain.TbUserExample.Criteria;
import com.suchuner.shop.mapper.TbUserMapper;
import com.suchuner.shop.sso.service.IUserService;
import com.suchuner.shop.sso.service.jedis.JedisClient;
import com.suchuner.shop.utils.JsonUtils;

@Service
public class UserService implements IUserService {

	@Autowired
	private TbUserMapper userMapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${USER_INFO}")
	private String USER_INFO;
	
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;

	public TaotaoResult checkData(String param, Integer type) {
		/*实现步骤:根据查询的条件设置查询条件,最后发布服务*/
		TbUserExample example=null;
		example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		switch (type) {
		case 1:
			criteria.andUsernameEqualTo(param);
			break;
		case 2:
			criteria.andPhoneEqualTo(param);
			break;
		case 3:
			criteria.andEmailEqualTo(param);
			break;
		default:
			return TaotaoResult.build(400,"非法参数!!!");
		}
		List<TbUser> list  = userMapper.selectByExample(example );
		return list!=null&&list.size()>0?TaotaoResult.ok(false):TaotaoResult.ok(true);
	}

	public TaotaoResult register(TbUser user) {
		/*需求:实现用户的注册功能
		 * 实现步骤:
		 * 1.实现数据的校验
		 * 2.数据校验完成后进行数据插入
		 * 3.返回是否成功
		 * 4.发布服务
		 * */
		if(user!=null){
			if(user.getUsername()!=null&&!user.getUsername().trim().equals("")){
				TaotaoResult result = checkData(user.getUsername(), 1);
				if(!(boolean)result.getData()){
					return TaotaoResult.build(400, "该用户名已被注册!");
				}
			}
			if(user.getPhone()!=null&&!user.getPhone().trim().equals("")){
				TaotaoResult result = checkData(user.getPhone(), 2);
				if(!(boolean)result.getData()){
					return TaotaoResult.build(400, "该手机号已被占用!");
				}
			}
			if(user.getEmail()!=null&&!user.getEmail().trim().equals("")){
				TaotaoResult result = checkData(user.getEmail(), 3);
				if(!(boolean)result.getData()){
					return TaotaoResult.build(400, "该邮箱已被占用!");
				}
			}
			if(user.getPassword()==null||user.getPassword().trim().equals("")){
					return TaotaoResult.build(400, "密码不能为空!");
			}
			user.setCreated(new Date());
			user.setUpdated(new Date());
			//设置用户密码的加密
			String password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
			user.setPassword(password);
			userMapper.insertSelective(user);
			return TaotaoResult.ok();
		}
		return  TaotaoResult.build(400, "注册失败,请校验数据后重试!");
	}

	public TaotaoResult login(TbUser user) {
		/*需求:实现系统的单点登录的功能
		 * 实现步骤:
		 * 1.校验登录的数据
		 * 2.登录成功后,生成登录用户对应的口令
		 * 3.将生成的口令和对象的数据存入到Redis数据库中保存,并且设置口令和数据的有效期为半个小时
		 * 4.将口令返回给controller层,用于设置到cookie中去
		 * 5.发布服务
		 * */
		if(user==null||user.getUsername().trim().equals("")){
			return TaotaoResult.build(400, "请输入用户名!");
		}
		String password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		TbUserExample example =new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(user.getUsername());
		criteria.andPasswordEqualTo(password);
		List<TbUser> list = userMapper.selectByExample(example );
		if(list==null&&list.size()==0){
			return TaotaoResult.build(400, "用户名或密码错误!");
		}
		String token  = UUID.randomUUID().toString();
		TbUser loginUser = list.get(0);
		loginUser.setPassword(null);
		jedisClient.set(USER_INFO+":"+token, JsonUtils.objectToJson(loginUser));
		jedisClient.expire(USER_INFO+":"+token, SESSION_EXPIRE);
		return TaotaoResult.ok(token);
	}

	public TaotaoResult getUserByToken(String token) {
		String JsonStr = jedisClient.get(USER_INFO+":"+token);
		if(JsonStr!=null&&!JsonStr.trim().equals("")){
		TbUser loginUser = JsonUtils.jsonToPojo(JsonStr, TbUser.class);
		jedisClient.expire(USER_INFO+":"+token, SESSION_EXPIRE);
		return TaotaoResult.ok(loginUser);
		}
		return TaotaoResult.build(400, "用户身份已过期,请重新登录!");
	}

	public TaotaoResult logout(String token) {
		jedisClient.expire(USER_INFO+":"+token, 0);
		return TaotaoResult.build(200, "已安全退出!");
	}

}
