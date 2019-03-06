package com.sotero.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Properties;

import org.bson.json.JsonParseException;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.util.JSONParseException;

public class APITesting {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double overallElapsedTimes = 0;
		long overallTime = 0;
		int recordsLength = 0;
		//Creating CSV File
		PrintWriter writingCSV = null;
		try{
			
		new PrintWriter(new File(System.getProperty("user.dir")+"/TestHarness.csv"));
		StringBuilder builderContents = new StringBuilder();
		builderContents.append("FILENAME,RECORDS,ELAPSED_TIME,OVERALL_TIME"+"\n");
		String path = args[0].trim();
		File file = new File(path);
		//Checking the path whether is a Directory or not
		if(!file.isDirectory()){
			System.out.println("The given path is not a directory");
		return;	
		}
		String propPath = System.getProperty("user.dir") + "/jdbc.properties";
		InputStream in = new FileInputStream(new File(propPath));
		Properties properties = new Properties();
		properties.load(in);
		String[] allJsonFiles = file.list();
		for(String name : allJsonFiles){
			
			//StartTime
			long startTime = System.nanoTime();
			JSONObject jsontxtarr = new JSONObject();
			String content = new String(Files.readAllBytes(Paths.get(path+"/"+name)));
	        jsontxtarr = new JSONObject(content);
	        
	        //Calling API Method
	        if(properties.getProperty("api.path").trim().length() > 2){
	        	
	        	System.out.println("Entered API Path is Incorrect");
	        	System.exit(0);
	        }
	        JSONObject outputJSON = readJsonFromUrl("http://"+properties.getProperty("api.host").trim()+":45670/path"+properties.getProperty("api.path").trim(), jsontxtarr);
	        long endTime = (System.nanoTime() - startTime) /1000000;
	        //If you want the Output then open the below comments
	      /*FileWriter fw = new FileWriter(path+"/"+name.split("\\.")[0]+"output.txt");
	        fw.write(outputJSON.toString());
	        fw.flush();*/
	        //Sum of all Elapsed,overall and records
	        builderContents.append(name+","+jsontxtarr.getJSONArray("RECORDS").length()+","+outputJSON.get("elapsedtime").toString()+","+endTime+"\n");
	        System.out.println("Records = "+jsontxtarr.getJSONArray("RECORDS").length()+", Elapsed Time = "+outputJSON.get("elapsedtime").toString()+", OverallTime = "+endTime);
	        overallElapsedTimes +=Double.parseDouble(outputJSON.get("elapsedtime").toString());
	        overallTime +=endTime;
	        recordsLength +=jsontxtarr.getJSONArray("RECORDS").length();
			
		}
		
		builderContents.append(",,,,"+"\n");
		builderContents.append("Sum,"+recordsLength+","+overallElapsedTimes+","+overallTime+"\n");
		writingCSV.write(builderContents.toString());
		writingCSV.close();
		System.out.println("Sum: Records = "+recordsLength+", Sum of ElapsedTime = "+overallElapsedTimes+", Sum of OVerallTime = "+overallTime+"\n");
		
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(JSONParseException e){
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	//Making API Calls
	public static JSONObject readJsonFromUrl(String url, JSONObject jsontxtarr) throws IOException, JSONException, ParseException {
		//InputStream is = new URL(url).openStream();
		URL object = new URL(url);
		HttpURLConnection contxt = (HttpURLConnection) object.openConnection();
		try {
			
			contxt.setDoOutput(true);
			contxt.setDoInput(true);
			contxt.setRequestProperty("Content-Type", "application/json");
			contxt.setRequestProperty("Accept", "application/json");
			contxt.setRequestMethod("POST");
			 OutputStream os = contxt.getOutputStream();
	            os.write(jsontxtarr.toString().getBytes());
	            os.flush();
			BufferedReader rd = new BufferedReader(new InputStreamReader(contxt.getInputStream()));
			
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			contxt.disconnect();
		}
	}
	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

}
