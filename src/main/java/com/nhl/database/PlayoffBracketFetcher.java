package com.nhl.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.nhl.model.playoffmatches.PlayoffMatches;

public class PlayoffBracketFetcher extends Database {
	
	public PlayoffBracketFetcher() {
	}
	
	public PlayoffMatches fetchPlayoffMatches(String season) {
		PlayoffMatches playoffData = null;
		Connection conn = createConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = conn.prepareStatement(readSql("playoffResults"));
			stmt.setInt(1, Integer.parseInt(season.replace("/", "")));
			rs = stmt.executeQuery();
			
			playoffData = new PlayoffMatches(rs);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			closeResultSet(rs);
			closeStatement(stmt);
			closeConnection(conn);
		}
		return playoffData;
	}
}
