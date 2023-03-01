package com.statistics;

import java.util.ArrayList;

import com.teamStats.RegulationTeamData;
import com.teamStats.StatisticsRegulationData;

public class PlayoffSeeding {
	
	private String season;
	private StatisticsRegulationData regulationStats;
	private PlayoffData playoffData;
	private String conference;
	private ArrayList<RegulationTeamData> playoffTeams;
	
	private ArrayList<PlayoffBracketMatch> matches;

	public PlayoffSeeding(StatisticsRegulationData regulationStats, PlayoffData playoffData, String conference, String season) {
		this.season = season;
		this.regulationStats = regulationStats;
		this.playoffData = playoffData;
		this.conference = conference;
		this.playoffTeams = this.regulationStats.getTop8ByConference(conference);
		fixDifferencesDueToPreviousTiebreakers();
		findSeeding();
	}
	
	private void fixDifferencesDueToPreviousTiebreakers() {
		if(season.equals("2015/2016")) {
			for(RegulationTeamData team : regulationStats.getTeams()) {
				if(team.getName().equals("Boston Bruins")) {
					team.substractPoint();
				}
			}
		}
	}
	
	private void findSeeding() {
		matches = new ArrayList<PlayoffBracketMatch>();
		
		ArrayList<RegulationTeamData> divWinners = regulationStats.getDivisionWinners(conference);
		ArrayList<RegulationTeamData> wildCards = regulationStats.getWildCards(conference);
		ArrayList<RegulationTeamData> winningDivisionRunnerUps = regulationStats.getDivisionRunnerUps(divWinners.get(0).getDivision());
		ArrayList<RegulationTeamData> secondDivisionRunnerUps = regulationStats.getDivisionRunnerUps(divWinners.get(1).getDivision());
		
		matches.add(new PlayoffBracketMatch(SeedingLabel.FIRSTROUNDDIV1WINNER, playoffData, divWinners.get(0), wildCards.get(1)));
		matches.add(new PlayoffBracketMatch(SeedingLabel.FIRSTROUNDDIV1RUNNERUP, playoffData, winningDivisionRunnerUps.get(0), winningDivisionRunnerUps.get(1)));
		matches.add(new PlayoffBracketMatch(SeedingLabel.FIRSTROUNDDIV2WINNER, playoffData, divWinners.get(1), wildCards.get(0)));
		matches.add(new PlayoffBracketMatch(SeedingLabel.FIRSTROUNDDIV2RUNNERUP, playoffData, secondDivisionRunnerUps.get(0), secondDivisionRunnerUps.get(1)));
		
		matches.add(new PlayoffBracketMatch(SeedingLabel.DIV1FINALS, playoffData, getMatch(SeedingLabel.FIRSTROUNDDIV1WINNER), getMatch(SeedingLabel.FIRSTROUNDDIV1RUNNERUP)));
		matches.add(new PlayoffBracketMatch(SeedingLabel.DIV2FINALS, playoffData, getMatch(SeedingLabel.FIRSTROUNDDIV2WINNER), getMatch(SeedingLabel.FIRSTROUNDDIV2RUNNERUP)));
		
		matches.add(new PlayoffBracketMatch(SeedingLabel.CONFERENCEFINALS, playoffData, getMatch(SeedingLabel.DIV1FINALS), getMatch(SeedingLabel.DIV2FINALS)));
	}
	
	public void setConference(String conference) {
		this.conference = conference;
		findSeeding();
	}
	
	public int getTeamSeeding(RegulationTeamData team) {
		return playoffTeams.indexOf(team) + 1;
	}
	
	public ArrayList<PlayoffBracketMatch> getMatches() {
		return matches;
	}
	
	public PlayoffBracketMatch getMatch(SeedingLabel label) {
		for(PlayoffBracketMatch match : matches) {
			if(match.getLabel() == label)
				return match;
		}
		return null;
	}
}
