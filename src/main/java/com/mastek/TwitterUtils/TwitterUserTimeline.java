package com.mastek.TwitterUtils;

import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TwitterUserTimeline {
	
	public static void main(String[] args) {
	
		Twitter twitter = TwitterFactory.getSingleton();
		List<Status> statuses = null;
		try {
			statuses = twitter.getHomeTimeline();
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	    System.out.println("Showing home timeline.");
	    for (Status status : statuses) {
	        System.out.println(status.getUser().getName() + ":" +
	                           status.getText());
	        System.out.println();
	        System.out.println();
	        System.out.println();
	    }
		
//	    String latestStatus = "Test tweet1";
//		try {
//			Status status = twitter.updateStatus(latestStatus);
//			System.out.println("Successfully updated the status to [" + status + "].");
//		} catch (TwitterException e) {
//			e.printStackTrace();
//		}
	}
}
