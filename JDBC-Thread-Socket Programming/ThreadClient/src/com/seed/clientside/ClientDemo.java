package com.seed.clientside;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.seed.model.Employee;
import com.seed.utility.ConnectionUtil;

public class ClientDemo implements Runnable
{

	 List<Employee> empList = new ArrayList<>();
	 ObjectOutputStream oos = null; 
	 Socket s = null;
	
	public synchronized void sendListToServer() throws UnknownHostException, IOException
	{
		if(oos == null)
		{
			try 
			{
				wait();
			}
			catch (InterruptedException e) 
			{
			
				e.printStackTrace();
			}
		}
		System.out.println("Connected to Server");
		oos = new ObjectOutputStream(s.getOutputStream());
		oos.writeObject(empList);
		s.close();
		System.out.println("Data Transfered to Server");
	}
	
	public synchronized void retriveEmployeeData(Connection con) throws SQLException
	{
		PreparedStatement pst = con.prepareStatement("select * from Employee");
		
		ResultSet rst = pst.executeQuery();
		
		while(rst.next())
		{
			empList.add(new Employee(rst.getInt(1),rst.getString(2),rst.getFloat(3)));
		}
		
		notify();
	}
	
	public synchronized void connectToServer() throws UnknownHostException, IOException
	{
		s = new Socket(InetAddress.getLocalHost(), 5000);
		System.out.println("Client Started ..");
	}
	
	public static void main(String[] args) 
	{
		ClientDemo obj = new ClientDemo();
		
		Thread t1 = new Thread(obj, "databaseRetrive");
		Thread t2 = new Thread(obj, "connectingServer");
		Thread t3 = new Thread(obj,"dataTransfer");
		
		t1.start();
		t2.start();
		t3.start();
	}

	@Override
	public void run() 
	{
		if(Thread.currentThread().getName().equals("databaseRetrive"))
		{
			Connection con = ConnectionUtil.getConnection();
			try 
			{
					retriveEmployeeData(con);
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		
		
		if(Thread.currentThread().getName().equals("connectingServer"))
		{
			try
			{
				connectToServer();
			}
			catch (UnknownHostException e) 
			{		
				e.printStackTrace();
			}
			catch (IOException e) 
			{			
				e.printStackTrace();
			}
		}
		
		if(Thread.currentThread().getName().equals("dataTransfer"))
		{
			try 
			{
				sendListToServer();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

}
