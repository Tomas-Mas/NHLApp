package com.teamStats;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TeamData {

	private String name;
	private String abbreviatedName;
	private int id;
	private String conference;
	private String division;
	private TeamStatistics home;
	private TeamStatistics away;
	
	public TeamData() {
	}
	
	public void loadTeamData(ResultSet rs) throws SQLException {
		this.name = rs.getString("name");
		this.abbreviatedName = rs.getString("abbreviation");
		this.id = rs.getInt("id");
		this.conference = rs.getString("conference");
		this.division = rs.getString("division");
		loadStats(rs);
	}
	
	public void loadStats(ResultSet rs) throws SQLException {
		if(rs.getString("team").equals("home")) {
			home = new TeamStatistics(rs);
		} else if(rs.getString("team").equals("away")) {
			away = new TeamStatistics(rs);
		}
	}
	
	public String getName() {
		return name;
	}
	
	public String getAbbreviatedName() {
		return abbreviatedName;
	}
	
	public int getId() {
		return id;
	}
	
	public String getConference() {
		return conference;
	}
	
	public String getDivision() {
		return division;
	}
	
	public TeamStatistics getHomeStats() {
		return home;
	}
	
	public TeamStatistics getAwayStats() {
		return away;
	}
	
	public TeamStatistics getOverallStats() {
		if(home != null && away != null)
			return new TeamStatistics(home, away);
		else if(home != null && away == null)
			return home;
		else if(home == null && away != null)
			return away;
		else
			return new TeamStatistics();
	}
}
