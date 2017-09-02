package com.mastek.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mastek.TwitterUtils.TwitterStreaming;

import twitter4j.TwitterException;

@WebServlet("/myservlet")
public class MyServlet extends HttpServlet {
	TwitterStreaming twitterStreamingObj = TwitterStreaming.getTwitterStreamInstance();
	
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	System.out.println("Servlet called");
    	
        if (request.getParameter("start") != null) {
        	System.out.println("Starting streaming via jsp");
        	try {
				twitterStreamingObj.startStreaming();
			} catch (TwitterException e) {
				e.printStackTrace();
			}
        	request.getRequestDispatcher("shutdown.jsp").forward(request, response);
        } else if (request.getParameter("stop") != null) {
        	System.out.println("Stop streaming via jsp");
        	twitterStreamingObj.stopStreaming();
        	request.getRequestDispatcher("index.jsp").forward(request, response);
        } else {
            System.out.println("Request parameter not matched");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
        
    }

}