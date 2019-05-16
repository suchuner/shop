package com.suchuner.shop.sso.service;

import com.suchuner.shop.common.TaotaoResult;
import com.suchuner.shop.domain.TbUser;

public interface IUserService {
	/**参数数据的合法检查方法
	 * @param param 参数的数据
	 * @param type 1 代表username 2 代表phone 3 代表email 其他的都非法
	 * @return true表示数据可用 ,false表示数据不可用
	 */
	public TaotaoResult checkData(String param,Integer type);
	
	/**用户注册
	 * @param user
	 * @return
	 */
	public TaotaoResult register(TbUser user);
	
	/**实现用户的登录
	 * @param user 前端传递的数据需要包含名称和密码:做后台校验
	 * @return
	 */
	public TaotaoResult login(TbUser user);
	
	/**实现用户的安全退出,删除保存的登录口令即可
	 * @param token
	 * @return
	 */
	public TaotaoResult logout(String token);

	/**根据登录的口令查询登录的用户
	 * @param token
	 * @return
	 */
	public TaotaoResult getUserByToken(String token);

}
