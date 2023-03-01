package com.statistics;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.htmlOutput.Tag;

public class StatisticsServlet extends HttpServlet {
	private static final long serialVersionUID = -3676588673582475027L;

	public StatisticsServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		Tag html = new Tag("html");
		html.addTag(getHeader());
		Tag body = getMenu(request, response);
		
		Tag mainContent = new Tag("div", "id='mainContent'");
		
		request.setAttribute("statsVersion", "statPage");
		mainContent.addTag(getRegulationStatistics(request, response));
		
		//TODO playoff spider
		//mainContent.addTag(new Tag("div", "id='playoffContainer'"));
		request.setAttribute("statsVersion", "playoffPage");
		mainContent.addTag(getPlayoffSpider(request, response));
		
		
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
	
	private Tag getMenu(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Tag body = new Tag("body");
		request.getRequestDispatcher("/MainMenuServlet").include(request, response);
		body.addTag((Tag)request.getAttribute("titleTag"));
		body.addTag((Tag)request.getAttribute("menuTag"));
		body.addTag((Tag)request.getAttribute("seasonTag"));
		return body;
	}
	
	private Tag getRegulationStatistics(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/StatisticsRegulationServlet").include(request, response);
		return (Tag)request.getAttribute("regulationStats");
	}
	
	private Tag getPlayoffSpider(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/PlayoffServlet").include(request, response);
		return (Tag)request.getAttribute("playoffSpider");
	}

}
