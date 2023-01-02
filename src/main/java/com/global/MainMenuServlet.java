package com.global;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.htmlOutput.Tag;
import com.database.Database;

@WebServlet("/MainMenuServlet")
public class MainMenuServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	Database db;
	
    public MainMenuServlet() {
        super();
        db = new Database();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		String activeSeason = "";
		ArrayList<String> seasons = getSeasons();
		
		Cookie[] cookies = (request.getCookies());
		for(Cookie c : cookies) {
			if(c.getName().equals("season")) {
				activeSeason = c.getValue();
			}
		}
		
		if(activeSeason.equals("")) {
			activeSeason = seasons.get(0);
			Cookie c = new Cookie("season", activeSeason);
			c.setMaxAge(365 * 24 * 60 * 60);
			c.setPath("/");
			response.addCookie(c);
		}
		
		Tag menuDiv = new Tag("div", "class='menu' id='mainMenu'", new Tag[] {
				new Tag("button", "class='menulinks'", "Home"), 		// onclick functions
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
		
		Title title = new Title();
		
		title.getTitleDiv().print(out);
		menuDiv.print(out);
		seasonDiv.print(out);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private ArrayList<String> getSeasons() {
		ArrayList<String> seasons = new ArrayList<String>();
		Connection conn = db.createConnection();
		Statement st =  null;
		ResultSet rs = null;
		
		try {
			st = conn.createStatement();
			String sql = "select distinct season from games order by season desc";
			rs = st.executeQuery(sql);
			
			while(rs.next()) {
				String season = String.valueOf(rs.getInt("season"));
				seasons.add(season.substring(0, 4) + "/" + season.substring(4, 8));
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			db.closeStatement(st);
			db.closeResultSet(rs);
			db.closeConnection(conn);
		}
		return seasons;
	}

}
