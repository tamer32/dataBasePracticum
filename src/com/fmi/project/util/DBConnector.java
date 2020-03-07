package com.fmi.project.util;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class DBConnector {
	
	static Connection conn = null;
	static ResultSet result = null;
	static MyModel model = null;
	private static String dbUrl;
	private static String username;
	private static String password;
	
	static {
		Properties props = new Properties();
		InputStream in = DBConnector.class.getResourceAsStream("config.properties");		
		try {
			props.load(in);
		} catch (IOException e) {
			System.out.println("Properties file not found");
		}		
		
		dbUrl = props.getProperty("db.url");
		username = props.getProperty("db.username");
		password = (String) props.getOrDefault("db.password", "");
	}
	
	public static Connection getConnection() {
		
		try {
			Class.forName("org.h2.Driver");
			conn = DriverManager.getConnection(dbUrl, username, password);
		} catch (ClassNotFoundException e) {
			System.out.println("Driver not found");
		} catch (SQLException e) {
			System.out.println("Problem creating connection");
		}
		return conn;
	}
	
	public static MyModel getAllModel(String tableName) {
		String sql = "select * from " + tableName;
		conn = getConnection();
		try {
			PreparedStatement state = conn.prepareStatement(sql);
			result = state.executeQuery();
			model = new MyModel(result);
		} catch (SQLException e) {
			System.out.println("Problem with querry : "+ e.getMessage());
		} catch (Exception e) {
			System.out.println("Unexpected error");
		}
		
		return model;
	}
	
	public static ArrayList<String> getAllValuesInColumn(String columnName,String tableName) {
		String sql = String.format("select %s from %s", columnName, tableName);
		conn = getConnection();
		try {
			PreparedStatement state = conn.prepareStatement(sql);
			result = state.executeQuery();			
		} catch (SQLException e) {
			System.out.println("Problem with querry : "+ e.getMessage());
		} catch (Exception e) {
			System.out.println("Unexpected error");
		}
		
		ArrayList<String> countryNames = new ArrayList<>();
		
		try {
			while (result.next()) { 
				countryNames.add(result.getString(1));		    
			}
		} catch (SQLException e) {
			System.out.println("Problem occurred while converting result to list");
		}
		
		return countryNames;
	}
	
	public static String getAValueFromRowWhere(String columnName,String tableName,String conditionColumn,String conditionValue) {
		String sql = String.format("select %s from %s where %s = '%s'", columnName, tableName,conditionColumn,conditionValue);
		conn = getConnection();
		try {
			PreparedStatement state = conn.prepareStatement(sql);
			result = state.executeQuery();			
		} catch (SQLException e) {
			System.out.println("Problem with querry : "+ e.getMessage());
		} catch (Exception e) {
			System.out.println("Unexpected error");
		}			
		
		try {
			result.next();
			return result.getString(1);
		} catch (SQLException e) {
			System.out.println("Problem occurred while fetching value");
		}
		return null;			
	}

}
