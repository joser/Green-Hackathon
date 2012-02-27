package com.bluevia.greenhackathon;

import java.io.IOException;
import javax.servlet.http.*;
import com.bluevia.java.AbstractClient.Mode;
import com.bluevia.java.oauth.OAuthToken;
import com.bluevia.java.sms.MessageMT;

@SuppressWarnings("serial")
public class GreenICTHackathonServlet extends HttpServlet {
	
	// App keys
	private static OAuthToken consumer = new OAuthToken(
			"your_app_key", 
			"your_app_secret");
	// User keys
	private static OAuthToken accesstoken = new OAuthToken(
			"your_user_key",
			"your user_secret");
	
	// Process request
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		doGet(req, resp);
	}
	
	// Process request
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

			// Create SMS Sender client
			MessageMT smsSender;
			int status;
			String responseMessage;		
			
			try {
				// Configure sender
				smsSender = new MessageMT(consumer, accesstoken, Mode.LIVE);
				
				// Get destination number
				String smsRecipient = req.getParameter("number");
				
				// Get message
				String message = req.getParameter("message");
				
				// Send message
				String recipients[] = { smsRecipient };
				String messageId = smsSender.send(recipients, message);
				System.out.println("Message id : " + messageId);

				// Set response to Arduino
				status = 200;
				responseMessage = "Message sent.";
				
			} catch (Exception e) {
				e.printStackTrace();
				// Set response to Arduino
				status = 500;
				responseMessage = "Sorry. Something went wrong.";
			}
			
			// Send response to Arduino
			resp.setContentType("text/plain");
			resp.setStatus(status);
			resp.getWriter().println(responseMessage);
						
	}
			
}
