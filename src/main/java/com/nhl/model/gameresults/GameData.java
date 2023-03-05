package com.nhl.model.gameresults;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GameData {
	private int gameId;
	private GameType gameType;
	private int season;
	private String gameDate;
	private int homeId;
	private String homeName;
	private int homeScore;
	private int awayId;
	private String awayName;
	private int awayScore;
	private String venueName;
	private String statusName;
	
	private ArrayList<EventData> events = new ArrayList<EventData>();
	private ArrayList<PeriodData> periods = new ArrayList<PeriodData>();
	
	public GameData() {
	}
	
	/*public GameData(int gameId, String gameType, int season, String gameDate, String homeName, int homeScore, 
			String awayName, int awayScore, String venueName, String statusName) {
		this.gameId = gameId;
		this.gameType = setGameType(gameType);
		this.season = season;
		this.gameDate = gameDate;
		this.homeId = homeId;
		this.homeName = homeName;
		this.homeScore = homeScore;
		this.awayName = awayName;
		this.awayScore = awayScore;
		this.venueName = venueName;
		this.statusName = statusName;
	}*/
	
	public void setDataFromResultSet(ResultSet rs) throws SQLException {
		this.gameId = rs.getInt("g_id");
		this.gameType = setGameType(rs.getString("gameType"));
		this.season = rs.getInt("season");
		this.gameDate = rs.getString("gameDate");
		this.homeId = rs.getInt("homeTeamId");
		this.homeName = rs.getString("homeTeamName");
		this.homeScore = rs.getInt("homeScore");
		this.awayId = rs.getInt("awayTeamId");
		this.awayName = rs.getString("awayTeamName");
		this.awayScore = rs.getInt("awayScore");
		this.venueName = rs.getString("venueName");
		this.statusName = rs.getString("statusName");
		
		//System.out.println(this.gameId);
		
		rs.previous();
		while(rs.next()) {
			if(rs.getInt("g_id") == this.gameId) {
				if(periods.size() == 0) {	//periods
					PeriodData period = new PeriodData(rs.getInt("periodNumber"), rs.getInt("homePeriodScore"), rs.getInt("awayPeriodScore"));
					periods.add(period);
				} else {
					if(periods.get(periods.size() -1).getNumber() != rs.getInt("periodNumber")) {
						PeriodData period = new PeriodData(rs.getInt("periodNumber"), rs.getInt("homePeriodScore"), rs.getInt("awayPeriodScore"));
						periods.add(period);
					}
				}
				EventData event = new EventData();
				event.setDataFromResultSet(rs);
				this.events.add(event);
				
			} else {
				rs.previous();
				break;
			}
		}
	}
	
	private GameType setGameType(String gameType) {
		switch(gameType) {
		case "PR":
			return GameType.preparation;
		case "R":
			return GameType.regulation;
		case "P":
			return GameType.playoff;
		default:
			return GameType.undefined;
		}
	}
	
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public void setGameType(GameType gameType) {
		this.gameType = gameType;
	}
	public void setSeason(int season) {
		this.season = season;
	}
	public void setGameDate(String gameDate) {
		this.gameDate = gameDate;
	}
	public void setHomeName(String homeName) {
		this.homeName = homeName;
	}
	public void setHomeScore(int homeScore) {
		this.homeScore = homeScore;
	}
	public void setAwayName(String awayName) {
		this.awayName = awayName;
	}
	public void setAwayScore(int awayScore) {
		this.awayScore = awayScore;
	}
	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
	public int getGameId() {
		return this.gameId;
	}
	public GameType getGameType() {
		return this.gameType;
	}
	public int getSeason() {
		return this.season;
	}
	public String getGameDate() {
		return this.gameDate;
	}
	public int getHomeId() {
		return homeId;
	}
	public String getHomeName() {
		return this.homeName;
	}
	public int getHomeScore() {
		return this.homeScore;
	}
	public int getAwayId() {
		return awayId;
	}
	public String getAwayName() {
		return this.awayName;
	}
	public int getAwayScore() {
		return this.awayScore;
	}
	public String getVenueName() {
		return this.venueName;
	}
	public String getStatusName() {
		return this.statusName;
	}
	public ArrayList<EventData> getEvents() {
		return this.events;
	}
	public ArrayList<PeriodData> getPeriods() {
		return this.periods;
	}
	
	public String getPeriodScore(int periodNumber) {
		String score = "";
		for(PeriodData p : this.periods) {
			if(p.getNumber() == periodNumber) {
				score = p.getHomeScore() + " - " + p.getAwayScore();
			}
		}
		return score;
	}
	
	public int getScoreForPeriod(String team, int periodNumber) {
		for(PeriodData period: this.periods) {
			if(period.getNumber() == periodNumber) {
				return period.getScore(team);
			}
		}
		return 0;
	}
	
	public String getResultDetail() {
		if(periods.size() <= 3) {
			return "";
		} else if(periods.size() == 4) {
			return "OT";
		} else if(periods.size() == 5) {
			if(gameType == GameType.playoff)
				return "OT";
			else
				return "SO";
		} else if(periods.size() > 5) {
			return "OT";
		} else {
			return "N/A";
		}
	}
	
}
