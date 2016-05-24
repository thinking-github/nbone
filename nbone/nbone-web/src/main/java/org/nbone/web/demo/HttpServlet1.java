package org.nbone.web.demo;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpServlet1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public HttpServlet1() {
        super();
    }

	public void init(ServletConfig config) throws ServletException {
		
		System.out.println("=========================");
		System.out.println("=========================");
		System.out.println("===========99999999999999==============");
		System.out.println("===========99999999999999==============");
		System.out.println("=========================");
		System.out.println("=========================");
		
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
