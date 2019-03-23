package com.lk.action;


import com.lk.entity.User;
import com.lk.service.UserService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class UserAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String password;
	public void setName(String name) {
		this.name = name;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	private UserService userService;
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String regist(){
		User user = new User();
		user.setAccount(name);
		user.setPassword(password);
		if(userService.regist(user)){
			ActionContext context = ActionContext.getContext();
			context.getSession().put("user", user);
			return "success";
		}	
		return "fail";
	}
	
}
