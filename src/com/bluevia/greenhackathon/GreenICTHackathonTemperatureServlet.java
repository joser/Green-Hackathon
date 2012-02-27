package com.bluevia.greenhackathon;

import java.io.IOException;
import javax.servlet.http.*;
import com.bluevia.java.AbstractClient.Mode;
import com.bluevia.java.oauth.OAuthToken;
import com.bluevia.java.sms.MessageMT;

@SuppressWarnings("serial")
public class GreenICTHackathonTemperatureServlet extends HttpServlet {
	
	// App keys
	private static OAuthToken consumer = new OAuthToken(
			"your_app_key", 
			"your_app_secret");
	// User keys
	private static OAuthToken accesstoken = new OAuthToken(
			"your_user_key",
			"your_user_secret");
	
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
				
				// Get temperature
				int temperature = Integer.valueOf(req.getParameter("temperature"));
				
				// Evaluate temperature and send message
				
				if(temperature > 30) {
				
					// Get destination number
					String smsRecipient = req.getParameter("number");
					
					// Get message
					String message = "Temperature measure from Arduino is above 30 degrees";
					
					// Send message
					String recipients[] = { smsRecipient };
					String messageId = smsSender.send(recipients, message);
					System.out.println("Message id : " + messageId);
	
					// Set response to Arduino
					status = 200;
					responseMessage = "Message sent";
				
				} else {

					// Set response to Arduino
					status = 200;
					responseMessage = "No message needed to be sent";
					
				}
					
				
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
