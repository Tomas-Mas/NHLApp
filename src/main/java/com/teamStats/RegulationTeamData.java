package com.teamStats;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RegulationTeamData {

	private String name;
	private int id;
	private String conference;
	private String division;
	private RegulationTeamStatistics home;
	private RegulationTeamStatistics away;
	
	public RegulationTeamData() {
	}
	
	public void loadTeamData(ResultSet rs) throws SQLException {
		this.name = rs.getString("name");
		this.id = rs.getInt("id");
		this.conference = rs.getString("conference");
		this.division = rs.getString("division");
		loadStats(rs);
	}
	
	public void loadStats(ResultSet rs) throws SQLException {
		if(rs.getString("team").equals("home")) {
			home = new RegulationTeamStatistics(rs);
		} else if(rs.getString("team").equals("away")) {
			away = new RegulationTeamStatistics(rs);
		}
	}
	
	public String getName() {
		return name;
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
	
	public RegulationTeamStatistics getHomeStats() {
		return home;
	}
	
	public RegulationTeamStatistics getAwayStats() {
		return away;
	}
	
	public RegulationTeamStatistics getOverallStats() {
		if(home != null && away != null)
			return new RegulationTeamStatistics(home, away);
		else if(home != null && away == null)
			return home;
		else if(home == null && away != null)
			return away;
		else
			return new RegulationTeamStatistics();
	}
}
