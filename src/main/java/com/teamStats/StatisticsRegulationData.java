package com.teamStats;

import java.sql.ResultSet;
import java.util.ArrayList;

public class StatisticsRegulationData {
	
	private ArrayList<RegulationTeamData> teams;

	public StatisticsRegulationData(ResultSet rs) {
		teams = new ArrayList<RegulationTeamData>();
		loadDataFromResultSet(rs);
	}
	
	private void loadDataFromResultSet(ResultSet rs) {
		try {
			RegulationTeamData team = new RegulationTeamData();
			while(rs.next()) {
				if(team.getName().equals(rs.getString("name"))) {
					team.loadData(rs);
				} else {
					teams.add(team);
					team = new RegulationTeamData();
					team.loadData(rs);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<RegulationTeamData> getTeamStats() {
		return this.teams;
	}
}
