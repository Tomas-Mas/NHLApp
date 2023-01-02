package com.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

	private String oracleConnection = "jdbc:oracle:thin:@localhost:1521:orcl";
	private String user = "c##nhl";
	private String password = "nhl";
	
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
}
