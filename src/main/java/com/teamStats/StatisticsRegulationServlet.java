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
		StatisticsRegulationData stats = getStats(season);
		
		//HashMap<String, ArrayList<String>> confDivs = stats.getConferencesDivisions();
		
		Tag regulationStats = buildRegulationStatsDiv(stats);
		request.setAttribute("regulationStats", regulationStats);
		
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	
	private StatisticsRegulationData getStats(String season) {
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
	
	private Tag buildRegulationStatsDiv(StatisticsRegulationData stats) {
		Tag regulationStatsDiv = new Tag("div", "id='regulationStats'");
		HashMap<String, ArrayList<String>> confDivMap = stats.getConferencesDivisions();
		for(String conference : confDivMap.keySet()) {
			regulationStatsDiv.addTag(buildConferences(conference, stats));
		}
		
		
		
		return regulationStatsDiv;
	}
	
	private Tag buildConferences(String conference, StatisticsRegulationData stats) {
		Tag conferenceDiv = new Tag("div", "class='conferenceRegulationStats'");
		Tag conferenceTable = new Tag("table");
		conferenceTable.addTag(buildHeader(conference));
		
		int rank = 1;
		for(RegulationTeamData team : stats.getTeamsByConference(conference)) {
			conferenceTable.addTag(buildRow(rank, team, team.getOverallStats()));
			rank++;
		}
		
		conferenceDiv.addTag(conferenceTable);
		return conferenceDiv;
	}
	
	private Tag buildHeader(String conference) {
		Tag header = new Tag("tr", new Tag[] {
				//new Tag("th", "title='", "#"),
				new Tag("th", "colspan='3'", conference),
				new Tag("th", "title='Games Played'", "GP"),
				new Tag("th", "title='Regulation wins'", "W"),
				new Tag("th", "title='Overtime/Shootout wins'", "OW"),
				new Tag("th", "title='OverTime/Shootout loses'", "OL"),
				new Tag("th", "title='Regulation loses'", "L"),
				new Tag("th", "title='Goals for'", "GF"),
				new Tag("th", "title='Goals against'", "GA"),
				new Tag("th", "title='Goals difference'", "+/-"),
				new Tag("th", "title='Points'", "P"),
		});
		return header;
	}
	
	private Tag buildRow(int rank, RegulationTeamData team, RegulationTeamStatistics stats) {
		Tag tr = new Tag("tr", new Tag[] {
				new Tag("td", "class='numeric'", String.valueOf(rank)),
				new Tag("td", "", new Tag("div", "class='teamPic'")),
				new Tag("td", "id='" + team.getId() + "'", team.getName()),
				new Tag("td", "class='numeric'", String.valueOf(stats.getGamesPlayed())),
				new Tag("td", "class='numeric'", String.valueOf(stats.getRegulationWins())),
				new Tag("td", "class='numeric'", String.valueOf(stats.getOvertimeWins())),
				new Tag("td", "class='numeric'", String.valueOf(stats.getOvertimeLoses())),
				new Tag("td", "class='numeric'", String.valueOf(stats.getRegulationLoses())),
				new Tag("td", "class='numeric'", String.valueOf(stats.getGoalsFor())),
				new Tag("td", "class='numeric'", String.valueOf(stats.getGoalsAgainst())),
				new Tag("td", "class='numeric'", String.valueOf(stats.getGoalDifference())),
				new Tag("td", "class='numeric'", String.valueOf(stats.getPoints()))
		});
		
		return tr;
	}
}
