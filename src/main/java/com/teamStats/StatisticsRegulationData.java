package com.teamStats;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class StatisticsRegulationData {
	
	private ArrayList<TeamData> teams;
	private HashMap<String, ArrayList<String>> confDivs;

	public StatisticsRegulationData(ResultSet rs) {
		teams = new ArrayList<TeamData>();
		confDivs = new HashMap<String, ArrayList<String>>();
		loadDataFromResultSet(rs);
	}
	
	private void loadDataFromResultSet(ResultSet rs) {
		try {
			TeamData team = new TeamData();
			while(rs.next()) {
				if(team.getName() == null) {
					team.loadTeamData(rs);
				} else if (team.getName().equals(rs.getString("name"))) {
					team.loadStats(rs);
					teams.add(team);
					team = new TeamData();
				}
				mapConferenceDivisions(rs);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<TeamData> getTeams() {
		return this.teams;
	}
	
	public ArrayList<TeamData> getTeamsByConference(String conference) {
		ArrayList<TeamData> teamsByConference = new ArrayList<TeamData>();
		for(TeamData team : teams) {
			if(team.getConference().equals(conference)) {
				teamsByConference.add(team);
			}
		}
		return sortByPoints(teamsByConference);
	}
	
	public ArrayList<TeamData> getTeamsByDivisions(String division) {
		ArrayList<TeamData> teamsByDivision = new ArrayList<TeamData>();
		for(TeamData team : teams) {
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
	
	private ArrayList<TeamData> sortByPoints(ArrayList<TeamData> teams) {
		/*teams.sort((o2, o1)
				->Integer.valueOf(o1.getOverallStats().getPoints()).compareTo(o2.getOverallStats().getPoints()));
		return teams;*/
		
		//tiebreakers
		teams.sort(new Comparator<TeamData>() {
			
			@Override
			public int compare(TeamData o2, TeamData o1) {
				int res = Integer.valueOf(o1.getOverallStats().getPoints()).compareTo(o2.getOverallStats().getPoints());
				if(res != 0) {
					return res;
				}
				res = Integer.valueOf(o2.getOverallStats().getGamesPlayed()).compareTo(o1.getOverallStats().getGamesPlayed());
				//System.out.println("games played: " + o1.getOverallStats().getGamesPlayed() + " - " + o2.getOverallStats().getGamesPlayed() + " " + res);
				if(res != 0) {
					return res;
				}
				res = Integer.valueOf(o1.getOverallStats().getRegulationWins()).compareTo(o2.getOverallStats().getRegulationWins());
				//System.out.println("regulation wins: " + o1.getOverallStats().getRegulationWins() + " - " + o2.getOverallStats().getRegulationWins() + " " + res);
				if(res != 0) {
					return res;
				}
				res = Integer.valueOf(o1.getOverallStats().getRegulationOvertimeWins()).compareTo(o2.getOverallStats().getRegulationOvertimeWins());
				//System.out.println("regulation + OT wins: " + o1.getOverallStats().getRegulationOvertimeWins() + " - " + o2.getOverallStats().getRegulationOvertimeWins() + " " + res);
				if(res != 0) {
					return res;
				}
				res = Integer.valueOf(o1.getOverallStats().getTotalWins()).compareTo(o2.getOverallStats().getTotalWins());
				//System.out.println("regulation + OT + SO wins: " + o1.getOverallStats().getTotalWins() + " - " + o2.getOverallStats().getTotalWins() + " " + res);
				if(res != 0) {
					return res;
				}
				System.out.println("whops team order might not be correct");
				// TODO from nhl.com about tiebreakers
				/*The greater number of points earned in games against each other among two or more tied clubs. 
				For the purpose of determining standing for two or more Clubs that have not played an even number of games 
				with one or more of the other tied Clubs, the first game played in the city that has the extra game (the "odd game") 
				shall not be included. When more than two Clubs are tied, the percentage of available points earned in games among each other 
				(and not including any "odd games") shall be used to determine standing.*/
				
				res = Integer.valueOf(o1.getOverallStats().getGoalDifference()).compareTo(o2.getOverallStats().getGoalDifference());
				//System.out.println("goal difference: " + o1.getOverallStats().getGoalDifference() + " - " + o2.getOverallStats().getGoalDifference() + " " + res);
				if(res != 0) {
					return res;
				}
				res = Integer.valueOf(o1.getOverallStats().getGoalsFor()).compareTo(o2.getOverallStats().getGoalsFor());
				//System.out.println("goals for: " + o1.getOverallStats().getGoalsFor() + " - " + o2.getOverallStats().getGoalsFor() + " " + res);
				return res;
			}
			
		});
		return teams;
	}
}
