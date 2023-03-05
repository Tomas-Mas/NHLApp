package com.nhl.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.nhl.model.gameresults.GameData;

public class GameDataFetcher extends Database {
	
	public GameDataFetcher() {
	}
	
	public ArrayList<GameData> fetchGames(String season) {
		ArrayList<GameData> games = new ArrayList<GameData>();
		Connection conn = createConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			pst = conn.prepareStatement(readSql("mainPageResults"), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			pst.setInt(1, Integer.parseInt(season.replace("/", "")));
			rs = pst.executeQuery();
			
			GameData game = new GameData();
			while(rs.next()) {
				if(rs.getInt("g_id") != game.getGameId()) {	//found new game id in rs
					if(game.getGameId() != 0) {  //adding not-first game
						games.add(game);
						game = new GameData();
					}
					game.setDataFromResultSet(rs);
				}
			}
			games.add(game);	//adding last game into list
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			closeStatement(pst);
			closeResultSet(rs);
			closeConnection(conn);
		}
		return games;
	}
}
