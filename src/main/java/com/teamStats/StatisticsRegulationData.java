package com.teamStats;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
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
	
	public ArrayList<RegulationTeamData> getTop8ByConference(String conference) {
		ArrayList<RegulationTeamData> top8 = new ArrayList<RegulationTeamData>();
		int ind = 0;
		for(RegulationTeamData team : getTeamsByConference(conference)) {
			if(ind == 8) {
				break;
			}
			top8.add(team);
			ind++;
		}
		return sortByPoints(top8);
	}
	
	public ArrayList<RegulationTeamData> getDivisionWinners(String conference) {
		ArrayList<RegulationTeamData> divWinners = new ArrayList<RegulationTeamData>();
		divWinners.add(getTeamsByDivisions(confDivs.get(conference).get(0)).get(0));
		divWinners.add(getTeamsByDivisions(confDivs.get(conference).get(1)).get(0));
		return sortByPoints(divWinners);
	}
	
	public ArrayList<RegulationTeamData> getWildCards(String conference) {
		ArrayList<RegulationTeamData> wildCards = new ArrayList<RegulationTeamData>();
		ArrayList<RegulationTeamData> divisionTeams = getTeamsByDivisions(confDivs.get(conference).get(0));
		wildCards.add(divisionTeams.get(3));
		wildCards.add(divisionTeams.get(4));
		divisionTeams = getTeamsByDivisions(confDivs.get(conference).get(1));
		wildCards.add(divisionTeams.get(3));
		wildCards.add(divisionTeams.get(4));
		return sortByPoints(wildCards);
	}
	
	public ArrayList<RegulationTeamData> getDivisionRunnerUps(String division) {
		ArrayList<RegulationTeamData> runnerUps = new ArrayList<RegulationTeamData>();
		ArrayList<RegulationTeamData> divisionTeams = getTeamsByDivisions(division);
		runnerUps.add(divisionTeams.get(1));
		runnerUps.add(divisionTeams.get(2));
		return runnerUps;
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
		/*teams.sort((o2, o1)
				->Integer.valueOf(o1.getOverallStats().getPoints()).compareTo(o2.getOverallStats().getPoints()));
		return teams;*/
		
		//tiebreakers
		teams.sort(new Comparator<RegulationTeamData>() {
			
			@Override
			public int compare(RegulationTeamData o2, RegulationTeamData o1) {
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
