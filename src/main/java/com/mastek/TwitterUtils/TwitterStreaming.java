package com.mastek.TwitterUtils;

import java.io.IOException;

import com.mastek.mapr.stream.MapRStreamProducer;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterStreaming {
	MapRStreamProducer producerInstance = MapRStreamProducer.getProducerInstance();
	TwitterStream twitterStream;
	private boolean streamState = false;
	int count = 0;
	
	private TwitterStreaming(){}
	private static TwitterStreaming twitterStreamInstance;
	public static TwitterStreaming getTwitterStreamInstance(){
		if(twitterStreamInstance == null){
			twitterStreamInstance = new TwitterStreaming();
        }
        return twitterStreamInstance;
	}
	
	public int startStreaming() throws TwitterException, IOException {
		
		if(streamState){
			System.out.println("Twitter stream is already up... Not starting again");
			return 0;
		}
		StatusListener listener = new StatusListener() {
			@Override
			public void onStatus(Status status) {
				count++;
				System.out.println(count + ". ["+ status.getUser().getName() + "] " + status.getText());
//				System.out.println(count);
				producerInstance.produce(status.getUser().getName(), status.getText());
			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}

			@Override
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}

			@Override
			public void onException(Exception ex) {
				ex.printStackTrace();
			}

			@Override
			public void onScrubGeo(long arg0, long arg1) {}

			@Override
			public void onStallWarning(StallWarning arg0) {}
		};

		System.out.println("Starting");
		
//		ConfigurationBuilder cb = new ConfigurationBuilder();
//		cb.setDebugEnabled(true)
//		  .setOAuthConsumerKey("gJXBAeCRoOBSw3YDXledBUOlS")
//		  .setOAuthConsumerSecret("VBvgQmEcR6vDQJ6mJvKwJ3fjh84oqs0sUoMWrG8mVLbZ1Zs5pi")
//		  .setOAuthAccessToken("121035200-7tpErYtVK5asz4QnnkBO39FR1J0hSS4WJgspbbFu")
//		  .setOAuthAccessTokenSecret("26LlXwOZ5l9B5M3eSnV7uCL02fFcOSbWgvH93PhS3CJ9S")
//		  .setHttpProxyHost("192.168.100.40")
//		  .setHttpProxyPort(8080)
//		  ;
		
//		TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
		twitterStream = new TwitterStreamFactory().getInstance();
		twitterStream.addListener(listener);

//		twitterStream.sample();

//		twitterStream.filter("language");
		
		FilterQuery tweetFilterQuery = new FilterQuery(); 
//		tweetFilterQuery.track(new String[]{"Bieber", "Teletubbies"});
		tweetFilterQuery.locations(new double[][]{new double[]{-180,-85}, new double[]{180,85}}); 
////		See https://dev.twitter.com/docs/streaming-apis/parameters#locations for proper location doc. 
////		Note that not all tweets have location metadata set.
		tweetFilterQuery.language(new String[]{"en"});
		twitterStream.filter(tweetFilterQuery);
		
		streamState = true;
		return 1;
	}
	
	public boolean stopStreaming(){
		try {
			if(twitterStream != null){
				System.out.println("Shutting down streaming");
				twitterStream.shutdown();
			} else{
				System.out.println("Stream already closed");
			}
			streamState = false;
			return true;
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} 
		return false;
	}
	
	public boolean getStreamState(){
		return streamState;
	}
}
