package com.mainPage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EventData {
	private int eventId;
	private String eventName;
	private int periodNumber;
	private String periodTime;
	private String strength;
	private String emptyNet;
	private String secondaryType;
	private String penaltySeverity;
	private int penaltyMinutes;
	
	private EventPlayer mainActor;	//scorer, penaltee
	private ArrayList<EventPlayer> secondaryActors = new ArrayList<EventPlayer>();	//assistances, penalty drew by
	
	public EventData(int eventId, String eventName, int periodNumber, String periodTime, String strength, String emptyNet, String secondaryType, String penaltySeverity, int penaltyMinutes) {
		this.eventId = eventId;
		this.eventName = eventName;
		this.periodNumber = periodNumber;
		this.periodTime = periodTime;
		this.strength = strength;
		this.emptyNet = emptyNet;
		this.secondaryType = secondaryType;
		this.penaltySeverity = penaltySeverity;
		this.penaltyMinutes = penaltyMinutes;
	}
	
	public EventData() {
	}
	
	public void setDataFromResultSet(ResultSet rs) throws SQLException {
		this.eventId = rs.getInt("ge_id");
		this.eventName = rs.getString("name");
		this.periodNumber = rs.getInt("periodNumber");
		this.periodTime = rs.getString("periodTime");
		this.strength = rs.getString("strength");
		this.emptyNet = rs.getString("emptyNet");
		this.secondaryType = rs.getString("secondaryType");
		this.penaltySeverity = rs.getString("secondaryType");
		this.penaltyMinutes = rs.getInt("penaltyMinutes");
		
		rs.previous();
		while(rs.next()) {
			if(rs.getInt("ge_id") == this.eventId) {	//found new event id in rs
				EventPlayer player = new EventPlayer(rs.getInt("p_id"),	rs.getString("firstName"), rs.getString("lastName"), 
						rs.getString("role"), rs.getString("actorTeamName"));
				if(player.getRole() != null) {
					if(player.getRole().equalsIgnoreCase("Scorer") || player.getRole().equalsIgnoreCase("PenaltyOn")) {
						this.mainActor = player;
					} else {
						this.secondaryActors.add(player);
					}
				}
			} else {
				rs.previous();
				break;
			}
		}
	}
	
	public void setMainActor(EventPlayer player) {
		this.mainActor = player;
	}
	
	public void addSecondaryActor(EventPlayer player) {
		this.secondaryActors.add(player);
	}
	
	public int getEventId() {
		return this.eventId;
	}
	
	public int getPeriodNumber() {
		return periodNumber;
	}
	
	public String getPeriodTime() {
		return periodTime;
	}
	
	public String getEventName() {
		return eventName;
	}
	
	public String getStrength() {
		return strength;
	}
	
	public String getEmptyNet() {
		return emptyNet;
	}
	
	public String getSecondaryType() {
		return secondaryType;
	}
	
	public String getPenaltySeverity() {
		return penaltySeverity;
	}
	
	public int getPenaltyMinutes() {
		return penaltyMinutes;
	}
	
	public EventPlayer getMainActor() {
		return mainActor;
	}
	
	public ArrayList<EventPlayer> getSecondaryActors() {
		return secondaryActors;
	}
}
