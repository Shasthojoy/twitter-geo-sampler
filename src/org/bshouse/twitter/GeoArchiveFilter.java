package org.bshouse.twitter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import twitter4j.FilterQuery;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;

public class GeoArchiveFilter {

	private OAuthSetup oas;

	
	public GeoArchiveFilter(String[] keywords) throws IOException {
		loadProps();
		getTweetsKeywords(oas.getTwitterStreamInstance(),keywords,null);
		
	}
	private void loadProps() throws IOException {
		Properties oauth = new Properties();
		oauth.load(new FileInputStream("."+File.separator+"oauth.properties"));
		oas = new OAuthSetup(oauth);
	}
		public void getTweetsKeywords(TwitterStream twitterStream, String[] keywords, String[] branches) throws IOException{
		
	    StatusListener listener = new LimitedStatusListener(twitterStream,10000);
	    FilterQuery fq = new FilterQuery();
	    double[][] locations = {{-180.0d,-90.0d},{180.0d,90.0d}};
	    fq.track(keywords);
	    fq.locations(locations);

	    twitterStream.addListener(listener);
	    twitterStream.filter(fq);
	}
	
	public static void main(String[] args) throws IOException {
		if(args.length > 0) {
			System.out.println("Filtering on:");
			for(String t: args) {
				System.out.println("\t"+t);
			}
			new GeoArchiveFilter(args);
		} else {
			System.out.println("Syntax: java GeoArchiveFilter [searchTerm1] [searchTerm2] [searchTermX]");
			System.out.println("Ensure you have a file named oauth.properties in the same directory as the application");
			System.out.println("It must contain:");
			System.out.println("consumer.key=[Your Consumer Key]");
			System.out.println("consumer.secret=[Your Consumer Secret]");
			System.out.println("token.key=[Your Token Key]");
			System.out.println("token.secret=[Your Token Secret]");
		}
	}
	
}
