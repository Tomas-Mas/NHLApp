package com.nhl.model.gameresults;

public class EventPlayer {
	private int id;
	private String firstName;
	private String lastName;
	private String role;
	private String teamName;
	
	public EventPlayer(int id, String firstName, String lastName, String role, String teamName) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		this.teamName = teamName;
	}
	
	public int getId() {
		return id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getFullName() {
		return firstName + " " + lastName;
	}
	
	public String getRole() {
		return role;
	}
	
	public String getTeamName() {
		return teamName;
	}
}