package com.shuxin.commons.utils;

import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.ResultSet;  
import java.sql.SQLException;  
import java.sql.Statement;


import com.shuxin.commons.utils.PropertiesLoader;  
 /**
  * 
  * @author admin
  *
  */

public class JDBCUtils {  
    private static    String driver ;
	private static    String url ; 
    private static    String user; 
    private static    String password;  
	
	
    static {
    	
    	PropertiesLoader  propertiesLoader = new PropertiesLoader("application.properties");
    	driver = propertiesLoader.getProperty("db.master.driverClassName");
    	url = propertiesLoader.getProperty("db.master.url");
    	user = propertiesLoader.getProperty("db.master.user1");
    	password = propertiesLoader.getProperty("db.master.password1");
    	
    }
    
   
    
    //注册数据库驱动  
    static{  
        try {  
            Class.forName(driver);  
        } catch (Exception e) {  
            throw new ExceptionInInitializerError(e);  
        }  
    }  
      
    /** 
     * 获取数据库连接 
     * @return 
     */  
    public static Connection getConnection(){  
        try {  
            return DriverManager.getConnection(url,user,password);  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
      
    /** 
     * 释放数据库连接资源 
     * @param conn 
     * @param st 
     * @param rs 
     */  
    public static void release(Connection conn,Statement st,ResultSet rs){  
        if (rs!=null) {  
            try {  
                rs.close();  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }finally{  
                rs = null;  
            }  
        }  
          
        if (st!=null) {  
            try {  
                st.close();  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }finally{  
                st = null;  
            }  
        }  
          
        if (conn!=null) {  
            try {  
                conn.close();  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }finally{  
                conn = null;  
            }  
        }  
    }

    
    
    
}  
