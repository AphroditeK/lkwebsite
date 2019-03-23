package com.lk.util;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.orm.hibernate5.HibernateTemplate;

public class HibernateTemplateUtil {
	private HibernateTemplate hibernateTemplate;
	public HibernateTemplateUtil(HibernateTemplate hibernateTemplate) {
		super();
		this.hibernateTemplate = hibernateTemplate;
	}
	
	public boolean update(Object object){
		boolean result = false;
		hibernateTemplate.update(object);
		result = true;
		return result;
	}
	
	public boolean save(Object object) {
		boolean result = false;
		hibernateTemplate.save(object);
		result = true;
		return result;
	}
	
	/**
	 * 拼接hql语句，自动赋值查询条件
	 */
	private static <T> String selectStatement(Class<T> className, Map<String, Object> varables) {
		StringBuilder stringBuilder = new StringBuilder();
		/*
		 * 通过className得到该实体类的字符串形式,
		 */
		stringBuilder.append("from " + className.getSimpleName());
		stringBuilder.append(" where 1=1 ");
		/*
		 * 动态的拼接sql语句,如果一个属性的值为"", 则不往条件中添加.
		 * 使用参数动态绑定的方式
		 */
		if(varables!=null){
			for (Entry<String, Object> entry : varables.entrySet()) {
				if (!entry.getValue().equals("")) {
					stringBuilder.append(" and " + entry.getKey() + "=" + entry.getValue());
				}
			}
		}
		return stringBuilder.toString();
	}
	
	
	/**
	 * 单表单记录查询
	 */
	@SuppressWarnings("unchecked")
	public<T> T queryOne(Class<T> clazz,Map<String,Object> values){
		T t = null;
		List<T> list = (List<T>) hibernateTemplate.find(selectStatement(clazz, values));
		if(1 == list.size()){
			t = list.get(0);
		}
		return t;
	}
	
	/**
	 * 单表多记录查询
	 * @param clazz
	 * @param varibles
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public<T> List<T> queryList(Class<T> clazz,Map<String, Object> varibles){
		List<T> list = null;
		list = (List<T>) hibernateTemplate.find(selectStatement(clazz, varibles));
		return list;
	}
	
}
