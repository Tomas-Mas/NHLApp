package com.teamStats;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RegulationTeamData {

	private String name;
	private String conference;
	private String division;
	private RegulationTeamStatistics home;
	private RegulationTeamStatistics away;
	
	public RegulationTeamData() {
		this.name = "";
	}
	
	public void loadData(ResultSet rs) throws SQLException {
		this.name = rs.getString("name");
		this.conference = rs.getString("conference");
		this.division = rs.getString("division");
		if(rs.getString("team").equals("home")) {
			home = new RegulationTeamStatistics(rs);
		} else if(rs.getString("team").equals("away")) {
			away = new RegulationTeamStatistics(rs);
		}
	}
	
	public String getName() {
		return name;
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
