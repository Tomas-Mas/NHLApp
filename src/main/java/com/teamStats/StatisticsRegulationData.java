package com.teamStats;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class StatisticsRegulationData {
	
	private ArrayList<RegulationTeamData> teams;
	private HashMap<String, ArrayList<String>> confDivs;

	public StatisticsRegulationData(ResultSet rs) {
		teams = new ArrayList<RegulationTeamData>();
		confDivs = new HashMap<String, ArrayList<String>>();
		loadDataFromResultSet(rs);
	}
	
	private void loadDataFromResultSet(ResultSet rs) {
		try {
			RegulationTeamData team = new RegulationTeamData();
			while(rs.next()) {
				if(team.getName() == null) {
					team.loadTeamData(rs);
				} else if (team.getName().equals(rs.getString("name"))) {
					team.loadStats(rs);
					teams.add(team);
					team = new RegulationTeamData();
				}
				mapConferenceDivisions(rs);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<RegulationTeamData> getTeams() {
		return this.teams;
	}
	
	public ArrayList<RegulationTeamData> getTeamsByConference(String conference) {
		ArrayList<RegulationTeamData> teamsByConference = new ArrayList<RegulationTeamData>();
		for(RegulationTeamData team : teams) {
			if(team.getConference().equals(conference)) {
				teamsByConference.add(team);
			}
		}
		return sortByPoints(teamsByConference);
	}
	
	public ArrayList<RegulationTeamData> getTeamsByDivisions(String division) {
		ArrayList<RegulationTeamData> teamsByDivision = new ArrayList<RegulationTeamData>();
		for(RegulationTeamData team : teams) {
			if(team.getDivision().equals(division)) {
				teamsByDivision.add(team);
			}
		}
		return sortByPoints(teamsByDivision);
	}
	
	public HashMap<String, ArrayList<String>> getConferencesDivisions() {
		return confDivs;
	}
	
	private void mapConferenceDivisions(ResultSet rs) throws SQLException {
		ArrayList<String> divisions;
		if(!confDivs.containsKey(rs.getString("conference"))) {
			confDivs.put(rs.getString("conference"), new ArrayList<String>());
		} else {
			divisions = confDivs.get(rs.getString("conference"));
			if(!divisions.contains(rs.getString("division"))) {
				divisions.add(rs.getString("division"));
				confDivs.replace(rs.getString("conference"), divisions);
			}
		}
	}
	
	private ArrayList<RegulationTeamData> sortByPoints(ArrayList<RegulationTeamData> teams) {
		teams.sort((o2, o1)
				->Integer.valueOf(o1.getOverallStats().getPoints()).compareTo(o2.getOverallStats().getPoints()));
		return teams;
	}
}
