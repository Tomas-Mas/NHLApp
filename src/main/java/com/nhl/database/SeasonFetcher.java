package com.nhl.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class SeasonFetcher extends Database {

	public SeasonFetcher() {
		
	}
	
	public ArrayList<String> getSeasons() {
		ArrayList<String> seasons = new ArrayList<String>();
		Connection conn = createConnection();
		Statement st =  null;
		ResultSet rs = null;
		
		try {
			st = conn.createStatement();
			String sql = "select distinct season from games order by season desc";
			rs = st.executeQuery(sql);
			
			while(rs.next()) {
				String season = String.valueOf(rs.getInt("season"));
				seasons.add(season.substring(0, 4) + "/" + season.substring(4, 8));
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			closeStatement(st);
			closeResultSet(rs);
			closeConnection(conn);
		}
		return seasons;
	}
}
