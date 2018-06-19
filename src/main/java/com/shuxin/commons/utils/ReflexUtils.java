package com.shuxin.commons.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReflexUtils {
	
	/**
	 * 动态调用类或接口的方法
	 * @param obj
	 * @param methodName
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public static  Object methodInvoke(Object obj,String methodName,Object... parameters) throws Exception
	{
		Class[] argsClass = new Class[parameters.length];
		for (int i = 0, j = parameters.length; i < j; i++) 
		{   

	         argsClass[i] = parameters[i].getClass();   

	     }   
		
		Method method = obj.getClass().getMethod(methodName,argsClass);
		return method.invoke(obj,parameters);
	} 
	
	/**
	 * 动态调用类或接口的方法
	 * @param obj
	 * @param methodName
	 * @param paramType
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public static Object methodInvoke(Object obj,String methodName,
			Class[] paramType,Object[] parameters) throws Exception
	{		 
		
		Method method = obj.getClass().getMethod(methodName,paramType);
		return method.invoke(obj,parameters);
	}	
	
	
	/**
	 * 将数据库中数据组装成页面需要的map
	 * @param map
	 * @param list
	 * @param echarsFlag
	 */
	public static void wrapperMap(Map<String, List<String>> map,
			List<Map<String, String>> list,String echarsFlag)
	{
		List<String> valueList = null;
		for(Map<String, String> tempMap : list)
		{
			if(map.containsKey(echarsFlag+"_name"))
			{
				map.get(echarsFlag+"_name").add(tempMap.get("name"));
			}
			else
			{
				valueList = new ArrayList<String>();
				valueList.add(tempMap.get("name"));
				map.put(echarsFlag+"_name", valueList);
			}
			//剔除key为“NAME”后还剩多少key
			int keyCount = tempMap.keySet().size()-1;
			for(int i=1;i<=keyCount;i++)
			{
				if(map.containsKey(echarsFlag+"_value"+i))
				{
					map.get(echarsFlag+"_value"+i).add(tempMap.get("value"+i));
				}
				else
				{
					valueList = new ArrayList<String>();
					valueList.add(tempMap.get("value"+i));
					map.put(echarsFlag+"_value"+i, valueList);
				}
			}
		}
	}
	
	/**
	 * 将数据库中数据组装成页面需要的map
	 * @param map
	 * @param list
	 * @param echarsFlag
	 */
	public static void wrapperMap2(Map<String, List<String>> map,
			List<Map<String, String>> list,String echarsFlag)
	{
		List<String> valueList = null;
		for(Map<String, String> tempMap : list)
		{			
			for(int i=0;i<tempMap.keySet().size();i++)
			{
				if(map.containsKey(echarsFlag+"_value"+i))
				{
					map.get(echarsFlag+"_value"+i).add(tempMap.get("value"+i));
				}
				else
				{
					valueList = new ArrayList<String>();
					valueList.add(tempMap.get("value"+i));
					map.put(echarsFlag+"_value"+i, valueList);
				}
			}
		}
	}

}
