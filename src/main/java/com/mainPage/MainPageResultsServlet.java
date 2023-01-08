package com.mainPage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.database.Database;
import com.htmlOutput.Tag;

@WebServlet("/MainPageResultsServlet")
public class MainPageResultsServlet extends HttpServlet {
	private static final long serialVersionUID = 4326926228916888024L;
	
	private Database db;
	private String contextPath;
	
	public MainPageResultsServlet() {
		super();
		db = new Database();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		contextPath = request.getContextPath();
		String season = (String)request.getAttribute("season");
		ArrayList<GameData> games = getGames(season);
		Tag table = buildTable(games);
		request.setAttribute("results", new Tag("div", "id='mainFinishedGames'", table));
		
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private ArrayList<GameData> getGames(String season) {
		ArrayList<GameData> games = new ArrayList<GameData>();
		Connection conn = db.createConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			pst = conn.prepareStatement(db.readSql("mainPageResults"), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			pst.setInt(1, Integer.parseInt(season.replace("/", "")));
			rs = pst.executeQuery();
			
			GameData game = new GameData();
			while(rs.next()) {
				if(rs.getInt("g_id") != game.getGameId()) {	//found new game id in rs
					if(game.getGameId() != 0) {  //adding not-first game
						games.add(game);
						game = new GameData();
					}
					game.setDataFromResultSet(rs);
				}
			}
			games.add(game);	//adding last game into list
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			db.closeStatement(pst);
			db.closeResultSet(rs);
			db.closeConnection(conn);
		}
		return games;
	}
	
	private Tag buildTable(ArrayList<GameData> games) {
		Tag table = new Tag("table", "id='mainFinishedTable'");
		int rowId = 0;
		for(GameData game : games) {
			rowId++;
			table.addTag(builtTableHeader(rowId, game));
			table.addTag(buildDetailRow(rowId, game));
		}

		
		
		return table;
	}
	
	private Tag builtTableHeader(int rowId, GameData game) {
		Tag tr = new Tag("tr", "id='headerRow" + rowId + "' class='clickableTr'", new Tag[] {
				new Tag("td", "class='mainTableCell'", new Tag[] {
						new Tag("div", "class='homePic'"),
						new Tag("div", "class='homeName'", new Tag("p", "class='teamName'", game.getHomeName()))
				}),
				new Tag("td", "class='mainTableCell'", new Tag[] {
						new Tag("div", "class='score'", new Tag("p", "class='score'", game.getHomeScore() + " : " + game.getAwayScore())),
						new Tag("div", "class='gamePageButtonDiv'", 
								new Tag("button", "id='" + game.getGameId() + "' class='gamePageButton'", "Game page"))
				}),
				new Tag("td", "class='mainTableCell'", new Tag[] {
						new Tag("div", "class='awayPic'"),
						new Tag("div", "class='awayName'", new Tag("p", "class='teamName'", game.getAwayName()))
				})
				
		});
		return tr;
	}
	
	public Tag buildDetailRow(int rowId, GameData game) {
		Tag tr = new Tag("tr", "id='dataRow" + rowId + "' class='dataRow' style='display:none'");
		Tag td = new Tag("td", "class='matchData' colspan=3");
		Tag eventDiv;
		int periodNum = 0;
		for(EventData e : game.getEvents()) {
			//printing period headers
			if(e.getEventId() == 0) {	//game with no data
				tr = new Tag();
				return tr;
			}
			if(e.getPeriodNumber() != periodNum) {
				if(e.getPeriodNumber() != (periodNum + 1)) {
					while(e.getPeriodNumber() != (periodNum + 1)) {
						periodNum++;
						td.addTag(new Tag("div", "class='periodHeader'", new Tag[] {
								new Tag("div", "", periodNum + ". period"),
								new Tag("div", "", "0 - 0")
						}));
					}
				}
				periodNum = e.getPeriodNumber();
				td.addTag(new Tag("div", "class='periodHeader'", new Tag[] {
						new Tag("div", "", periodNum + ". period"),
						new Tag("div", "", game.getPeriodScore(periodNum))
				}));
			}
			
			//printing event line
			if(e.getMainActor().getTeamName().equalsIgnoreCase(game.getHomeName())) {
				eventDiv = new Tag("div", "class='eventLineHome'");
			} else if(e.getMainActor().getTeamName().equalsIgnoreCase(game.getAwayName())) {
				eventDiv = new Tag("div", "class='EventLineAway'");
			} else {
				eventDiv = new Tag("div", "class='eventLineUndefined'");
			}
			
			if(e.getEventName().equals("Goal")) {
				eventDiv.addTag(new Tag("div", "class='periodEventTime'", e.getPeriodTime()));
				eventDiv.addTag(new Tag("div", "class='periodEventIcon'", 
						new Tag("img", "src='" + contextPath + "/sources/images/game-detail-icons/goal-icon.png' title='" + e.getSecondaryType() + "'")));
				eventDiv.addTag(new Tag("div", "class='periodEventMainPlayer'", buildPlayerLink(e.getMainActor())));
				if(!e.getStrength().equals("Even")) {
					eventDiv.addTag(new Tag("div", "class='periodEventStrength'", e.getStrength()));
				}
				if(e.getEmptyNet() != null) {
					if(e.getEmptyNet().equals("true")) {
						eventDiv.addTag(new Tag("div", "class='periodEventStrength'", "(empty net)"));
					}
				}
				if(!e.getSecondaryActors().isEmpty()) {
					eventDiv.addTag(getAssistsDiv(e.getSecondaryActors()));
				}
			} else if(e.getEventName().equals("Penalty")) {
				eventDiv.addTag(new Tag("div", "class='periodEventTime'", e.getPeriodTime()));
				eventDiv.addTag(getPenaltyIconDiv(e.getPenaltySeverity(), e.getPenaltyMinutes(), e.getSecondaryType()));
				eventDiv.addTag(new Tag("div", "class='periodEventMainPlayer'", buildPlayerLink(e.getMainActor())));
				eventDiv.addTag(new Tag("div", "class='periodEventPenaltyType'", "(" + e.getSecondaryType() + ")"));
			}
			td.addTag(eventDiv);
		}
		tr.addTag(td);
		return tr;
	}
	
	public Tag buildPlayerLink(EventPlayer player) {
		//return new HtmlTag("a", "href='player.jsp?id=" + player.getId() + "'", player.getFullName());
		//return new HtmlTag("a", "id='scorerName" + player.getId() + "' class='gameDetailPlayerName'", player.getFullName());
		return new Tag("a", "id=" + player.getId() + " class='gameDetailPlayerName'", player.getFullName());
	}
	
	public Tag getAssistsDiv(ArrayList<EventPlayer> secondaryActors) {
		int assists = 0;
		String assistsText = "";
		for(EventPlayer player : secondaryActors) {
			if(assists > 0) {
				assistsText += " + " + player.getFullName();
			} else {
				assistsText += player.getFullName();
			}
			assists++;
		}
		return new Tag("div", "class='periodEventAssist'", assistsText);
	}
	
	public Tag getPenaltyIconDiv(String severity, int minutes, String secondaryType) {
		Tag penaltyIconDiv = new Tag("div", "class='periodEventIcon'");
		if(minutes == 2) {
			penaltyIconDiv.addTag(new Tag("img", "src='" + contextPath + "/sources/images/game-detail-icons/penalty2.png' title='" + severity + " - " + minutes + " minutes'"));
		} else if(minutes == 5) {
			penaltyIconDiv.addTag(new Tag("img", "src='" + contextPath + "/sources/images/game-detail-icons/penalty5.png' title='" + severity + " - " + minutes + " minutes'"));
		} else if(minutes == 10 ) {
			penaltyIconDiv.addTag(new Tag("img", "src='" + contextPath + "/sources/images/game-detail-icons/penalty10.png' title='" + secondaryType + "'"));
		} else if(minutes == 4) {
			penaltyIconDiv.addTag(new Tag("img", "src='" + contextPath + "/sources/images/game-detail-icons/penalty4.png' title='" + severity + " - " + minutes + " minutes'"));
		} else if(minutes == 0) {
			penaltyIconDiv.addTag(new Tag("img", "src='" + contextPath + "/sources/images/game-detail-icons/penaltyP.png' title='" + severity + " - " + secondaryType + "'"));
		} else {
			penaltyIconDiv.addTag(new Tag("img", "src='" + contextPath + "/sources/images/game-detail-icons/penaltyDef.png' title='" 
					+ minutes + " - " + severity + " - " + secondaryType + "'"));
		}
		return penaltyIconDiv;
	}
	
}
