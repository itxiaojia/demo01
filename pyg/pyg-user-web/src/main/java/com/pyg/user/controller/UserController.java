package com.pyg.user.controller;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pyg.service.user.UserService;
import com.pyg.utils.PhoneFormatCheckUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.pojo.TbUser;
import com.pyg.service.user.UserService;

import com.pyg.utils.PageResult;
import com.pyg.utils.PygResult;
/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Reference(timeout = 30000)
	private UserService userService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbUser> findAll(){			
		return userService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage/{page}/{rows}")
	public PageResult  findPage(@PathVariable int page,@PathVariable int rows){			
		return userService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param user
	 * @return
	 */
	@RequestMapping("/add")
	public PygResult add(@RequestBody TbUser user,String code){
		try {
			//校验手机号是否正确
			if (!PhoneFormatCheckUtils.isChinaPhoneLegal(user.getPhone())){
				throw new RuntimeException("手机号不正确");
			}
			//校验手机号对应的验证码是否正确
			if (!userService.checkCode(user.getPhone(),code)){
				throw new RuntimeException("验证码错误");
			}
			//MD5加密
			String password = DigestUtils.md5Hex(user.getPassword());
			user.setPassword(password);
			user.setCreated(new Date());
			user.setUpdated(new Date());
			//设置状态
			user.setStatus("Y");

			userService.add(user);
			return new PygResult(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new PygResult(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param user
	 * @return
	 */
	@RequestMapping("/update")
	public PygResult update(@RequestBody TbUser user){
		try {
			userService.update(user);
			return new PygResult(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new PygResult(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne/{id}")
	public TbUser findOne(@PathVariable Long id){
		return userService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete/{ids}")
	public PygResult delete(@PathVariable Long [] ids){
		try {
			userService.delete(ids);
			return new PygResult(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new PygResult(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbUser user, int page, int rows  ){
		return userService.findPage(user, page, rows);		
	}

	@RequestMapping("/sendSms")
	public PygResult sendSms(String phone){
		try {
			userService.sendSms(phone);
			//添加成功
			return new PygResult(true,"验证码发送成功");
		} catch (Exception e) {
			e.printStackTrace();
			//添加成功
			return new PygResult(false,"验证码发送失败");
		}

	}

	@RequestMapping("/findLoginUser")
	public Map findLoginUser(){
		String loginName = SecurityContextHolder.getContext().getAuthentication().getName();
		Map map = new HashMap();
		map.put("loginName",loginName );

		return map;
	}
}
