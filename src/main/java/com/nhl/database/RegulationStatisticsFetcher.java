package com.nhl.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.nhl.model.regulationstatistics.RegulationStatisticsData;

public class RegulationStatisticsFetcher extends Database {
	
	public RegulationStatisticsFetcher() {
	}
	
	public RegulationStatisticsData fetchStats(String season) {
		RegulationStatisticsData stats = null;
		Connection conn = createConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = conn.prepareStatement(readSql("teamStandings"));
			int s = Integer.parseInt(season.replace("/", ""));
			stmt.setInt(1, s);
			stmt.setInt(2, s);
			stmt.setInt(3, s);
			rs = stmt.executeQuery();
			
			stats = new RegulationStatisticsData(rs);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeStatement(stmt);
			closeResultSet(rs);
			closeConnection(conn);
		}
		return stats;
	}
}
