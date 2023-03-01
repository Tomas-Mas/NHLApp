package com.statistics;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.teamStats.RegulationTeamData;

public class PlayoffData {

	ArrayList<Match> matches;
	
	public PlayoffData(ResultSet rs) throws SQLException {
		matches = new ArrayList<Match>();
		int matchIndex = 0;
		while(rs.next()) {
			matchIndex = getExistingMatchIndex(rs);
			if(matchIndex >= 0) {
				matches.get(matchIndex).addGame(rs);
			} else {
				matches.add(new Match(rs));
			}
		}
	}
	
	private int getExistingMatchIndex(ResultSet rs) throws SQLException {
		int homeId = rs.getInt("homeTeamId");
		int awayId = rs.getInt("awayTeamId");
		int index = 0;
		for(Match m : matches) {
			if(m.containsId(homeId) && m.containsId(awayId)) {
				return index;
			}
			index++;
		}
		return -1;
	}
	
	public ArrayList<Match> getMatches() {
		return matches;
	}
	
	public Match getMatch(RegulationTeamData team1, RegulationTeamData team2) {
		for(Match match : matches) {
			if(match.consistsOfTeams(team1, team2))
				return match;
		}
		return null;
	}
	
	public RegulationTeamData getWinner(RegulationTeamData team1, RegulationTeamData team2) {
		Match match = getMatch(team1, team2);
		Integer winnerId = match.getWinnerId();
		if(winnerId == null)
			return null;
		if(team1.getId() == winnerId)
			return team1;
		if(team2.getId() == winnerId)
			return team2;
		return null;
	}
	
}
