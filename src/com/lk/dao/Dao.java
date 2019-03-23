package com.lk.dao;

import com.lk.entity.User;

public interface Dao {
	
	public boolean add(User user);
	
	public User query(String account);
	
	public boolean update(User user);
	
}
