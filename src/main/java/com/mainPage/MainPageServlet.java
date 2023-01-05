package com.mainPage;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.htmlOutput.Tag;


@WebServlet("/MainPageServlet")
public class MainPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public MainPageServlet() {
        super();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		request.getRequestDispatcher("/MainMenuServlet").include(request, response);
		String season = (String)request.getAttribute("season");
		
		Tag html = new Tag("html");
		html.addTag(buildHeader());
		Tag body = new Tag("body");
		body.addTag((Tag)request.getAttribute("titleTag"));
		body.addTag((Tag)request.getAttribute("menuTag"));
		body.addTag((Tag)request.getAttribute("seasonTag"));
		
		
		
		
		Tag testDiv = new Tag("div");
		testDiv.addTag(new Tag("p", "", "this is main page and a lot of stuff is here kind of.."));
		testDiv.addTag(new Tag("p", "", season));
		
		
		Cookie[] newCookies = (Cookie[])request.getAttribute("newCookies");
		if(request.getAttribute("newCookies") != null) {
			addNewCookies(response, newCookies);
		}
		
		//printing all the tags
		body.addTag(testDiv);
		html.addTag(body);
		html.print(out);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private Tag buildHeader() {
		Tag header = new Tag("head");
		header.addTag(new Tag("title", "", "NHL"));
		//css
		header.addTag(new Tag("link", "rel='stylesheet' href='global/root.css'"));
		header.addTag(new Tag("link", "rel='stylesheet' href='global/main-menu/mainMenu.css'"));
		header.addTag(new Tag("link", "rel='stylesheet' href='global/title/title.css'"));
		//scripts
		//<script src="root/menu/menu.js"></script>
		
		
		return header;
	}
	
	private void addNewCookies(HttpServletResponse response, Cookie[] cookies) {
		for(Cookie c : cookies) {
			response.addCookie(c);
		}
	}

}