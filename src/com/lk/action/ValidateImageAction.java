package com.lk.action;

import java.io.InputStream;
import java.util.Map;

import com.lk.util.ValidateImg;
import com.opensymphony.xwork2.ActionSupport;

public class ValidateImageAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private InputStream input;
	public InputStream getInput() {
		return input;
	}
	public void setInput(InputStream input) {
		this.input = input;
	}

	public String execute(){
		Map<String, Object> map = new ValidateImg().createImg();
		input = (InputStream)map.get("image");
		String code = (String)map.get("code");
		System.out.println(code);
		return "success";
	}
}
