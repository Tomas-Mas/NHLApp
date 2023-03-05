package com.nhl.output;

import java.util.ArrayList;

import com.nhl.database.GameDataFetcher;
import com.nhl.model.gameresults.EventData;
import com.nhl.model.gameresults.EventPlayer;
import com.nhl.model.gameresults.GameData;

public class GameResults {

	private GameDataFetcher gameDataFetcher;
	
	public GameResults() {
		this.gameDataFetcher = new GameDataFetcher();
	}
	
	public Tag getResults(String season) {
		ArrayList<GameData> games = gameDataFetcher.fetchGames(season);
		Tag table = new Tag("table", "id='mainFinishedTableRegulation' class ='mainFinishedTable'");
		int rowId = 0;
		for(GameData game : games) {
			rowId++;
			table.addTag(buildTableHeader(rowId, game));
			table.addTag(buildDetailRow(rowId, game));
		}
		return table;
	}
	
	private Tag buildTableHeader(int rowId, GameData game) {
		Tag tr = new Tag("tr", "id='headerRow" + rowId + "' class='clickableTr'");
		Tag td = new Tag("td");
		
		Tag header = new Tag("table", "class='resultsHeader'");
		Tag headerHomeRow = new Tag("tr", new Tag[] {
				new Tag("td", "rowspan='2' class='resultsHeaderDate'", game.getGameDate()),
				new Tag("td", "class='resultsPics'", TeamIcon.getIconDiv(game.getHomeId(), game.getHomeName())),
				new Tag("td", "class='resultsTeamName'", game.getHomeName()),
				new Tag("td", "class='resultsScore'", String.valueOf(game.getHomeScore())),
				new Tag("td", "rowspan='2' class='resultsDetail'", game.getResultDetail())
		});
		headerHomeRow.addTag(buildPeriodsCells("home", game));
		headerHomeRow.addTag(new Tag("td", "rowspan='2' class='resultsButton'", new Tag("button", "id='" + game.getGameId() + "' class='gamePageButton'", "Game page"))); 
		//headerHomeRow.addTag(new Tag("td", "", "a"));
		
		Tag headerAwayRow = new Tag("tr", new Tag[] {
				new Tag("td", "class='resultsPics'", TeamIcon.getIconDiv(game.getAwayId(), game.getAwayName())),
				new Tag("td", "class='resultsTeamName'", game.getAwayName()),
				new Tag("td", "class='resultsScore'", String.valueOf(game.getAwayScore()))
		});
		headerAwayRow.addTag(buildPeriodsCells("away", game));
		//headerAwayRow.addTag(new Tag("td", "", "a"));
		
		header.addTag(headerHomeRow);
		header.addTag(headerAwayRow);
		td.addTag(header);
		tr.addTag(td);
		return tr;
	}
	
	private Tag[] buildPeriodsCells(String team, GameData game) {
		Tag[] periodTags = new Tag[game.getPeriods().size()];
		for(int i = 0; i < game.getPeriods().size(); i++) {
			periodTags[i] = new Tag("td", "class='resultsPeriodScore numeric'", String.valueOf(game.getPeriods().get(i).getScore(team)));
		}
		return periodTags;
	}
	
	private Tag buildDetailRow(int rowId, GameData game) {
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
						new Tag("img", "src='sources/images/game-detail-icons/goal-icon.png' title='" + e.getSecondaryType() + "'")));
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
					eventDiv.addTag(buildAssistsDiv(e.getSecondaryActors()));
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
	
	private Tag buildPlayerLink(EventPlayer player) {
		//return new HtmlTag("a", "href='player.jsp?id=" + player.getId() + "'", player.getFullName());
		//return new HtmlTag("a", "id='scorerName" + player.getId() + "' class='gameDetailPlayerName'", player.getFullName());
		return new Tag("a", "id=" + player.getId() + " class='gameDetailPlayerName'", player.getFullName());
	}
	
	private Tag buildAssistsDiv(ArrayList<EventPlayer> secondaryActors) {
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
	
	private Tag getPenaltyIconDiv(String severity, int minutes, String secondaryType) {
		Tag penaltyIconDiv = new Tag("div", "class='periodEventIcon'");
		if(minutes == 2) {
			penaltyIconDiv.addTag(new Tag("img", "src='sources/images/game-detail-icons/penalty2.png' title='" + severity + " - " + minutes + " minutes'"));
		} else if(minutes == 5) {
			penaltyIconDiv.addTag(new Tag("img", "src='sources/images/game-detail-icons/penalty5.png' title='" + severity + " - " + minutes + " minutes'"));
		} else if(minutes == 10 ) {
			penaltyIconDiv.addTag(new Tag("img", "src='sources/images/game-detail-icons/penalty10.png' title='" + secondaryType + "'"));
		} else if(minutes == 4) {
			penaltyIconDiv.addTag(new Tag("img", "src='sources/images/game-detail-icons/penalty4.png' title='" + severity + " - " + minutes + " minutes'"));
		} else if(minutes == 0) {
			penaltyIconDiv.addTag(new Tag("img", "src='sources/images/game-detail-icons/penaltyP.png' title='" + severity + " - " + secondaryType + "'"));
		} else {
			penaltyIconDiv.addTag(new Tag("img", "src='sources/images/game-detail-icons/penaltyDef.png' title='" 
					+ minutes + " - " + severity + " - " + secondaryType + "'"));
		}
		return penaltyIconDiv;
	}
	
	
}
