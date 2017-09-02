package com.mastek.TwitterUtils;

import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TrendingTopics {
	public static void main(String[] args) {
		Twitter twitter = TwitterFactory.getSingleton();
		Trends trends;
		try {
			trends = twitter.getPlaceTrends(1);
			int count = 0;
	        for (Trend trend : trends.getTrends()) {
	            if (count < 10) {
	                System.out.println(trend.getName());
	                count++;
	            }
	        }
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		
//		try {
//			ResponseList<Location> locs = twitter.getAvailableTrends();
//			for (Location location : locs) {
//				System.out.println(location.getName());
//			}
//		} catch (TwitterException e) {
//			e.printStackTrace();
//		}
		
	}
}
