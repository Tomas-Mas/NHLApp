package com.nhl.model.regulationstatistics;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Team {

	private String name;
	private String abbreviatedName;
	private int id;
	private String conference;
	private String division;
	private TeamStats home;
	private TeamStats away;
	
	public Team() {
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
			home = new TeamStats(rs);
		} else if(rs.getString("team").equals("away")) {
			away = new TeamStats(rs);
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
	
	public TeamStats getHomeStats() {
		return home;
	}
	
	public TeamStats getAwayStats() {
		return away;
	}
	
	public TeamStats getOverallStats() {
		if(home != null && away != null)
			return new TeamStats(home, away);
		else if(home != null && away == null)
			return home;
		else if(home == null && away != null)
			return away;
		else
			return new TeamStats();
	}
	
	//little cheat because of ever changing tiebreaker rules/seeding - newer seasons should be fine without this
	public void substractPoint() {
		this.home.substractPoint();
	}
}
