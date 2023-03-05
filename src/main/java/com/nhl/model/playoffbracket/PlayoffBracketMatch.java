package com.nhl.model.playoffbracket;

import java.util.ArrayList;

import com.nhl.model.playoffmatches.Match;
import com.nhl.model.playoffmatches.PlayoffMatches;
import com.nhl.model.regulationstatistics.Team;

public class PlayoffBracketMatch {
	
	private SeedingLabel label;
	private PlayoffMatches playoffData;
	private Team higherSeedTeam;
	private Team lowerSeedTeam;
	private Match match;
	
	private PlayoffBracketMatch prevBracketHigherSeed;
	private PlayoffBracketMatch prevBracketLowerSeed;
	private boolean finished;
	private Team winner;

	public PlayoffBracketMatch(SeedingLabel label, PlayoffMatches playoffData, Team team1, Team team2) {
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
	
	public PlayoffBracketMatch(SeedingLabel label, PlayoffMatches playoffData, PlayoffBracketMatch prevBracketHigherSeed, PlayoffBracketMatch prevBracketLowerSeed) {
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
	
	public PlayoffBracketMatch(SeedingLabel label, PlayoffMatches playoffData, PlayoffBracketMatch prevBracketHigherSeed) {
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
	
	public ArrayList<Team> getTeams() {
		ArrayList<Team> teams = new ArrayList<Team>();
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
	
	public Team getHigherSeedTeam() {
		return higherSeedTeam;
	}
	
	public Team getLowerSeedTeam() {
		return lowerSeedTeam;
	}
	
	public Match getMatchData() {
		return match;
	}
	
	public boolean isFinished() {
		return finished;
	}
	
	public Team getWinner() {
		return winner;
	}
	
	public PlayoffBracketMatch getPrevBracketHigherSeedMatch() {
		return prevBracketHigherSeed;
	}
	
	public PlayoffBracketMatch getPrevBracketLowerSeedMatch() {
		return prevBracketLowerSeed;
	}
}
