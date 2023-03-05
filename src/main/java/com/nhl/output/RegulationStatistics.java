package com.nhl.output;

import java.util.ArrayList;
import java.util.HashMap;

import com.nhl.database.RegulationStatisticsFetcher;
import com.nhl.model.regulationstatistics.RegulationStatisticsData;
import com.nhl.model.regulationstatistics.Team;
import com.nhl.model.regulationstatistics.TeamStats;

public class RegulationStatistics {

	private RegulationStatisticsFetcher statsFetcher;
	
	public RegulationStatistics() {
		this.statsFetcher = new RegulationStatisticsFetcher();
	}
	
	public Tag getMainPageRegulationStats(String season) {
		RegulationStatisticsData stats = statsFetcher.fetchStats(season);
		Tag regulationStatsDiv = new Tag("div", "id='regulationStats'");
		HashMap<String, ArrayList<String>> confDivMap = stats.getConferencesDivisions();
		Tag conferenceTable = new Tag("table");
		
		for(String conference : confDivMap.keySet()) {
			buildMainPageConferences(conferenceTable, conference, stats);
		}
		regulationStatsDiv.addTag(conferenceTable);
		return regulationStatsDiv;
	}
	
	private void buildMainPageConferences(Tag conferenceTable, String conference, RegulationStatisticsData stats) {
		conferenceTable.addTag(buildMainPageStatsHeader(conference));
		
		int rank = 1;
		for(Team team : stats.getTeamsByConference(conference)) {
			conferenceTable.addTag(buildMainPageStatsRow(rank, team, team.getOverallStats()));
			rank++;
		}
	}
	
	private Tag buildMainPageStatsHeader(String conference) {
		Tag header = new Tag("tr", "class='header'", new Tag[] {
				new Tag("th", "class='conferenceName' colspan='3'", conference),
				new Tag("th", "title='Games Played'", "GP"),
				new Tag("th", "title='Regulation wins'", "W"),
				new Tag("th", "title='Overtime/Shootout wins'", "OW"),
				new Tag("th", "title='OverTime/Shootout loses'", "OL"),
				new Tag("th", "title='Regulation loses'", "L"),
				new Tag("th", "title='Points'", "P"),
		});
		return header;
	}
	
	private Tag buildMainPageStatsRow(int rank, Team team, TeamStats stats) {
		Tag tr = new Tag("tr", new Tag[] {
				new Tag("td", "class='numeric'", String.valueOf(rank)),
				new Tag("td", TeamIcon.getIconDiv(team.getId(), team.getName())),
				new Tag("td", "id='" + team.getId() + "' class='teamName'", team.getName()),
				new Tag("td", "class='numeric'", String.valueOf(stats.getGamesPlayed())),
				new Tag("td", "class='numeric'", String.valueOf(stats.getRegulationWins())),
				new Tag("td", "class='numeric'", String.valueOf(stats.getOvertimeShootoutWins())),
				new Tag("td", "class='numeric'", String.valueOf(stats.getOvertimeShootoutLoses())),
				new Tag("td", "class='numeric'", String.valueOf(stats.getRegulationLoses())),
				new Tag("td", "class='numeric'", String.valueOf(stats.getPoints()))
		});
		
		return tr;
	}
	
	public Tag getStatPageRegulationStats(String season) {
		RegulationStatisticsData stats = statsFetcher.fetchStats(season);
		Tag regulationContainer = new Tag("div", "id='regulationContainer'");
		HashMap<String, ArrayList<String>> confDivMap = stats.getConferencesDivisions();
		Tag regulationTable = new Tag("table", "class='regulationContainerTable'");
		for(String conference : confDivMap.keySet()) {
			regulationTable.addTag(buildStatPageConferenceRow(conference, confDivMap, stats));
			regulationTable.addTag(new Tag("tr", "class='divisionTr'", buildStatPageDivisionCell(confDivMap.get(conference).get(1), stats)));
		}
		
		regulationContainer.addTag(regulationTable);
		return regulationContainer;
	}
	
	private Tag buildStatPageConferenceRow(String conference, HashMap<String, ArrayList<String>> confDivMap, RegulationStatisticsData stats) {
		Tag tableRow = new Tag("tr", "class='conferenceRow'");
		tableRow.addTag(buildStatPageConferenceCell(conference, stats));
		tableRow.addTag(buildStatPageDivisionCell(confDivMap.get(conference).get(0), stats));
		return tableRow;
	}
	
	private Tag buildStatPageConferenceCell(String conference, RegulationStatisticsData stats) {
		Tag td = new Tag("td", "rowspan='2' class='conferenceCell'");
		Tag conferenceDataTable = buildStatPageConferenceDataTable(conference, stats);
		td.addTag(conferenceDataTable);
		return td;
	}
	
	private Tag buildStatPageDivisionCell(String division, RegulationStatisticsData stats) {
		Tag td = new Tag("td");
		td.addTag(buildStatPageDivisionDataTable(division, stats));
		return td;
	}
	
	private Tag buildStatPageConferenceDataTable(String conference, RegulationStatisticsData stats) {
		Tag table = new Tag("table", "class='conferenceTable'");
		table.addTag(buildFullStatHeader(conference));
		
		int rank = 1;
		for(Team team : stats.getTeamsByConference(conference)) {
			table.addTag(buildFullStatData(rank, team));
			rank++;
		}
		return table;
	}
	
	private Tag buildStatPageDivisionDataTable(String division, RegulationStatisticsData stats) {
		Tag table = new Tag("table", "class='divisionTable'");
		table.addTag(buildFullStatHeader(division));
		
		int rank = 1;
		for(Team team : stats.getTeamsByDivisions(division)) {
			table.addTag(buildFullStatAbreviatedNameData(rank, team));
			rank++;
		}
		return table;
	}
	
	private Tag buildFullStatHeader(String title) {
		return new Tag("tr", "class='statHeader'", new Tag[] {
				new Tag("th", "class='conferenceName' colspan='3'", title),
				new Tag("th", "title='Games Played'", "GP"),
				new Tag("th", "title='Regulation wins'", "W"),
				new Tag("th", "title='Overtime/Shootout wins'", "OW"),
				new Tag("th", "title='OverTime/Shootout loses'", "OL"),
				new Tag("th", "title='Regulation loses'", "L"),
				new Tag("th", "title='Goals for'", "GF"),
				new Tag("th", "title='Goals against'", "GA"),
				new Tag("th", "title='Goals difference'", "+/-"),
				new Tag("th", "title='Points'", "P")
		});
	}
	
	private Tag buildFullStatData(int rank, Team team) {
		return new Tag("tr", "class='team" + team.getId() + " highlightable'", new Tag[] {
				new Tag("td", "class='numeric'", String.valueOf(rank)),
				new Tag("td", "", TeamIcon.getIconDiv(team.getId(), team.getName())),
				new Tag("td", "id='" + team.getId() + "' class='teamName'", team.getName()),
				new Tag("td", "class='numeric'", String.valueOf(team.getOverallStats().getGamesPlayed())),
				new Tag("td", "class='numeric'", String.valueOf(team.getOverallStats().getRegulationWins())),
				new Tag("td", "class='numeric'", String.valueOf(team.getOverallStats().getOvertimeShootoutWins())),
				new Tag("td", "class='numeric'", String.valueOf(team.getOverallStats().getOvertimeShootoutLoses())),
				new Tag("td", "class='numeric'", String.valueOf(team.getOverallStats().getRegulationLoses())),
				new Tag("td", "class='numeric'", String.valueOf(team.getOverallStats().getGoalsFor())),
				new Tag("td", "class='numeric'", String.valueOf(team.getOverallStats().getGoalsAgainst())),
				new Tag("td", "class='numeric'", String.valueOf(team.getOverallStats().getGoalDifference())),
				new Tag("td", "class='numeric'", String.valueOf(team.getOverallStats().getPoints()))
		});
	}
	
	private Tag buildFullStatAbreviatedNameData(int rank, Team team) {
		return new Tag("tr", "class='team" + team.getId() + " highlightable'", new Tag[] {
				new Tag("td", "class='numeric rank'", String.valueOf(rank)),
				new Tag("td", "", TeamIcon.getIconDiv(team.getId(), team.getName())),
				new Tag("td", "id='" + team.getId() + "' class='teamName' title='"+ team.getName() +"'", team.getAbbreviatedName()),
				new Tag("td", "class='numeric'", String.valueOf(team.getOverallStats().getGamesPlayed())),
				new Tag("td", "class='numeric'", String.valueOf(team.getOverallStats().getRegulationWins())),
				new Tag("td", "class='numeric'", String.valueOf(team.getOverallStats().getOvertimeShootoutWins())),
				new Tag("td", "class='numeric'", String.valueOf(team.getOverallStats().getOvertimeShootoutLoses())),
				new Tag("td", "class='numeric'", String.valueOf(team.getOverallStats().getRegulationLoses())),
				new Tag("td", "class='numeric'", String.valueOf(team.getOverallStats().getGoalsFor())),
				new Tag("td", "class='numeric'", String.valueOf(team.getOverallStats().getGoalsAgainst())),
				new Tag("td", "class='numeric'", String.valueOf(team.getOverallStats().getGoalDifference())),
				new Tag("td", "class='numeric'", String.valueOf(team.getOverallStats().getPoints()))
		});
	}
}
