package com.teamStats;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.database.Database;

@WebServlet("/StatisticsRegulationServlet")
public class StatisticsRegulationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private Database db;
	private String contextPath;
	
    public StatisticsRegulationServlet() {
        super();
        db = new Database();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		contextPath = request.getContextPath();
		String season = (String)request.getAttribute("season");
		StatisticsRegulationData stats = getStats(season);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	
	private StatisticsRegulationData getStats(String season) {
		StatisticsRegulationData stats = null;
		Connection conn = db.createConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = conn.prepareStatement(db.readSql("teamStandings"));
			int s = Integer.parseInt(season.replace("/", ""));
			stmt.setInt(1, s);
			stmt.setInt(2, s);
			stmt.setInt(3, s);
			rs = stmt.executeQuery();
			
			stats = new StatisticsRegulationData(rs);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.closeStatement(stmt);
			db.closeResultSet(rs);
			db.closeConnection(conn);
		}
		return stats;
	}
}
