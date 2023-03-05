package com.nhl.output;

import com.nhl.database.PlayoffBracketFetcher;
import com.nhl.database.RegulationStatisticsFetcher;

import com.nhl.model.playoffbracket.PlayoffBracketMatch;
import com.nhl.model.playoffbracket.PlayoffSeeding;
import com.nhl.model.playoffbracket.SeedingLabel;

import com.nhl.model.playoffmatches.Game;
import com.nhl.model.playoffmatches.Match;
import com.nhl.model.playoffmatches.PlayoffMatches;

import com.nhl.model.regulationstatistics.RegulationStatisticsData;
import com.nhl.model.regulationstatistics.Team;

public class PlayoffBracket {

	private PlayoffBracketFetcher playoffFetcher;
	private RegulationStatisticsFetcher regulationStatsFetcher;
	private PlayoffSeeding playoffSeeding;
	private PlayoffBracketMatch finals;
	
	public PlayoffBracket() {
		this.playoffFetcher = new PlayoffBracketFetcher();
		this.regulationStatsFetcher = new RegulationStatisticsFetcher();
		this.playoffSeeding = null;
	}
	
	public Tag getBracketsDiv(String season) {
		Tag playoffContainer = new Tag("div", "id='playoffContainer'");
		RegulationStatisticsData regulationStats = regulationStatsFetcher.fetchStats(season);
		PlayoffMatches playoffMatches = playoffFetcher.fetchPlayoffMatches(season);
		
		boolean topBracket = true;
		for(String conference : regulationStats.getConferencesDivisions().keySet()) {
			playoffSeeding = new PlayoffSeeding(regulationStats, playoffMatches, conference, season);
			if(topBracket)
				playoffContainer.addTag(buildTopBracketDiv(playoffMatches));
			else {
				Tag bottomBracket = buildBottomBracketDiv();
				//playoffContainer.addTag(buildBottomBracketDiv());
				playoffContainer.addTag(buildFinals());
				playoffContainer.addTag(bottomBracket);
			}
			topBracket = false;
		}
		return playoffContainer;
	}
	
	private Tag buildTopBracketDiv(PlayoffMatches playoffMatches) {
		Tag topBracketContainer = new Tag("div", "class='conference-bracket-container'");
		topBracketContainer.addTag(buildFirstRoundMatchDiv(playoffSeeding.getMatch(SeedingLabel.FIRSTROUNDDIV1WINNER), false));
		topBracketContainer.addTag(buildFirstRoundMatchDiv(playoffSeeding.getMatch(SeedingLabel.FIRSTROUNDDIV1RUNNERUP), true));
		topBracketContainer.addTag(buildFirstRoundMatchDiv(playoffSeeding.getMatch(SeedingLabel.FIRSTROUNDDIV2RUNNERUP), false));
		topBracketContainer.addTag(buildFirstRoundMatchDiv(playoffSeeding.getMatch(SeedingLabel.FIRSTROUNDDIV2WINNER), true));
		
		topBracketContainer.addTag(buildSecondThirdRoundDiv(playoffSeeding.getMatch(SeedingLabel.DIV1FINALS), false, "div-finals-top"));
		topBracketContainer.addTag(buildSecondThirdRoundDiv(playoffSeeding.getMatch(SeedingLabel.DIV2FINALS), true, "div-finals-bot"));
		
		topBracketContainer.addTag(buildSecondThirdRoundDiv(playoffSeeding.getMatch(SeedingLabel.CONFERENCEFINALS), false, "conf-finals"));
		
		finals = new PlayoffBracketMatch(SeedingLabel.FINALS, playoffMatches, playoffSeeding.getMatch(SeedingLabel.CONFERENCEFINALS));
		
		return topBracketContainer;
	}
	
	private Tag buildBottomBracketDiv() {
		Tag botBracketContainer = new Tag("div", "class='conference-bracket-container'");
		botBracketContainer.addTag(buildFirstRoundMatchDiv(playoffSeeding.getMatch(SeedingLabel.FIRSTROUNDDIV2WINNER), false));
		botBracketContainer.addTag(buildFirstRoundMatchDiv(playoffSeeding.getMatch(SeedingLabel.FIRSTROUNDDIV2RUNNERUP), true));
		botBracketContainer.addTag(buildFirstRoundMatchDiv(playoffSeeding.getMatch(SeedingLabel.FIRSTROUNDDIV1RUNNERUP), false));
		botBracketContainer.addTag(buildFirstRoundMatchDiv(playoffSeeding.getMatch(SeedingLabel.FIRSTROUNDDIV1WINNER), true));
		
		botBracketContainer.addTag(buildSecondThirdRoundDiv(playoffSeeding.getMatch(SeedingLabel.DIV2FINALS), false, "div-finals-top"));
		botBracketContainer.addTag(buildSecondThirdRoundDiv(playoffSeeding.getMatch(SeedingLabel.DIV1FINALS), true, "div-finals-bot"));
		
		botBracketContainer.addTag(buildSecondThirdRoundDiv(playoffSeeding.getMatch(SeedingLabel.CONFERENCEFINALS), true, "conf-finals"));
		
		finals.setPrevBracketLowerSeed(playoffSeeding.getMatch(SeedingLabel.CONFERENCEFINALS));
		
		return botBracketContainer;
	}
	
	private Tag buildFirstRoundMatchDiv(PlayoffBracketMatch match, boolean flip) {
		Tag firstRoundContainer = new Tag("div", "class='first-round playoff-bracket'");
		if(flip) {
			firstRoundContainer.addTag(buildMatchDiv(match.getLowerSeedTeam(), match.getHigherSeedTeam(), match.getMatchData()));
		} else {
			firstRoundContainer.addTag(buildMatchDiv(match.getHigherSeedTeam(), match.getLowerSeedTeam(), match.getMatchData()));
		}
		return firstRoundContainer;
	}
	
	private Tag buildSecondThirdRoundDiv(PlayoffBracketMatch match, boolean flip, String classText) {
		Tag divisionFinalsContainer = new Tag("div", "class='" + classText + " playoff-bracket'");
		
		if(match.getPrevBracketHigherSeedMatch().isFinished() && match.getPrevBracketLowerSeedMatch().isFinished()) {
			if(flip) {
				divisionFinalsContainer.addTag(buildMatchDiv(match.getLowerSeedTeam(), match.getHigherSeedTeam(), match.getMatchData()));
			} else {
				divisionFinalsContainer.addTag(buildMatchDiv(match.getHigherSeedTeam(), match.getLowerSeedTeam(), match.getMatchData()));
			}
		} else {
			if(flip) {
				divisionFinalsContainer.addTag(buildMatchDiv(match.getPrevBracketLowerSeedMatch(), match.getPrevBracketHigherSeedMatch()));
			} else {
				divisionFinalsContainer.addTag(buildMatchDiv(match.getPrevBracketHigherSeedMatch(), match.getPrevBracketLowerSeedMatch()));
			}
		}
		return divisionFinalsContainer;
	}
	
	private Tag buildFinals() {
		Tag[] matchBracket = new Tag[2];
		if(finals.getMatchData() != null) {
			matchBracket = buildMatchDiv(finals.getHigherSeedTeam(), finals.getLowerSeedTeam(), finals.getMatchData());
		} else {
			matchBracket = buildMatchDiv(finals.getPrevBracketHigherSeedMatch(), finals.getPrevBracketLowerSeedMatch());
		}
		
		return new Tag("div", "class='playoff-finals-container'", new Tag[] {
				new Tag("div", "class='stanley-cup-logo'", new Tag("img", "src='sources/images/logos/stanley_cup_logo.png'")),
				new Tag("div", "class='stanley-cup-match playoff-bracket'", matchBracket)
		});
	}
	
	private Tag[] buildMatchDiv(Team team1, Team team2, Match match) {
		Tag matchHeader = new Tag("div", "class='match-header'");
		Tag matchTable = new Tag("table", "class='match-table'");
		if(match == null) {
			matchTable.addTag(buildTeamRow(team1, "-"));
			matchTable.addTag(buildTeamRow(team2, "-"));
		}
		if(match != null) {
			matchTable.addTag(buildTeamRow(team1, match.getTeamScore(team1.getId())));
			matchTable.addTag(buildTeamRow(team2, match.getTeamScore(team2.getId())));
		}
		matchHeader.addTag(matchTable);
		return new Tag[] {matchHeader, buildMatchResults(match)};
	}
	
	private Tag[] buildMatchDiv(PlayoffBracketMatch match1, PlayoffBracketMatch match2) {
		Tag matchHeader = new Tag("div", "class='match-header'");
		Tag matchTable = new Tag("table", "class='match-table'");
		if(match1.isFinished()) {
			matchTable.addTag(buildTeamRow(match1.getWinner(), "-"));
		} else {
			matchTable.addTag(buildTeamRow(match1));
		}
		if(match2.isFinished()) {
			matchTable.addTag(buildTeamRow(match2.getWinner(), "-"));
		} else {
			matchTable.addTag(buildTeamRow(match2));
		}
		
		matchHeader.addTag(matchTable);
		
		if(match1.isFinished() && match2.isFinished()) {
			return new Tag[] {matchHeader, buildMatchResults(finals.getMatchData())};
		}
		return new Tag[] {matchHeader, buildMatchResults(null)};
	}
	
	private Tag buildTeamRow(Team team, String teamScore) {
		Tag row = new Tag("tr", "class='team" + team.getId() + " highlightable'");
		row.addTag(new Tag("td", "class='team-pic'", TeamIcon.getIconDiv(team.getId(), team.getName())));
		row.addTag(new Tag("td", "class='team-seeding'", "(" + playoffSeeding.getTeamSeeding(team) + ")"));
		if(teamScore.equals("4")) {
			row.addTag(new Tag("td", "class='team-name bracket-winner'", team.getAbbreviatedName()));
		} else {
			row.addTag(new Tag("td", "class='team-name'", team.getAbbreviatedName()));
		}
		row.addTag(new Tag("td", "class='team-points'", teamScore));
		return row;
	}
	
	private Tag buildTeamRow(PlayoffBracketMatch match) {
		Tag row = new Tag("tr");
		if(match.getHigherSeedTeam() != null && match.getLowerSeedTeam() != null) {
			row.addTag(new Tag("td", "class='team-pic-undecided'", new Tag[] {
					TeamIcon.getIconDiv(match.getHigherSeedTeam().getId(), match.getHigherSeedTeam().getName(), "upper-pic"),
					TeamIcon.getIconDiv(match.getLowerSeedTeam().getId(), match.getLowerSeedTeam().getName(), "lower-pic")
			}));
			row.addTag(new Tag("td", "class='team-seeding'"));
			row.addTag(new Tag("td", "class='team-name'", "TBD"));
			row.addTag(new Tag("td", "class='team-points'", "-"));
		} else {
			row.addTag(new Tag("td", "class='team-pic'", TeamIcon.getTBDIconDiv()));
			row.addTag(new Tag("td", "class='team-seeding'"));
			row.addTag(new Tag("td", "class='team-name'", "TBD"));
			row.addTag(new Tag("td", "class='team-points'"));
		}
		return row;
	}
	
	private Tag buildMatchResults(Match match) {
		Tag resultsDiv = new Tag("div", "class='match-results' style='display: none;'");
		Tag resultsTable = new Tag("table", "class='results-table'");
		if(match == null) {
			resultsTable.addTag(buildMatchResultRow());
		} else {
			for(Game game : match.getGames()) {
				resultsTable.addTag(buildMatchResultRow(game));
			}
		}
		resultsDiv.addTag(resultsTable);
		return resultsDiv;
	}
	
	private Tag buildMatchResultRow() {
		return new Tag("tr");
	}
	
	private Tag buildMatchResultRow(Game game) {
		return new Tag("tr", "id='" + game.getGameId() + "'", new Tag[] {
				new Tag("td", "class='res-date'", game.getGameDate().substring(0, 6)),
				new Tag("td", "class='res-teams'", game.getHomeAbbreviation() + "-" + game.getAwayAbbreviation()),
				new Tag("td", "class='res-score'", game.getHomeScore() + "-" + game.getAwayScore())
		});
	}
}
