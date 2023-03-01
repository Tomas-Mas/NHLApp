package com.statistics;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Game {

	private int gameId;
	private String gameDate;
	private int homeId;
	private String homeName;
	private String homeAbbreviation;
	private int awayId;
	private String awayName;
	private String awayAbbreviation;
	private int periods;
	private int homeScore;
	private int awayScore;
	
	public Game(ResultSet rs) throws SQLException {
		this.gameId = rs.getInt("gameId");
		this.gameDate = rs.getString("gameDate");
		this.homeId = rs.getInt("homeTeamId");
		this.homeName = rs.getString("homeTeamName");
		this.homeAbbreviation = rs.getString("homeAbbreviation");
		this.awayId = rs.getInt("awayTeamId");
		this.awayName = rs.getString("awayTeamName");
		this.awayAbbreviation = rs.getString("awayAbbreviation");
		this.periods = rs.getInt("periods");
		this.homeScore = rs.getInt("homeScore");
		this.awayScore = rs.getInt("awayScore");
	}
	
	public int getGameId() {
		return gameId;
	}
	
	public String getGameDate() {
		return gameDate;
	}
	
	public int getHomeId() {
		return homeId;
	}
	
	public String getHomeName() {
		return homeName;
	}
	
	public String getHomeAbbreviation() {
		return homeAbbreviation;
	}
	
	public int getAwayId() {
		return awayId;
	}
	
	public String getAwayName() {
		return awayName;
	}
	
	public String getAwayAbbreviation() {
		return awayAbbreviation;
	}
	
	public int getPeriods() {
		return periods;
	}
	
	public int getHomeScore() {
		return homeScore;
	}
	
	public int getAwayScore() {
		return awayScore;
	}
	
	public boolean wonBy(int id) {
		if(homeId == id) {
			if(homeScore > awayScore)
				return true;
			else
				return false;
		} else {
			if(awayScore > homeScore)
				return true;
			else 
				return false;
		}
	}
}
