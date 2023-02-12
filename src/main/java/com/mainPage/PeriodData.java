package com.mainPage;

public class PeriodData {
	private int number;
	private int homeScore;
	private int awayScore;
	
	/*public PeriodData() {
	}*/
	
	public PeriodData(int number, int homeScore, int awayScore) {
		this.number = number;
		this.homeScore = homeScore;
		this.awayScore = awayScore;
	}
	
	public int getNumber() {
		return this.number;
	}
	public int getHomeScore() {
		return this.homeScore;
	}
	public int getAwayScore() {
		return this.awayScore;
	}
	public int getScore(String team) {
		if(team.equals("home"))
			return this.homeScore;
		else if(team.equals("away"))
			return this.awayScore;
		else
			return 100;
	}
}
