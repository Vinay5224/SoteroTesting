package com.sotero.jsoncreate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JsonCreation {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try{
			long startTime = System.nanoTime();
			String path = System.getProperty("user.dir")+"/jdbc.properties";
			InputStream in = new FileInputStream(new File(path));
					//"/home/exa1/Desktop/jdbc.properties"));
			Properties properties = new Properties();
			properties.load(in);
		Class.forName("com.mysql.jdbc.Driver");
		Connection  con = DriverManager.getConnection("jdbc:mysql://"+properties.getProperty("jdbc.url").trim()+":"+properties.getProperty("jdbc.port").trim()+"/"+properties.getProperty("jdbc.db").trim(), properties.getProperty("jdbc.user").trim(), properties.getProperty("jdbc.password").trim());
	//	Connection  con = DriverManager.getConnection("jdbc:mysql://34.239.121.181:6033/dbtest_enc", "root", "Exafluence201");
		String sql ="select * from "+properties.getProperty("jdbc.table").trim()+" limit "+args[0]+";";
		Statement stmt = con.createStatement();
		ResultSet rs =  stmt.executeQuery(sql);
		long endtime = (System.nanoTime() - startTime) /1000000;
		System.out.println("Total Time::::: "+endtime+ " ms");
		
		if(con!=null){
			con.close();
		}
		if(stmt!=null){
			stmt.close();
		}
		if(rs!=null){
			rs.close();
		}
		}catch(SQLException e){
			e.printStackTrace();
		}catch(ClassNotFoundException es){
			es.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
