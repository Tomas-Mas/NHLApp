package com.statistics;

import java.util.ArrayList;

import com.teamStats.RegulationTeamData;

public class PlayoffBracketMatch {
	
	private SeedingLabel label;
	private PlayoffData playoffData;
	private RegulationTeamData higherSeedTeam;
	private RegulationTeamData lowerSeedTeam;
	private Match match;
	
	private PlayoffBracketMatch prevBracketHigherSeed;
	private PlayoffBracketMatch prevBracketLowerSeed;
	private boolean finished;
	private RegulationTeamData winner;

	PlayoffBracketMatch(SeedingLabel label, PlayoffData playoffData, RegulationTeamData team1, RegulationTeamData team2) {
		this.label = label;
		this.playoffData = playoffData;
		this.higherSeedTeam = team1;
		this.lowerSeedTeam = team2;
		this.match = playoffData.getMatch(higherSeedTeam, lowerSeedTeam);
		this.prevBracketHigherSeed = null;
		this.prevBracketLowerSeed = null;
		
		Integer winnerId = match.getWinnerId();
		if(winnerId == null)
			finished = false;
		else {
			finished = true;
			if(higherSeedTeam.getId() == winnerId)
				winner = higherSeedTeam;
			else if(lowerSeedTeam.getId() == winnerId)
				winner = lowerSeedTeam;
			else
				System.out.println("finding winner of match in PlayoffBracketMatch is being mean");
		}
	}
	
	PlayoffBracketMatch(SeedingLabel label, PlayoffData playoffData, PlayoffBracketMatch prevBracketHigherSeed, PlayoffBracketMatch prevBracketLowerSeed) {
		this.label = label;
		this.playoffData = playoffData;
		this.prevBracketHigherSeed = prevBracketHigherSeed;
		this.prevBracketLowerSeed = prevBracketLowerSeed;
		
		if(prevBracketHigherSeed.isFinished()) {
			higherSeedTeam = prevBracketHigherSeed.getWinner();
		} else {
			higherSeedTeam = null;
		}
		if(prevBracketLowerSeed.isFinished()) {
			lowerSeedTeam = prevBracketLowerSeed.getWinner();
		} else {
			lowerSeedTeam = null;
		}
		
		if(higherSeedTeam != null && lowerSeedTeam != null) {
			match = playoffData.getMatch(higherSeedTeam, lowerSeedTeam);
		} else {
			match = null;
		}
		
		Integer winnerId;
		if(match != null) {
			winnerId = match.getWinnerId();
			if(winnerId == null) {
				finished = false;
			} else {
				finished = true;
				if(higherSeedTeam.getId() == winnerId)
					winner = higherSeedTeam;
				else if(lowerSeedTeam.getId() == winnerId)
					winner = lowerSeedTeam;
				else
					System.out.println("finding winner of match in PlayoffBracketMatch is being mean");
			}
		}
	}
	
	PlayoffBracketMatch(SeedingLabel label, PlayoffData playoffData, PlayoffBracketMatch prevBracketHigherSeed) {
		this.label = label;
		this.playoffData = playoffData;
		this.prevBracketHigherSeed = prevBracketHigherSeed;
		
		if(prevBracketHigherSeed.isFinished()) {
			higherSeedTeam = prevBracketHigherSeed.getWinner();
		} else {
			higherSeedTeam = null;
		}
		
		if(higherSeedTeam != null && lowerSeedTeam != null) {
			match = playoffData.getMatch(higherSeedTeam, lowerSeedTeam);
		} else {
			match = null;
		}
		
		Integer winnerId;
		if(match != null) {
			winnerId = match.getWinnerId();
			if(winnerId == null) {
				finished = false;
			} else {
				finished = true;
				if(higherSeedTeam.getId() == winnerId)
					winner = higherSeedTeam;
				else if(lowerSeedTeam.getId() == winnerId)
					winner = lowerSeedTeam;
				else
					System.out.println("finding winner of match in PlayoffBracketMatch is being mean");
			}
		}
	}
	
	public ArrayList<RegulationTeamData> getTeams() {
		ArrayList<RegulationTeamData> teams = new ArrayList<RegulationTeamData>();
		teams.add(higherSeedTeam);
		teams.add(lowerSeedTeam);
		return teams;
	}
	
	public void setPrevBracketLowerSeed(PlayoffBracketMatch prevBracketLowerSeedMatch) {
		this.prevBracketLowerSeed = prevBracketLowerSeedMatch;
		if(prevBracketLowerSeed.isFinished()) {
			lowerSeedTeam = prevBracketLowerSeed.getWinner();
		} else {
			lowerSeedTeam = null;
		}
		
		if(higherSeedTeam != null && lowerSeedTeam != null) {
			match = playoffData.getMatch(higherSeedTeam, lowerSeedTeam);
		} else {
			match = null;
		}
		
		Integer winnerId;
		if(match != null) {
			winnerId = match.getWinnerId();
			if(winnerId == null) {
				finished = false;
			} else {
				finished = true;
				if(higherSeedTeam.getId() == winnerId)
					winner = higherSeedTeam;
				else if(lowerSeedTeam.getId() == winnerId)
					winner = lowerSeedTeam;
				else
					System.out.println("finding winner of match in PlayoffBracketMatch is being mean");
			}
		}
	}
	
	public SeedingLabel getLabel() {
		return label;
	}
	
	public RegulationTeamData getHigherSeedTeam() {
		return higherSeedTeam;
	}
	
	public RegulationTeamData getLowerSeedTeam() {
		return lowerSeedTeam;
	}
	
	public Match getMatchData() {
		return match;
	}
	
	public boolean isFinished() {
		return finished;
	}
	
	public RegulationTeamData getWinner() {
		return winner;
	}
	
	public PlayoffBracketMatch getPrevBracketHigherSeedMatch() {
		return prevBracketHigherSeed;
	}
	
	public PlayoffBracketMatch getPrevBracketLowerSeedMatch() {
		return prevBracketLowerSeed;
	}
}
