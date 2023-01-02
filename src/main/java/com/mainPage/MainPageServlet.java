package com.mainPage;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
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
		out.print("<html>");
		
		Tag header = buildHeader();
		header.print(out);
		
		out.print("<body>");
		
		request.getRequestDispatcher("/MainMenuServlet").include(request, response);
		//out = response.getWriter();
		
		
		
		out.print("this is main page and a lot of stuff is here kind of..");
		
		out.print("</body>");
		out.print("</html>");
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

}
