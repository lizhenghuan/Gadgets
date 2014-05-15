package cn.robtoium;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;


public class DataProcess {
	private static List<HashMap<String, String>> config = null;
	private static List<String> methods = null;
	private static List<String> comments = null;
	private static List<String> commands = null;
	private static String fileName, commandPrefix, instrumentation, testClass, packageName, command;
	

	public static void main(String[] args) {
		createFile();
		List<HashMap<String, String>> lists = new ArrayList<HashMap<String,String>>();
		lists = initLists();
		try {
			WriteXML xml = new WriteXML("testcase.xml", "Methods");
			xml.start();
			for (int i = 0; i < lists.size(); i++) {
				xml.write(lists.get(i), "Method");
			}
			xml.end();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//生成脚本文件,如test.sh或 test.bat
	public static void createFile(){
		initLists();
		try {
			System.out.println("test脚本写入...");
			File file = new File(Bean.currentPath + fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(file);
			String newline = System.getProperty("line.separator");
			for (int i = 0; i < commands.size(); i++) {
				byte[] bytes = commands.get(i).getBytes();  
				out.write(bytes);
				out.write(newline.getBytes());
			}
			out.flush();  
			out.close(); 
			System.out.println("test脚本写入完成");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 初始化packageName,methods,comments,commands
	 */
	public static List<HashMap<String, String>> initLists(){
		comments = new ArrayList<String>();
		commands = new ArrayList<String>();
		comments = readfile("/**", "path");
		methods = readfile("publicvoid", "path");
		if (comments.size() < methods.size()) {
			System.out.println("Error, 有测试方法未添加注释");
		} 
		fileName = config.get(0).get("fileName");
		commandPrefix = config.get(0).get("commandPrefix");
		instrumentation = config.get(0).get("instrumentation");
		testClass = config.get(0).get("testClass");
		packageName = config.get(0).get("packageName");
		for (int i = 0; i < methods.size(); i++) {
			command = commandPrefix + " " + testClass + "#" + methods.get(i).toString() + " " + packageName + "/" + instrumentation;
			commands.add(command);
		}
		List<HashMap<String, String>> lists = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < methods.size(); i++) {
			map.put("PackageName", packageName);
			map.put("MethodName", methods.get(i).toString());
			map.put("MethodComments", comments.get(i).toString());
			map.put("Command", commands.get(i).toString());
			lists.add(map);
		}
		return lists;
	}
	
	/**
	 * 读取AllTest文件
	 * @param str
	 * @return
	 */
	public static List<String> readfile(String str, String key){
		config = parserXml();
		//配置AllTest的读取路径
//		String pathName = Bean.currentPath + config.get(0).get(key);
		String pathName = config.get(0).get(key);
		File file = new File(pathName);
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
			BufferedReader bReader = new BufferedReader(inputStreamReader);
			List<String> strs = new ArrayList<String>();
			String temp = null;
			while ((temp = bReader.readLine()) != null) {
				temp = temp.replaceAll(" ", "").trim();
				if (str == "/**") {
					if (temp.startsWith("/**")) {
						temp = bReader.readLine();
						temp = temp.replace("*", "").trim();
						strs.add(temp);
					}
				} else if (str == "publicvoid"){
					if (temp.contains(str)) {
						temp = temp.replaceFirst(str, "");
						temp = temp.substring(0, temp.indexOf("()"));
						strs.add(temp);
					}
				}
			}
			return strs;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 解析Config.xml文件
	 * @return
	 */
	public static List<HashMap<String, String>> parserXml(){
		try {
			FileInputStream inputStream = new FileInputStream(new File(Bean.currentPath + "Config.xml"));
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			MyHandler handler = new MyHandler(Bean.rootName);
			parser.parse(inputStream, handler);
			inputStream.close();
			return handler.getList();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}