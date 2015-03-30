package org.bshouse.twitter;

import java.util.Properties;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class OAuthSetup {
	
	public static TwitterFactory factory = null;
	public static TwitterStreamFactory streamFactory = null;
	
	
	public OAuthSetup(Properties oauth_props) {
		if(factory == null) {
			loadFactories(oauth_props);
		}
	}
	
	public Twitter getTwitterInstance() {
		return factory.getInstance();

	}
	public TwitterStream getTwitterStreamInstance() {
		return streamFactory.getInstance();

	}


	private void loadFactories(Properties props){
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setOAuthConsumerKey(props.getProperty("consumer.key"));
		cb.setOAuthConsumerSecret(props.getProperty("consumer.secret"));
		cb.setOAuthAccessToken(props.getProperty("token.key"));
		cb.setOAuthAccessTokenSecret(props.getProperty("token.secret"));
		cb.setJSONStoreEnabled(true);
		Configuration c = cb.build();
		factory = new TwitterFactory(c);
		streamFactory = new TwitterStreamFactory(c);
	}
	
}
