package com.lk.service;

import org.springframework.transaction.annotation.Transactional;

import com.lk.dao.Dao;
import com.lk.entity.User;

@Transactional
public class UserService {
	
	private Dao dao;
	public void setDao(Dao dao) {
		this.dao = dao;
	}
	
	public boolean regist(User user){
		if(null != user.getAccount() && null != user.getPassword()){
			if(null == dao.query(user.getAccount())){
				dao.add(user);
				return true;
			}
		}
		return false;
	}
	
	public boolean login(User user){
		
		
		return false;
	}
	

}
