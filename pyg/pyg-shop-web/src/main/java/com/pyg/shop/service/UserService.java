package com.pyg.shop.service;

import com.pyg.manager.service.SellerService;
import com.pyg.pojo.TbSeller;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Mr.jia
 * @Date: 2018/9/25 21:07
 */
//自定义认证类
public class UserService implements UserDetailsService {

	private SellerService sellerService;

	public SellerService getSellerService() {
		return sellerService;
	}

	public void setSellerService(SellerService sellerService) {
		this.sellerService = sellerService;
	}

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//构建角色
		List<GrantedAuthority> grantedAuthorities=new ArrayList<>();
		grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		//查询数据库的动态密码
		//根据用户名查询数据库对象,获得密码
		TbSeller tbSeller = sellerService.findOne(username);
		if (tbSeller!=null){
			if ("1".equals(tbSeller.getStatus())){
				return new User(username,tbSeller.getPassword(),grantedAuthorities);
			}
		}
		return null;
	}
}
