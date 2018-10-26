package com.seed.utility;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConnectionUtil 
{
	public static Connection getConnection()
	{
		Connection con = null;
		
		try(FileInputStream fin = new FileInputStream("oracle.properties")) 
		{
			Properties p = new Properties();
			p.load(fin);
			
			con = DriverManager.getConnection(p.getProperty("url"), p.getProperty("username"), p.getProperty("password"));
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return con;
	}
}
