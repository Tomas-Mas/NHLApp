package com.nhl.model.playoffmatches;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.nhl.model.regulationstatistics.Team;

public class Match {

	private int team1Id;
	private String team1Name;
	private String team1Abbreviation;
	private int team2Id;
	private String team2Name;
	private String team2Abbreviation;
	
	private ArrayList<Game> games;
	
	public Match(ResultSet rs) throws SQLException {
		this.team1Id = rs.getInt("homeTeamId");
		this.team1Name = rs.getString("homeTeamName");
		this.team1Abbreviation = rs.getString("homeAbbreviation");
		this.team2Id = rs.getInt("awayTeamId");
		this.team2Name = rs.getString("awayTeamName");
		this.team2Abbreviation = rs.getString("awayAbbreviation");
		
		games = new ArrayList<Game>();
		games.add(new Game(rs));
	}
	
	public void addGame(ResultSet rs) throws SQLException {
		games.add(new Game(rs));
	}
	
	public boolean containsId(int id) {
		if(team1Id == id || team2Id == id)
			return true;
		return false;
	}
	
	//getters
	public int getTeam1Id() {
		return team1Id;
	}
	
	public String getTeam1Name() {
		return team1Name;
	}
	
	public String getTeam1Abbreviation() {
		return team1Abbreviation;
	}
	
	public int getTeam2Id() {
		return team2Id;
	}
	
	public String getTeam2Name() {
		return team2Name;
	}
	
	public String getTeam2Abbreviation() {
		return team2Abbreviation;
	}
	
	public ArrayList<Game> getGames() {
		return games;
	}
	
	public boolean consistsOfTeams(Team team1, Team team2) {
		if((team1.getId() == team1Id || team1.getId() == team2Id) && (team2.getId() == team1Id || team2.getId() == team2Id))
			return true;
		else
			return false;
	}
	
	public String getTeamScore(int teamId) {
		int points = 0;
		for(Game game : games) {
			if(game.wonBy(teamId))
				points++;
		}
		return String.valueOf(points);
	}
	
	public Integer getWinnerId() {
		int points = Integer.valueOf(getTeamScore(team1Id));
		if(points == 4)
			return team1Id;
		points = Integer.valueOf(getTeamScore(team2Id));
		if(points == 4) 
			return team2Id;
		return null;
	}
}
