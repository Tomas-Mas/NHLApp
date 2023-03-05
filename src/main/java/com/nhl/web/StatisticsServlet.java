package com.nhl.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nhl.output.PlayoffBracket;
import com.nhl.output.RegulationStatistics;
import com.nhl.output.Tag;


public class StatisticsServlet extends HttpServlet {
	private static final long serialVersionUID = -3676588673582475027L;
	
	private RegulationStatistics regulationStats;
	private PlayoffBracket playoffBracket;

	public StatisticsServlet() {
        super();
    }
	
	public void init() {
		this.regulationStats = new RegulationStatistics();
		this.playoffBracket = new PlayoffBracket();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		request.getRequestDispatcher("/MainMenuServlet").include(request, response);
		String season = (String)request.getAttribute("season");
		
		Tag html = new Tag("html");
		html.addTag(getHeader());
		Tag body = new Tag("body");
		body.addTag((Tag)request.getAttribute("titleTag"));
		body.addTag((Tag)request.getAttribute("menuTag"));
		body.addTag((Tag)request.getAttribute("seasonTag"));
		
		Tag mainContent = new Tag("div", "id='mainContent'");
		
		//request.setAttribute("statsVersion", "statPage");
		//mainContent.addTag(getRegulationStatistics(request, response));
		mainContent.addTag(regulationStats.getStatPageRegulationStats(season));
		
		//TODO playoff spider
		//mainContent.addTag(new Tag("div", "id='playoffContainer'"));
		//request.setAttribute("statsVersion", "playoffPage");
		//mainContent.addTag(getPlayoffSpider(request, response));
		mainContent.addTag(playoffBracket.getBracketsDiv(season));
		
		
		body.addTag(mainContent);
		html.addTag(body);
		html.print(out);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private Tag getHeader() {
		Tag header = new Tag("head");
		header.addTag(new Tag("title", "", "Statistics"));
		//css
		header.addTag(new Tag("link", "rel='stylesheet' href='global/root.css'"));
		header.addTag(new Tag("link", "rel='stylesheet' href='global/main-menu/mainMenu.css'"));
		header.addTag(new Tag("link", "rel='stylesheet' href='global/title/title.css'"));
		header.addTag(new Tag("link", "rel='stylesheet' href='statistics-page/statistics.css'"));
		header.addTag(new Tag("link", "rel='stylesheet' href='statistics-page/playoff-spider.css'"));
		//scripts
		header.addTag(new Tag("script", "src='global/main-menu/menu.js'"));
		header.addTag(new Tag("script", "src='statistics-page/statistics.js'"));
		
		return header;
	}
}
