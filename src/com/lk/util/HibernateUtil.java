package com.lk.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

public class HibernateUtil {
	
	private static SessionFactory sessionFactory = null;

	static{
		// 第一步:读取Hibernate的配置文件 hibernamte.cfg.xml文件
		Configuration con = new Configuration().configure("hibernate.cfg.xml");
		// 第二步:创建会话工厂
		SessionFactory sessionFactory = con.buildSessionFactory();
		HibernateUtil.sessionFactory = sessionFactory;
	}

	/**
	 * 获得Session
	 * @return 获取会话对象
	 */
	public static Session getSession() {
		return sessionFactory.openSession();
	}

	/**
	 * 更新数据或保存数据  必须要有主键
	 * @param 一个对象obj
	 * @return 成功true,失败false
	 */
	public static boolean saveOrUpdate(Object object) {
		Session session = null;
		Transaction tran = null;
		boolean result = false;
		try {
			session = getSession();
			tran = session.beginTransaction();
			session.saveOrUpdate(object);
			tran.commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			if (tran != null) {
				// 事物回滚
				tran.rollback();
			}
			result = false;
		} finally {
			if (session != null) {
				// 关闭session
				session.close();
			}
		}
		return result;
	}

	/**
	 * 查询一条数据根据主键的id号
	 * @param 对应类类
	 * @param 实现序列化的对应类的主键
	 * @return 对应类的对象
	 */
	public static <T> T get(Class<T> c,Serializable obj) {
		Session session = null;
		T t = null;
		try {
			session = getSession();
			t = session.get(c, obj);
		} catch (Exception e) {
		} finally {
			if (session != null) {
				// 关闭session
				session.close();
			}
		}
		return t;
	}

	/**
	 * @param 一个对象obj
	 * @return 成功true，失败false
	 */
	public static boolean delete(Object obj) {
		Session session = null;
		Transaction tran = null;
		boolean result = false;
		try {
			session = getSession();
			tran = session.beginTransaction();
			session.delete(obj);
			tran.commit();
			result = true;
		} catch (Exception e) {
			if (tran != null) {
				// 事物回滚
				tran.rollback();
			}
			result=false;
		} finally {
			if (session != null) {
				// 关闭session
				session.close();
			}
		}
		return result;
	}

	/**
     * 单表多条记录查询
     * @param className 类类
     * @param varables 封装查询条件的map<br/>
     * 例如<br/>
     * Map<String,Object> map = new HashMap<>();<br/>
		map.put("state", 1);<br/>
		return HibernateUtil.queryList(Game.class, map);<br/>
     * @return 成功List集合 失败null
     */
    public static <T> List<T> queryList(Class<T> className, Map<String,Object> varables){        
        List<T> valueList = selectStatement(className, varables).list();
        return valueList;       
    }

	/**
	 * 拼接SQL查询字符串,得到Query并赋值查询条件
	 * 无需了解
	 * 
	 * @param className
	 * @param varables
	 * @param session
	 * @return Query
	 */
	private static <T> Query<T> selectStatement(Class<T> className, Map<String, Object> varables) {
		StringBuilder stringBuilder = new StringBuilder();
		/*
		 * 通过className得到该实体类的字符串形式,
		 */
		stringBuilder.append("from " + className.getSimpleName());
		stringBuilder.append(" where 1=1 ");
		/*
		 * 动态的拼接sql语句,如果一个属性的值为"", 则不往条件中添加.
		 */
		if(varables!=null){
			for (Entry<String, Object> entry : varables.entrySet()) {
				if (!entry.getValue().equals("")) {
					stringBuilder.append(" and " + entry.getKey() + "=:" + entry.getKey());
				}
			}
		}
		@SuppressWarnings("unchecked")
		Query<T> query = getSession().createQuery(stringBuilder.toString());
		/*
		 * 动态的给条件赋值
		 */
		if(varables!=null){
			for (Entry<String, Object> entry : varables.entrySet()) {
				if (!entry.getValue().equals("")) {
					query.setParameter(entry.getKey(), entry.getValue());
				}
			}
		}
		return query;
	}

	/**
	 * 单表单条记录查询
	 * 
	 * @param clazz
	 * @param varables
	 * 例如<br/>
     * Map<String, Object> map = new HashMap<String, Object>();<br/>
		map.put("username", admin.getUsername());<br/>
		map.put("password", admin.getPassword());<br/>
		if (null != HibernateUtil.queryOne(User.class, map)) {<br/>
			return true;<br/>
		}<br/>
		return false;<br/>
	 * @return 成功一个对象  失败一个null
	 */
	public static <T> T queryOne(Class<T> clazz, Map<String, Object> varables) {
		return (T) selectStatement(clazz, varables).uniqueResult();
	}

	/**
	 * @param hql语句
	 * @param page从多少页开始
	 * @param size每页多少数量
	 * @return List数据
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> queryByPage(Class<T> clazz, Map<String, Object> varables,  int page, int size) {
		List<T> list = new ArrayList<T>();
		Session session = null;
		try {
			session = getSession();
			StringBuilder stringBuilder = new StringBuilder();
			/*
			 * 通过className得到该实体类的字符串形式,
			 */
			stringBuilder.append("from " + clazz.getSimpleName());
			stringBuilder.append(" where 1=1 ");
			/*
			 * 动态的拼接sql语句,如果一个属性的值为"", 则不往条件中添加.
			 */
			if(varables!=null){
				for (Entry<String, Object> entry : varables.entrySet()) {
					if (!entry.getValue().equals("")) {
						stringBuilder.append(" and " + entry.getKey() + "=:" + entry.getKey());
					}
				}
			}
			@SuppressWarnings("unchecked")
			Query<T> query = session.createQuery(stringBuilder.toString());
			/*
			 * 动态的给条件赋值
			 */
			if(varables!=null){
				for (Entry<String, Object> entry : varables.entrySet()) {
					if (!entry.getValue().equals("")) {
						query.setParameter(entry.getKey(), entry.getValue());
					}
				}
			}
			// 筛选条数
			query.setFirstResult((page - 1) * size);
			query.setMaxResults(size);
			list = query.getResultList();
		} catch (Exception e) {
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return list;
	}
	
	/**
	 * @param clas对应的类类
	 * @return 该表的具体数目
	 */
	public static<T> Long getCount(Class<T> clazz,Map<String,Object> varables){
		Session session = null;
		Long result =null;
		try {
			session = getSession();
			
			StringBuilder stringBuilder = new StringBuilder();
			/*
			 * 通过className得到该实体类的字符串形式,
			 */
			stringBuilder.append("select count(1) from " + clazz.getSimpleName());
			stringBuilder.append(" where 1=1 ");
			/*
			 * 动态的拼接sql语句,如果一个属性的值为"", 则不往条件中添加.
			 */
			if(varables!=null){
				for (Entry<String, Object> entry : varables.entrySet()) {
					if (!entry.getValue().equals("")) {
						stringBuilder.append(" and " + entry.getKey() + "=:" + entry.getKey());
					}
				}
			}
			@SuppressWarnings("unchecked")
			Query<T> query = session.createQuery(stringBuilder.toString());
			/*
			 * 动态的给条件赋值
			 */
			if(varables!=null){
				for (Entry<String, Object> entry : varables.entrySet()) {
					if (!entry.getValue().equals("")) {
						query.setParameter(entry.getKey(), entry.getValue());
					}
				}
			}
			// 筛选条数
			result = (Long) query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return result;
	}
}