package org.bshouse.twitter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterObjectFactory;
import twitter4j.TwitterStream;

public class LimitedStatusListener implements StatusListener {
	
	
	private long tweetCount = 0;
	private long min = 0;
	private BufferedWriter outputStream;
	private TwitterStream twitterStream;
	
	public LimitedStatusListener(TwitterStream ts, long minTweets) throws IOException {
		twitterStream = ts;
		min = minTweets;
		outputStream = new java.io.BufferedWriter(
				new FileWriter((new SimpleDateFormat("yyyyMMdd_HHmmss_SSS")).format(new Date())+".json"));
	}
	
	@Override
    public void onStatus(Status status) {
		writeStatus(status);
		tweetCount++;        
		if(tweetCount >= min) {
			shutdown();
		}
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
    }

    @Override
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
        System.out.println("Got track limitation notice:" + numberOfLimitedStatuses + "/"+tweetCount);
    }

    @Override
    public void onScrubGeo(long userId, long upToStatusId) {
        System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
    }

    @Override
    public void onException(Exception ex) {
        ex.printStackTrace();
    }

	@Override
	public void onStallWarning(StallWarning stallWarning) {
		System.out.println("Stall Warning: "+stallWarning.getMessage());
		System.out.println("Count: "+tweetCount);
		
	}
	private void writeStatus(Status s) {
		try {
			outputStream.write(TwitterObjectFactory.getRawJSON(s)+"\n");
			outputStream.flush();
		} catch(Exception e) {
			System.out.println(e.toString());
		}
	}
	private void shutdown() {
		twitterStream.clearListeners();
		twitterStream.shutdown();
		try {
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
