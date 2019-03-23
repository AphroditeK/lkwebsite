package com.lk.dao;


import java.util.List;

import org.springframework.orm.hibernate5.HibernateTemplate;

import com.lk.entity.User;


public class UserDao implements Dao{
	
	private HibernateTemplate hibernateTemplate;
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	/**
	 * 保存用户
	 */
	public boolean add(User user){
		try {
			hibernateTemplate.save(user);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public User query(String account){
		User user = null;
		List<User> list = 
				(List<User>)hibernateTemplate.find("from User where account=?", account);
		if(0 == list.size()){
			return null;
		}
		user = list.get(0);
		return user;
	}
	
	public boolean update(User user){
		hibernateTemplate.update(user);
		return true;
	}

	
}
