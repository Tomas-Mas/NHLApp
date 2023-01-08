package com.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

	private String oracleConnection = "jdbc:oracle:thin:@localhost:1521:orcl";
	private String user = "c##nhl";
	private String password = "nhl";
	private String scriptLocation = "C:\\Data\\EclipseWorkspace\\NhlApp\\src\\main\\webapp\\sources\\sql\\";
	
	public Connection createConnection() {
		Connection conn = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(oracleConnection, user, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public void closeConnection(Connection conn) {
		try {
			if(conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void closeStatement(Statement st) {
		try {
			if(st != null)
				st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void closeResultSet(ResultSet rs) {
		try {
			if(rs != null) 
				rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String readSql(String fileName) {
		BufferedReader br = null;
		StringBuilder sql = new StringBuilder();
		try {
			br = new BufferedReader(new FileReader(scriptLocation + fileName + ".sql"));
			String line;
			
			while((line = br.readLine()) != null) {
				sql.append(line + "\n");
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try { br.close(); } catch(Exception e) { e.printStackTrace(); }
		}
		return sql.toString();
	}
}
