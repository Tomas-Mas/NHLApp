package com.nhl.model.playoffbracket;

import java.util.ArrayList;

import com.nhl.model.playoffmatches.PlayoffMatches;
import com.nhl.model.regulationstatistics.RegulationStatisticsData;
import com.nhl.model.regulationstatistics.Team;


public class PlayoffSeeding {
	
	private String season;
	private RegulationStatisticsData regulationStats;
	private PlayoffMatches playoffMatches;
	private String conference;
	private ArrayList<Team> playoffTeams;
	
	private ArrayList<PlayoffBracketMatch> matches;

	public PlayoffSeeding(RegulationStatisticsData regulationStats, PlayoffMatches playoffMatches, String conference, String season) {
		this.season = season;
		this.regulationStats = regulationStats;
		this.playoffMatches = playoffMatches;
		this.conference = conference;
		this.playoffTeams = this.regulationStats.getTop8ByConference(conference);
		fixDifferencesDueToPreviousTiebreakers();
		findSeeding();
	}
	
	private void fixDifferencesDueToPreviousTiebreakers() {
		if(season.equals("2015/2016")) {
			for(Team team : regulationStats.getTeams()) {
				if(team.getName().equals("Boston Bruins")) {
					team.substractPoint();
				}
			}
		}
	}
	
	private void findSeeding() {
		matches = new ArrayList<PlayoffBracketMatch>();
		
		ArrayList<Team> divWinners = regulationStats.getDivisionWinners(conference);
		ArrayList<Team> wildCards = regulationStats.getWildCards(conference);
		ArrayList<Team> winningDivisionRunnerUps = regulationStats.getDivisionRunnerUps(divWinners.get(0).getDivision());
		ArrayList<Team> secondDivisionRunnerUps = regulationStats.getDivisionRunnerUps(divWinners.get(1).getDivision());
		
		matches.add(new PlayoffBracketMatch(SeedingLabel.FIRSTROUNDDIV1WINNER, playoffMatches, divWinners.get(0), wildCards.get(1)));
		matches.add(new PlayoffBracketMatch(SeedingLabel.FIRSTROUNDDIV1RUNNERUP, playoffMatches, winningDivisionRunnerUps.get(0), winningDivisionRunnerUps.get(1)));
		matches.add(new PlayoffBracketMatch(SeedingLabel.FIRSTROUNDDIV2WINNER, playoffMatches, divWinners.get(1), wildCards.get(0)));
		matches.add(new PlayoffBracketMatch(SeedingLabel.FIRSTROUNDDIV2RUNNERUP, playoffMatches, secondDivisionRunnerUps.get(0), secondDivisionRunnerUps.get(1)));
		
		matches.add(new PlayoffBracketMatch(SeedingLabel.DIV1FINALS, playoffMatches, getMatch(SeedingLabel.FIRSTROUNDDIV1WINNER), getMatch(SeedingLabel.FIRSTROUNDDIV1RUNNERUP)));
		matches.add(new PlayoffBracketMatch(SeedingLabel.DIV2FINALS, playoffMatches, getMatch(SeedingLabel.FIRSTROUNDDIV2WINNER), getMatch(SeedingLabel.FIRSTROUNDDIV2RUNNERUP)));
		
		matches.add(new PlayoffBracketMatch(SeedingLabel.CONFERENCEFINALS, playoffMatches, getMatch(SeedingLabel.DIV1FINALS), getMatch(SeedingLabel.DIV2FINALS)));
	}
	
	public void setConference(String conference) {
		this.conference = conference;
		findSeeding();
	}
	
	public int getTeamSeeding(Team team) {
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
