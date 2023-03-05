package com.nhl.web;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nhl.database.SeasonFetcher;
import com.nhl.output.Tag;
import com.nhl.output.PageTitle;

@WebServlet("/MainMenuServlet")
public class MainMenuServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private SeasonFetcher seasonFetcher;
	
    public MainMenuServlet() {
        super();
        seasonFetcher = new SeasonFetcher();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String activeSeason = "";
		ArrayList<String> seasons = seasonFetcher.getSeasons();
		
		Cookie[] cookies = (request.getCookies());
		if(cookies != null) {
			for(Cookie c : cookies) {
				if(c.getName().equals("season")) {
					activeSeason = c.getValue();
				}
			}
		}
		
		if(activeSeason.equals("")) {
			activeSeason = seasons.get(0);
			Cookie c = new Cookie("season", activeSeason);
			c.setMaxAge(365 * 24 * 60 * 60);
			c.setPath("/");
			request.setAttribute("newCookies", new Cookie[] {c});
		}
		request.setAttribute("season", activeSeason);
		
		Tag menuDiv = new Tag("div", "class='menu' id='mainMenu'", new Tag[] {
				new Tag("button", "class='menulinks'", "Home"),
				new Tag("button", "class='menulinks'", "Statistics"),
				new Tag("button", "class='menulinks'", "TODO")});
		
		Tag seasonSelector = new Tag("select", "name='season' id='season' disabled");
		for(String season : seasons) {
			if(season.equals(activeSeason))
				seasonSelector.addTag(new Tag("option", "value='" + season + "' selected", season));
			else
				seasonSelector.addTag(new Tag("option", "value='" + season + "'", season));
		}
		Tag seasonDiv = new Tag("div", "class='season'", seasonSelector);
		
		request.setAttribute("titleTag", PageTitle.getTitleDiv());
		request.setAttribute("menuTag", menuDiv);
		request.setAttribute("seasonTag", seasonDiv);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
