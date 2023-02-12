package com.teamStats;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.database.Database;
import com.htmlOutput.Tag;

@WebServlet("/StatisticsRegulationServlet")
public class StatisticsRegulationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private Database db;
	//private String contextPath;
	
    public StatisticsRegulationServlet() {
        super();
        db = new Database();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//contextPath = request.getContextPath();
		String season = (String)request.getAttribute("season");
		StatisticsRegulationData stats = loadStats(season);
		String statsVersion = (String)request.getAttribute("statsVersion");
		
		if(statsVersion.equals("mainPage")) {
			Tag regulationStats = buildRegulationStatsDivMainPage(stats);
			request.setAttribute("regulationStats", regulationStats);
		} else if(statsVersion.equals("statPage")) {
			Tag statisticsDiv = buildRegulationStatisticsPage(stats);
			request.setAttribute("regulationStats", statisticsDiv);
		}
		
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	
	private StatisticsRegulationData loadStats(String season) {
		StatisticsRegulationData stats = null;
		Connection conn = db.createConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = conn.prepareStatement(db.readSql("teamStandings"));
			int s = Integer.parseInt(season.replace("/", ""));
			stmt.setInt(1, s);
			stmt.setInt(2, s);
			stmt.setInt(3, s);
			rs = stmt.executeQuery();
			
			stats = new StatisticsRegulationData(rs);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.closeStatement(stmt);
			db.closeResultSet(rs);
			db.closeConnection(conn);
		}
		return stats;
	}
	
	private Tag buildRegulationStatsDivMainPage(StatisticsRegulationData stats) {
		Tag regulationStatsDiv = new Tag("div", "id='regulationStats'");
		HashMap<String, ArrayList<String>> confDivMap = stats.getConferencesDivisions();
		Tag conferenceTable = new Tag("table");
		
		for(String conference : confDivMap.keySet()) {
			buildMainPageConferences(conferenceTable, conference, stats);
		}
		regulationStatsDiv.addTag(conferenceTable);
		return regulationStatsDiv;
	}
	
	private void buildMainPageConferences(Tag conferenceTable, String conference, StatisticsRegulationData stats) {
		/*Tag conferenceDiv = new Tag("div", "class='conferenceRegulationStats'");
		Tag conferenceTable = new Tag("table");*/
		conferenceTable.addTag(buildMainPageStatsHeader(conference));
		
		int rank = 1;
		for(RegulationTeamData team : stats.getTeamsByConference(conference)) {
			conferenceTable.addTag(buildMainPageStatsRow(rank, team, team.getOverallStats()));
			rank++;
		}
		
		/*conferenceDiv.addTag(conferenceTable);
		return conferenceDiv;*/
	}
	
	private Tag buildMainPageStatsHeader(String conference) {
		Tag header = new Tag("tr", "class='header'", new Tag[] {
				//new Tag("th", "title='", "#"),
				new Tag("th", "class='conferenceName' colspan='3'", conference),
				new Tag("th", "title='Games Played'", "GP"),
				new Tag("th", "title='Regulation wins'", "W"),
				new Tag("th", "title='Overtime/Shootout wins'", "OW"),
				new Tag("th", "title='OverTime/Shootout loses'", "OL"),
				new Tag("th", "title='Regulation loses'", "L"),
				new Tag("th", "title='Points'", "P"),
				
				/*new Tag("th", "class='conferenceName' colspan='3'", conference),
				new Tag("th", "title='Games Played'", "GP"),
				new Tag("th", "title='Regulation wins'", "W"),
				//new Tag("th", "title='Overtime/Shootout wins'", "OW"),
				new Tag("th", "title='OverTime/Shootout loses'", "OL"),
				new Tag("th", "title='Regulation loses'", "L"),
				//new Tag("th", "title='Goals for'", "GF"),
				//new Tag("th", "title='Goals against'", "GA"),
				//new Tag("th", "title='Goals difference'", "+/-"),
				new Tag("th", "title='Points'", "P"),*/
		});
		return header;
	}
	
	private Tag buildMainPageStatsRow(int rank, RegulationTeamData team, RegulationTeamStatistics stats) {
		Tag tr = new Tag("tr", new Tag[] {
				new Tag("td", "class='numeric'", String.valueOf(rank)),
				new Tag("td", "", new Tag("div", "class='teamPic'")),
				//new Tag("td", "id='" + team.getId() + "'", team.getName()),
				new Tag("td", "id='" + team.getId() + "' class='teamName'", team.getName()),
				new Tag("td", "class='numeric'", String.valueOf(stats.getGamesPlayed())),
				new Tag("td", "class='numeric'", String.valueOf(stats.getRegulationWins())),
				new Tag("td", "class='numeric'", String.valueOf(stats.getOvertimeWins())),
				new Tag("td", "class='numeric'", String.valueOf(stats.getOvertimeLoses())),
				new Tag("td", "class='numeric'", String.valueOf(stats.getRegulationLoses())),
				//new Tag("td", "class='numeric'", String.valueOf(stats.getGoalsFor())),
				//new Tag("td", "class='numeric'", String.valueOf(stats.getGoalsAgainst())),
				//new Tag("td", "class='numeric'", String.valueOf(stats.getGoalDifference())),
				new Tag("td", "class='numeric'", String.valueOf(stats.getPoints()))
		});
		
		return tr;
	}
	
	private Tag buildRegulationStatisticsPage(StatisticsRegulationData stats) {
		Tag regulationContainer = new Tag("div", "id='regulationContainer'");
		HashMap<String, ArrayList<String>> confDivMap = stats.getConferencesDivisions();
		Tag regulationTable = new Tag("table");
		for(String conference : confDivMap.keySet()) {
			regulationTable.addTag(buildStatPageConferenceRow(conference, confDivMap, stats));
			regulationTable.addTag(new Tag("tr", buildStatPageDivisionCell(confDivMap.get(conference).get(1), stats)));
		}
		
		regulationContainer.addTag(regulationTable);
		return regulationContainer;
	}
	
	private Tag buildStatPageConferenceRow(String conference, HashMap<String, ArrayList<String>> confDivMap, StatisticsRegulationData stats) {
		Tag tableRow = new Tag("tr", "class='conferenceRow'");
		tableRow.addTag(buildStatPageConferenceCell(conference, stats));
		tableRow.addTag(buildStatPageDivisionCell(confDivMap.get(conference).get(0), stats));
		return tableRow;
	}
	
	private Tag buildStatPageConferenceCell(String conference, StatisticsRegulationData stats) {
		Tag td = new Tag("td", "rowspan='2' class='conferenceCell'");
		Tag conferenceDataTable = buildStatPageConferenceDataTable(conference, stats);
		td.addTag(conferenceDataTable);
		return td;
	}
	
	private Tag buildStatPageDivisionCell(String division, StatisticsRegulationData stats) {
		Tag td = new Tag("td");
		td.addTag(buildStatPageDivisionDataTable(division, stats));
		return td;
	}
	
	private Tag buildStatPageConferenceDataTable(String conference, StatisticsRegulationData stats) {
		Tag table = new Tag("table");
		//Tag trTitle = new Tag("tr", new Tag("td", "", conference));
		
		//table.addTag(trTitle);
		table.addTag(buildFullStatHeader(conference));
		
		int rank = 1;
		for(RegulationTeamData team : stats.getTeamsByConference(conference)) {
			table.addTag(buildFullStatData(rank, team));
			rank++;
		}
		return table;
	}
	
	private Tag buildStatPageDivisionDataTable(String division, StatisticsRegulationData stats) {
		Tag table = new Tag("table");
		table.addTag(buildFullStatHeader(division));
		
		int rank = 1;
		for(RegulationTeamData team : stats.getTeamsByDivisions(division)) {
			table.addTag(buildFullStatData(rank, team));
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
	
	private Tag buildFullStatData(int rank, RegulationTeamData team) {
		return new Tag("tr", new Tag[] {
				new Tag("td", "class='numeric'", String.valueOf(rank)),
				new Tag("td", "", new Tag("div", "class='teamPic'")),
				new Tag("td", "id='" + team.getId() + "' class='teamName'", team.getName()),
				new Tag("td", "class='numeric'", String.valueOf(team.getOverallStats().getGamesPlayed())),
				new Tag("td", "class='numeric'", String.valueOf(team.getOverallStats().getRegulationWins())),
				new Tag("td", "class='numeric'", String.valueOf(team.getOverallStats().getOvertimeWins())),
				new Tag("td", "class='numeric'", String.valueOf(team.getOverallStats().getOvertimeLoses())),
				new Tag("td", "class='numeric'", String.valueOf(team.getOverallStats().getRegulationLoses())),
				new Tag("td", "class='numeric'", String.valueOf(team.getOverallStats().getGoalsFor())),
				new Tag("td", "class='numeric'", String.valueOf(team.getOverallStats().getGoalsAgainst())),
				new Tag("td", "class='numeric'", String.valueOf(team.getOverallStats().getGoalDifference())),
				new Tag("td", "class='numeric'", String.valueOf(team.getOverallStats().getPoints()))
		});
	}
}
