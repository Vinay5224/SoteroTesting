package com.sotero.jsoncreate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import java.sql.ResultSetMetaData;

public class JsonCreation {

	static Connection con;
	static Statement stmt;
	static ResultSet rs;
	static ResultSetMetaData rsmd;
	static Properties properties;

	public static void main(String[] args) {

		try {
			
			String batchSize = args[0].trim(); //"10000";
			if (batchSize.length() > 5) {
				System.out.println("Please, Enter the Valid BatchSize like 10000");
				return;
			}
			
			JSONObject jsonObj = new JSONObject();
			JSONArray jsonarray = new JSONArray();
			
			int batch = Integer.parseInt(batchSize);
			ArrayList<Integer> batchMatch = connectionProperties(batch);
			rsmd = rs.getMetaData();
			int rscount = rsmd.getColumnCount();

			// Creating JsonObject for the ColumnHeaders
			JSONArray colArray = new JSONArray();
			for (int j = 1; j <= rscount; j++) {
				JSONObject temp1 = new JSONObject();
				// Here column names are splitting with table name
				String val = rsmd.getColumnName(j).contains(".") ? rsmd.getColumnName(j).split("\\.")[1]
						: rsmd.getColumnName(j);
				temp1.put("name", val);
				temp1.put("tablename",
						properties.getProperty("sql.query").toUpperCase().split("FROM ")[1].split(" |n")[0]);
				colArray.add(temp1);
			}
			jsonObj.put("COLUMNS", colArray);
			jsonObj.put("aid", properties.getProperty("appID"));
			jsonObj.put("tid", "0");

			// Creating Records
			int batchCount = 0;
			int limit = Integer.parseInt(properties.getProperty("sql.query").toUpperCase().split("LIMIT ")[1].trim());
			JSONArray innerArray = new JSONArray();
			while (rs.next()) {

				// Creating the batch files
				JSONObject rowObj = new JSONObject();
				JSONArray rowArray = new JSONArray();
				for (int i = 1; i <= rscount; i++) {
					rowArray.add(rs.getObject(i).toString());

				}
				rowObj.put("ROWS", rowArray);
				innerArray.add(rowObj);

				if (batchMatch.contains(batchCount)) {
					JSONObject fileObj = new JSONObject();
					fileObj = jsonObj;
					fileObj.put("RECORDS", innerArray);
					FileWriter fw = new FileWriter("/home/ec2-user/JsonCreation/" + batchCount + ".txt");
					fw.write(fileObj.toString());
					fw.flush();
					innerArray.clear();
					innerArray = new JSONArray();

				} else if (limit == batchCount) {

					JSONObject fileObj = new JSONObject();
					fileObj = jsonObj;
					fileObj.put("RECORDS", innerArray);
					FileWriter fw = new FileWriter("/home/ec2-user/JsonCreation/" + batchCount + ".txt");
					fw.write(fileObj.toString());
					fw.flush();
					innerArray.clear();
					innerArray = new JSONArray();

				}

				batchCount++;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (con != null) {
					con.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public static ArrayList<Integer> connectionProperties(int batch) {

		ArrayList<Integer> batchMatch = new ArrayList<Integer>();
		try {
			String path = System.getProperty("user.dir") + "/jdbc.properties";
			InputStream in = new FileInputStream(new File(path));
			properties.load(in);
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://" + properties.getProperty("jdbc.url").trim() + ":"
					+ properties.getProperty("jdbc.port").trim() + "/" + properties.getProperty("jdbc.db").trim(),
					properties.getProperty("jdbc.user").trim(), properties.getProperty("jdbc.password").trim());
			String query = properties.getProperty("sql.query").toUpperCase();
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			for (int i = 0; i <= 50; i++) {
				batchMatch.add(batch * i);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return batchMatch;
	}

}
