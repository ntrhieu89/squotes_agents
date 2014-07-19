package squotes.jobs;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import org.apache.log4j.Logger;

import squotes.db.DatabaseContext;
import squotes.db.TempQuote;

/**
 * This job auto-post new famous quotes into the database 
 * @author stack
 *
 */
public class AutoPostJob implements Runnable {
	private DatabaseContext dbContext;
	
	private static final Logger log = Logger.getLogger(AutoPostJob.class.getName());
	private static final String BASE_URL = "http://localhost/squotes/v2/";
	
	public AutoPostJob() {
		dbContext = new DatabaseContext();
	}

	public void run() {
		while (true) {
			// try getting a temp quote
			TempQuote tempQuote = null;							
			tempQuote = dbContext.getUnpostedQuote();
			
			if (tempQuote != null) {
				// post it using REST api
				try {
					String url = BASE_URL + "admin/quote";
					URL obj = new URL(url);
					HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			 
					//add reuqest header
					con.setRequestMethod("POST");
					con.setRequestProperty("User-Agent", "Mozilla/5.0");
					con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			 
					String urlParameters = "language=english&content=" + tempQuote.getContent() + "&author=" + tempQuote.getAuthor() + "&admin_access_token=Ap$tick";
			 
					// Send post request
					con.setDoOutput(true);
					DataOutputStream wr = new DataOutputStream(con.getOutputStream());
					wr.writeBytes(urlParameters);
					wr.flush();
					wr.close();
			 
					int responseCode = con.getResponseCode();
					
					log.info("\nSending 'POST' request to URL : " + url);
					log.info("Chosen temp quote: " + tempQuote.getId());
					log.info("Response Code : " + responseCode);
				} catch (Exception e) {
					log.error(e.toString());
				}
				
				// set it as posted int the db
				dbContext.setAQuoteAsPosted(tempQuote.getId());
			}
			
			// sleep for a random time betwwen X and Y before continue the loop
			Random rand = new Random();			
			int time = rand.nextInt(1000 * 60 * 15);
			time += 1000 * 60 * 15;			
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
