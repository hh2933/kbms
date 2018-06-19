package com.shuxin.commons.utils;


import java.nio.charset.Charset;

import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * 字符集工具类
 * Author: L.cm
 * Date: 2016年3月29日 下午3:44:52
 */
public class Charsets {

    // 字符集ISO-8859-1
    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    // 字符集GBK
    public static final Charset GBK = Charset.forName("GBK");
    // 字符集utf-8
    public static final Charset UTF_8 = Charset.forName("UTF-8");
        
    /**
     * 将中文转换成拼音首字母
     * @param str
     * @return
     */
    public static  String getPinYinHeadChar(String str)
    {
    	if(StringUtils.isEmpty(str))
    	{
    		return "";
    	}
    	StringBuffer convert = new StringBuffer(); 
    	for (int j = 0; j < str.length(); j++) 
    	{  
            char word = str.charAt(j);  
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);  
            if (pinyinArray != null) 
            {  
                convert.append(pinyinArray[0].charAt(0));  
            }
            else
            {  
            	convert.append(word);  
            }  
        } 
       return convert.toString();
    }
    
    /**
     * 过滤特殊字符
     * @param str
     * @return
     */
    public static String filterSpecialChar(String str)
    {
    	String newStr="";
    	if(StringUtils.isEmpty(str))
    	{
    		return newStr;
    	}
    	
    	newStr=str.replace("'", " ");
    	
    	return newStr;
    }
    
    /**
     * 去掉多余字符
     * @param str
     * @return
     */
    public static String dropSurplusChar(String str)
    {
    	String newStr="";
    	if(StringUtils.isEmpty(str))
    	{
    		return newStr;
    	}
    	
    	newStr=str.replace(",", "");
    	return newStr.trim();
    }
    

}
