package com.teamStats;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TeamStatistics {

	private int gamesPlayed;
	private int goalsFor;
	private int goalsAgainst;
	private int points;
	private int regulationWins;
	private int regulationLoses;
	private int overtimeWins;
	private int overtimeLoses;
	private int shootoutWins;
	private int shootoutLoses;
	
	public TeamStatistics() {
		this.gamesPlayed = 0;
		this.goalsFor = 0;
		this.goalsAgainst = 0;
		this.points = 0;
		this.regulationWins = 0;
		this.regulationLoses = 0;
		this.overtimeWins = 0;
		this.overtimeLoses = 0;
		this.shootoutWins = 0;
		this.shootoutLoses = 0;
	}
	
	public TeamStatistics(ResultSet rs) throws SQLException {
		this.gamesPlayed = rs.getInt("gamesPlayed");
		this.goalsFor = rs.getInt("goalsFor");
		this.goalsAgainst = rs.getInt("goalsAgainst");
		this.points = rs.getInt("points");
		this.regulationWins = rs.getInt("regulationWins");
		this.regulationLoses = rs.getInt("regulationLoses");
		this.overtimeWins = rs.getInt("overtimeWins");
		this.overtimeLoses = rs.getInt("overtimeLoses");
		this.shootoutWins = rs.getInt("shootoutWins");
		this.shootoutLoses = rs.getInt("shootoutLoses");
	}
	
	public TeamStatistics(TeamStatistics home, TeamStatistics away) {
		this.gamesPlayed = home.getGamesPlayed() + away.getGamesPlayed();
		this.goalsFor = home.getGoalsFor() + away.getGoalsFor();
		this.goalsAgainst = home.getGoalsAgainst() + away.getGoalsAgainst();
		this.points = home.getPoints() + away.getPoints();
		this.regulationWins = home.getRegulationWins() + away.getRegulationWins();
		this.regulationLoses = home.getRegulationLoses() + away.getRegulationLoses();
		this.overtimeWins = home.getOvertimeWins() + away.getOvertimeWins();
		this.overtimeLoses = home.getOvertimeLoses() + away.getOvertimeLoses();
		this.shootoutWins = home.getShootoutWins() + away.getShootoutWins();
		this.shootoutLoses = home.getShootoutLoses() + away.getShootoutLoses();
	}
	
	public int getGamesPlayed() {
		return gamesPlayed;
	}
	
	public int getGoalsFor() {
		return goalsFor;
	}
	
	public int getGoalsAgainst() {
		return goalsAgainst;
	}
	
	public int getGoalDifference() {
		return goalsFor - goalsAgainst;
	}
	
	public int getPoints() {
		return points;
	}
	
	public int getRegulationWins() {
		return regulationWins;
	}
	
	public int getRegulationLoses() {
		return regulationLoses;
	}
	
	public int getOvertimeWins() {
		return overtimeWins;
	}
	
	public int getOvertimeLoses() {
		return overtimeLoses;
	}
	
	public int getShootoutWins() {
		return shootoutWins;
	}
	
	public int getShootoutLoses() {
		return shootoutLoses;
	}
	
	public int getOvertimeShootoutWins() {
		return overtimeWins + shootoutWins;
	}
	
	public int getOvertimeShootoutLoses() {
		return overtimeLoses + shootoutLoses;
	}
	
	public int getRegulationOvertimeWins() {
		return regulationWins + overtimeWins;
	}
	
	public int getTotalWins() {
		return regulationWins + overtimeWins + shootoutWins;
	}
	
	/*public int get2PointWins() {
		return regulationWins + overtimeWins;
	}*/
}
