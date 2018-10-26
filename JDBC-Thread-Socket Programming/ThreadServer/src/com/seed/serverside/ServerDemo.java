package com.seed.serverside;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.seed.model.Employee;

public class ServerDemo {

	public static void getListFromClient() throws IOException, ClassNotFoundException
	{
		ServerSocket ss = new ServerSocket(5000);
		System.out.println("Server Started...");
		System.out.println("Waiting for Client Request");
		Socket s = ss.accept();
		ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
		
		List<Employee> empList = (List<Employee>) ois.readObject();
		
		for (Employee employee : empList) {
			
			System.out.println(employee);
		}
		
	}
	
	public static void main(String[] args) 
	{
		try 
		{
			getListFromClient();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
