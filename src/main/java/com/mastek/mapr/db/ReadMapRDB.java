package com.mastek.mapr.db;

import org.ojai.Document;
import org.ojai.DocumentStream;

import com.mapr.db.MapRDB;
import com.mapr.db.Table;

public class ReadMapRDB {
	private static final String TABLE_PATH = "/hashtag_count";
	private static Table table;

	static {
		if (!MapRDB.tableExists(TABLE_PATH)) {
			System.out.println("Table doesnt exist... Exiting...");
		} else {
			System.out.println("Table already found... Fetching it...");
			table = MapRDB.getTable(TABLE_PATH); // get the table
		}
	}

	public static void main(String[] args) {
		scanAllRecords();
		findById("_#celine");
	}
	
	public static void scanAllRecords(){
		System.out.println("\n\nAll records with a POJO");
		try (DocumentStream documentStream = table.find()) {
			for (Document doc : documentStream) {
				HashTagCount obj = doc.toJavaBean(HashTagCount.class);
				System.out.println(obj);
			}
		}
	}
	
	public static void findById(String id){
		System.out.println("\n\n findById - " + id);
		// get single document and map it to the bean
		Document doc = table.findById(id);
		if(doc != null){
			HashTagCount hashtagCountDbObj = doc.toJavaBean(HashTagCount.class);
			System.out.println("hashtag already exists... count="+hashtagCountDbObj.getCount());
		} 
	}
}